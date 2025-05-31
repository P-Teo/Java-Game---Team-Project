package PaooGame.Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
/**
 * Clasa abstractă LevelBackground definește comportamentul comun
 * pentru fundalurile nivelelor din joc.
 * Aceasta oferă funcționalitate pentru deplasarea și desenarea fundalului.
 */

public class LevelBackground {
    protected BufferedImage image; // Imaginea care reprezintă fundalul nivelului
    protected int x = 0;  // Coordonata X a imaginii pe ecran, folosită pentru efectul de scroll

    /// Constructorul
    public LevelBackground(int levelNumber) {
        try {
            // Creează calea spre imagine dinamic
            String path = String.format("/Background/Backgroundlevel%d.png", levelNumber);
            image = ImageIO.read(getClass().getResource(path));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("Nu s-a putut încărca fundalul pentru nivelul " + levelNumber);
        }
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
