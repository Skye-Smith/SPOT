import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Window extends JFrame {
    private JLabel img = new JLabel();

    public Window() {
        img.setIcon(new ImageIcon("back.jpg"));

        setUndecorated(true);
        
        add(img);
    }

    public void changeImage(String filename) {
        img.setIcon(new ImageIcon(filename));
    }
}