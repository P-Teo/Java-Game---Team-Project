package PaooGame.Graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Level4Background extends LevelBackground{
    private BufferedImage image;
    private int x = 0;


    public Level4Background() {
        try {
            image = ImageIO.read(getClass().getResource("/Background/Backgroundlevel4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(boolean moveLeft, boolean moveRight, int screenWidth) {
        if (moveRight && x > -(image.getWidth() - screenWidth)) {
            x -= 7;
        } else if (moveLeft && x < 0) {
            x += 7;
        }
    }

    public void draw(Graphics g) {

        g.drawImage(image, x, 0, null);

    }

    @Override
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
