package PaooGame.Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class LevelBackground {
    protected BufferedImage image;
    protected int x = 0;

    // Constructorul gol (nu încarcă imaginea)
    public LevelBackground() {
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

    public BufferedImage getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }
}
