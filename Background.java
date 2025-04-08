import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class Background extends JPanel {
    private Image backgroundImage;

    // Constructor to load the image
    public Background() {
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\Guest-PC\\IdeaProjects\\Reaction_type\\src\\flappybirdbg.png"));
        } catch (IOException e) {
            e.printStackTrace();  // Handle exception if image loading fails
        }
    }

    // Override paintComponent to draw the image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Ensure the panel is painted first
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Scale the image to panel size
        }
    }
}
