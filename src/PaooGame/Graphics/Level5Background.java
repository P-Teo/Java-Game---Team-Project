package PaooGame.Graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Level5Background {
    private BufferedImage image;
    private int x = 0;


    public Level5Background() {
        try {
            image = ImageIO.read(getClass().getResource("/Background/Backgroundlevel5.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void draw(Graphics g) {

        g.drawImage(image, 0, 0, null);

    }

    public BufferedImage getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    // Returnează lățimea imaginii de fundal
    public int getWidth() {
        return image.getWidth();
    }

    // Returnează înălțimea imaginii de fundal
    public int getHeight() {
        return image.getHeight();
    }

}
