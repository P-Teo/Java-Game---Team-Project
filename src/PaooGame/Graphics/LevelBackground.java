package PaooGame.Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clasa abstractă LevelBackground definește comportamentul comun
 * pentru fundalurile nivelelor din joc.
 * Aceasta oferă funcționalitate pentru deplasarea și desenarea fundalului.
 */

public abstract class LevelBackground {
    protected BufferedImage image; // Imaginea care reprezintă fundalul nivelului
    protected int x = 0;  // Coordonata X a imaginii pe ecran, folosită pentru efectul de scroll

    /// Constructorul gol (nu încarcă imaginea)
    public LevelBackground() {
    }

    /// Actualizează poziția fundalului în funcție de direcția de mișcare
    public void update(boolean moveLeft, boolean moveRight, int screenWidth) {
        if (moveRight && x > -(image.getWidth() - screenWidth)) {  x -= 7;
        } else if (moveLeft && x < 0) {  x += 7;  }
    }

    /// Desenează fundalul pe ecran la poziția actuală X
    public void draw(Graphics g) {
        g.drawImage(image, x, 0, null);
    }

    /// Getter pentru imaginea fundalului
    public BufferedImage getImage() {
        return image;
    }

    /// Getter pentru poziția X a imaginii
    public int getX() {
        return x;
    }

    /// Returnează lățimea imaginii fundalului
    public int getWidth() {
        return image.getWidth();
    }

    /// Returnează înălțimea imaginii fundalului
    public int getHeight() {
        return image.getHeight();
    }
}
