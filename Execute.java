import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

public class Execute {
    // ArrayList to store next sequences.
    private static ArrayList<String> seqQueue;
    public static void main(String[] args) {
        // Create window and set to fullscreen
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        // String var to store filename of currently displayed image
        String currentSeq = "";
        String currentImg = "";
        Window window = new Window(currentImg);
        screen.setFullScreenWindow(window);

        // Create arraylist to store sequences
        seqQueue = new ArrayList<String>();
        // Sequence Start definitions
        String soundSeqStart = "";
        String pulseSeqStart = "";
        // Create initial queue state

        // Create Pulse and supporting variables
        
        int bpmWarn = 110;
        int bpm = 0;

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
            //bpm = pulse.getBPM();

            // Add sound sensor sequence to queue, if it is not already queued.
            if (soundState && !SeqSearch(soundSeqStart)) {
                seqQueue.add(soundSeqStart);
            }

            // Add heartbeat sequence to queue, if it is not already queued
            if (bpm >= bpmWarn && !SeqSearch(pulseSeqStart)) {
                seqQueue.add(pulseSeqStart);
            }

            // Update displayed image if button has been pressed
            if (aState || bState) {
                currentSeq = currentImg.substring(0, 1);
                currentImg = window.nextImg(currentImg, aState, seqQueue);
            }
        }
    }

    // Search queue for given sequence
    public static boolean SeqSearch(String sequence) {
        boolean found = false;
        for (int i = 0; i < seqQueue.size(); i++) {
            if (seqQueue.get(i).equals(sequence)) {
                found = true;
                break;
            }
        }

        return found;
    }
}