package PaooGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Level1Background {
    private BufferedImage image;
    private int x = 0;

    public Level1Background() {
        try {
            image = ImageIO.read(getClass().getResource("/background1.png"));
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
}
