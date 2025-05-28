package PaooGame.Object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
/**
 * Clasa Castel1 este responsabilă pentru afișarea castelelor pe ecran
 */


public class Castle1 {
    int x; // Poziția pe axa X a castelului
    int y; // Poziția pe axa Y a castelului
    int height; // Înălțimea la care se va desena castelul
    int width; // Lățimea la care se va desena castelul
    private BufferedImage castle_image;

    /// Constructorul clasei Castle1.
    public Castle1(int x, int y,int height, int width, String name){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        try { castle_image =  ImageIO.read(getClass().getResource(name)); // Încarcă imaginea castelului din resurse folosind calea specificată
        }catch (IOException e) {  e.printStackTrace(); } // Dacă încărcarea imaginii eșuează, afișează detalii despre excepție pentru depanare
    }

    /// Metoda care desenează castelul pe ecran.
    public void draw(Graphics g, int backgroundX) {
        int screenX = x + backgroundX; // Calculăm poziția pe ecran ținând cont de deplasarea fundalului
        g.drawImage(castle_image, screenX, y,width,height, null); // Desenăm imaginea castelului la poziția calculată, cu dimensiunile date
    }

}
