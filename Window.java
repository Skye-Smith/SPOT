import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Window extends JFrame {
    private ArrayList<String> list = new ArrayList<String>();
    private JLabel img = new JLabel();
    private String currentImg;
    private String dir;
    private int stage;

    // Initialize a Window object, given the name of a directory
    public Window(String directory) {
        dir = directory;
        searchDir(dir);

        changeImage(currentImg);

        setUndecorated(true);
        add(img);
    }

    // Takes in the name of a directory, and updates the contents of the ArrayList list
    private void searchDir(String dir) {
        // Try in order to catch IOException 
        try {
            // Returns all filenames in given directory in the form "dirname/filename"
            Stream<Path> fStream = Files.list(Paths.get(dir));
            ArrayList<Path> pathList = new ArrayList<Path>();
            list = new ArrayList<String>();

            // Adds each filepath to an ArrayList of paths
            fStream.forEach(pathList::add);
            fStream.close();

            // Converts each path to a String, and adds it to list
            for (int i = 0; i < pathList.size(); i++) {
                // Trims the path of its directory, the /, and the first character of the filename
                // This trimming makes processing the filename easier in other functions
                list.add(pathList.get(i).toString().substring(dir.length() + 2));
            }

            // Resets value of stage
            stage = 0;

            // Updates the value of currentImg
            String id = dir.substring(0,1).toUpperCase();
            currentImg = id + stage + ".png";
        }
        catch (IOException e) {
            // Exits the program if the given directory does not exist
            System.out.println("Directory: ./" + dir + " Does not exist");
            System.exit(0);
        }
    }

    // Finds the next image in the sequence, given whether A was pressed, and the sequence of directories
    public ArrayList<String> nextImg(boolean aState, ArrayList<String> seqQueue) {
        // Trims the value of currentImg to just characters between the file extension, and the number representing the depth of the image
        String choiceTree = currentImg.substring(String.valueOf(stage).length() + 1);
        choiceTree = choiceTree.substring(0, choiceTree.length() - 4);

        String search;
        String found = "";
        stage++;

        // Create a string from the value of stage, choiceTree and A/B depending on which button was pressed
        if (aState == true) {
            search = stage + choiceTree + "A";
        }
        else {
            search = stage + choiceTree + "B";
        }

        // Search the list of filenames for somethat that matches with search, or search without the added A/B
        for (int i = 0; i < list.size(); i++) {
            // Trim the ".png" from each filename
            String temp = list.get(i);
            temp = temp.substring(0, temp.length() - 4);

            // Create a second String from search, that does not include the A/B of the pressed button
            String searchAlt = search.substring(0, search.length() - 1);

            // Update value of found and quit the search if a match is found
            if (searchAlt.equals(temp) || search.equals(temp)) {
                found = temp;
                break;
            }
        }

        // Check if no match was found
        if (found.equals("")) {
            // Check if at end of idle sequence, if yes, quit program
            if (dir.equals("idle")) {
                System.out.println("End of idle sequence, quitting");
                System.exit(0);
            }

            // Remove the first item from seqQueue
            seqQueue.remove(0);
            // Try to update dir to the new index 0 of seqQueue
            try {
                dir = seqQueue.get(0);
            }
            // If seqQueue is now empty, quit program
            catch (IndexOutOfBoundsException e) {
                System.out.println("No Seqeuences Queued");
                System.exit(0);
            }
            // Update the ArrayList list with the filenames from the new directory
            searchDir(dir);
        }
        // If match was found, update currentImg to be the match plus necessary identifiers
        else {
            String id = dir.substring(0,1).toUpperCase();
            currentImg = id + found + ".png";
        }

        // Update the displayed image to the image found at dir/currentImg
	    changeImage(currentImg);

        return seqQueue;
    }

    // Updates displayed icon based on a given String filename, to the image at dir/filename
    public void changeImage(String filename) {
        // Create a string of the desired filepath
        String path = dir + "/" + currentImg;
        // Update the icon
        img.setIcon(new ImageIcon(path));
        // Repaint the component
        img.repaint();
    }
}
