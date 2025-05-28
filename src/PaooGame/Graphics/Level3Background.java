package PaooGame.Graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Clasa Level1Background extinde LevelBackground și reprezintă
 * fundalul specific pentru nivelul 3 din joc.
 */

public class Level3Background extends LevelBackground{
    private BufferedImage image; // Imaginea de fundal pentru nivelul 3
    private int x = 0; // Coordonata X a fundalului – folosită pentru efectul de scroll

    ///Constructorul clasei. Încarcă imaginea de fundal pentru nivelul 3
    public Level3Background() {
        try { image = ImageIO.read(getClass().getResource("/Background/Backgroundlevel3.png"));
        } catch (IOException e) { e.printStackTrace(); }
    }

    ///Actualizează poziția fundalului în funcție de direcția de mișcare
    public void update(boolean moveLeft, boolean moveRight, int screenWidth) {
        if (moveRight && x > -(image.getWidth() - screenWidth)) { x -= 7;
        } else if (moveLeft && x < 0) { x += 7; }
    }

    /// Desenează imaginea de fundal pe ecran la poziția actuală X.
    public void draw(Graphics g) {  g.drawImage(image, x, 0, null); }

    /// Returnează imaginea fundalului
    @Override
    public BufferedImage getImage() {
        return image;
    }

    /// Returnează poziția X a imaginii
    public int getX() {
        return x;
    }

    /// Returnează lățimea imaginii de fundal
    public int getWidth() {
        return image.getWidth();
    }

    /// Returnează înălțimea imaginii de fundal
    public int getHeight() {
        return image.getHeight();
    }

}
