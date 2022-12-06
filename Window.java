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

    public Window(String directory) {
        dir = directory;
        searchDir(dir);

        img.setIcon(new ImageIcon(currentImg));

        setUndecorated(true);
        
        add(img);
    }

    private void searchDir(String dir) {
        try {
            Stream<Path> fStream = Files.list(Paths.get(dir));
            ArrayList<Path> pathList = new ArrayList<Path>();
            list = new ArrayList<String>();

            fStream.forEach(pathList::add);
            fStream.close();

            for (int i = 0; i < pathList.size(); i++) {
                list.add(pathList.get(i).toString().substring(dir.length() + 2));
            }

            stage = 0;

            String id = dir.substring(0,1).toUpperCase();
            currentImg = id + stage + ".png";
        }
        catch (IOException e) {
            System.out.println("Directory: ./" + dir + " Does not exist");
            System.exit(0);
        }
    }

    public ArrayList<String> nextImg(boolean aState, ArrayList<String> seqQueue) {
        String choiceTree = currentImg.substring(String.valueOf(stage).length() + 1);
        System.out.println(choiceTree);
        choiceTree = choiceTree.substring(0, choiceTree.length() - 4);

        String search;
        String found = "";
        stage++;

        if (aState == true) {
            search = stage + choiceTree + "A";
        }
        else {
            search = stage + choiceTree + "B";
        }

        for (int i = 0; i < list.size(); i++) {
            String temp = list.get(i);
            temp = temp.substring(0, temp.length() - 4);
            
            String searchAlt = search.substring(0, search.length() - 1);

            if (searchAlt.equals(temp) || search.equals(temp)) {
                found = temp;
                break;
            }
        }

        if (found.equals("")) {
            seqQueue.remove(0);
            try {
                dir = seqQueue.get(0);
            }
            catch (IndexOutOfBoundsException e) {
                System.out.println("No Seqeuences Queued");
                System.exit(0);
            }

            System.out.println("new dir: " + dir);
            searchDir(dir);
            
            System.out.println(currentImg);
        }
        else {
            String id = dir.substring(0,1).toUpperCase();
            currentImg = id + found + ".png";
            System.out.println(currentImg);
        }

        return seqQueue;
    }

    public void changeImage(String filename) {
        img.setIcon(new ImageIcon(filename));
    }
}