package PaooGame.Object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

/**
 * Clasa StarFinal este folosita la nivelul 5 unde prințesa trebuie să colecționeze aceste stele
 */

public class StarFinal {
    private int x, y; // Coordonatele stelei
    private int width = 20; // Lățimea stelei
    private int height = 20; // Înălțimea stelei
    private boolean collected = false; // Flag care indică dacă steaua a fost colectată

    /// Constructor pentru obiectul StarFinal
    public StarFinal(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /// Desenează steaua pe ecran doar dacă nu a fost colectată.
    public void draw(Graphics g) {
        if (!collected) {
            g.setColor(Color.YELLOW);  // Setează culoarea galbenă pentru stea
            g.fillOval(x, y, width, height); // Desenează o elipsă (cerc) care reprezintă steaua
        }
    }

    /// Returnează un dreptunghi ce reprezintă zona de coliziune a stelei.
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /// Marchează steaua ca fiind colectată.
    public void collect() {
        collected = true;
    }

    /// Verifică dacă steaua a fost colectată.
    public boolean isCollected() {
        return collected;
    }

    public int getX() { return x;  }    ///returnează x-ul obiectului

    public int getY() {  return y; }    ///returnează y-ul obiectului


}
