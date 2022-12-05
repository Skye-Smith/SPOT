import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.ArrayList;

public class Window extends JFrame {
    private JLabel img = new JLabel();

    public Window(String filename) {
        img.setIcon(new ImageIcon(filename));

        setUndecorated(true);
        
        add(img);
    }

    public String nextImg(String current, boolean aState, ArrayList<String> seqQueue) {
        return "";
    }

    public void changeImage(String filename) {
        img.setIcon(new ImageIcon(filename));
    }
}