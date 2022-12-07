import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

public class Execute {
    // ArrayList to store next sequences.
    private static ArrayList<String> seqQueue;
    public static void main(String[] args) throws InterruptedException {
        // Create arraylist to store sequences
        seqQueue = new ArrayList<String>();
        // Define directory of soundsensor sequence
        String soundSeqDir = "sound";
        // Create initial queue state
        seqQueue.add("wakeup");
        seqQueue.add("check");
        seqQueue.add("idle");

        // Create window and set to fullscreen
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        // String var to store filename of currently displayed image
        Window window = new Window(seqQueue.get(0));
        screen.setFullScreenWindow(window);


        // Create ButtonManager and supporting variables
        ButtonManager buttons = new ButtonManager();
        boolean aState;
        boolean bState;
        boolean soundState;


        while(true) {
            // Update status variables
            aState = buttons.getA();
            bState = buttons.getB();
            soundState = buttons.getSound();

            // Add sound sensor sequence to queue, if it has been triggered and is not already queued.
            if (soundState && !SeqSearch(soundSeqDir)) {
                seqQueue.add(1, soundSeqDir);
            }

            // Update displayed image, if either button has been pressed
            if (aState || bState) {
                seqQueue = window.nextImg(aState, seqQueue);
            }

            Thread.sleep(100);
        }
    }

    // Search queue for given sequence
    public static boolean SeqSearch(String dir) {
        boolean found = false;
        for (int i = 0; i < seqQueue.size(); i++) {
            if (seqQueue.get(i).equals(dir)) {
                found = true;
                break;
            }
        }

        return found;
    }
}
