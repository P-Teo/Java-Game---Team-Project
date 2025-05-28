package PaooGame.Object;


import PaooGame.Entity.Player;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Clasa TrapObject este folosita la nivelurile 2-4 pentru a reprezenta pietrele de pe drumul prințesei
 */

public class TrapObject {
    int x; // Poziția pe axa X a capcanei
    int y; // Poziția pe axa Y a capcanei
    int width; // Lățimea capcanei
    int height; // Înălțimea capcanei
    double damage = 0.001; // Cantitatea de damage pe care o aplică capcana
    private BufferedImage image; // Imaginea folosită pentru afișarea capcanei

    /// Constructor pentru obiectul TrapObject
    public TrapObject(int x, int y, int width, int height,String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try { image =  ImageIO.read(getClass().getResource(name)); }catch (IOException e) { e.printStackTrace(); }
    }

    /// Verifică dacă picioarele jucătorului (Player) sunt în coliziune cu capcana.
    public boolean areEntitiesColliding(Player p, int absoluteX) {
        int feetHeight = 15; // înălțimea zonei pentru picioare
        Rectangle rectFeet = new Rectangle(absoluteX+10, p.y + p.height - feetHeight, p.width-20, feetHeight);
        Rectangle rectE = new Rectangle(this.x+20, this.y-10, this.width-40, this.height+20);
        return rectFeet.intersects(rectE);
    }

    public int getX(){
        return this.x;
    } ///Returnează poziția pe axa X a capcanei.
    public int gety(){
        return this.x;
    } ///Returnează poziția pe axa Y a capcanei.
    public double getDamage(){
        return this.damage;
    } ///Returnează cantitatea de damage pe care o aplică capcana.
    /// // Calculează poziția pe ecran în funcție de scroll
    public void draw(Graphics g,int backgroundX) { int screenX = x + backgroundX; g.drawImage(image, screenX, y,width,height, null); }
}
