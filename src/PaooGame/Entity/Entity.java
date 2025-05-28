package PaooGame.Entity;

import java.awt.image.BufferedImage;

/**
 * Clasa de bază pentru toate entitățile jocului (jucători, inamici etc.).
 * Reține atributele comune pentru poziție, animații, stare etc.
 */

public class Entity {
    public int x, y; // Coordonatele entității pe ecran sau în lume
    public int speed; // Viteza de mișcare a entității
    public int width, height; // Dimensiunile entității (lățime și înălțime)
    public int health;  // Sănătatea entității
    public double damage;  // Daunele produse de entitate în timpul unui atac
    public BufferedImage fullHeart, halfHeart,emptyHeart;   // Imagini pentru afișarea stării de viață (inimă plină, jumătate, goală)
    public BufferedImage []leftRunning = new BufferedImage[12];   // Cadre pentru animația de alergare spre stânga (12 imagini)
    public BufferedImage []rightRunning = new BufferedImage[12];  // Cadre pentru animația de alergare spre dreapta (12 imagini)
    public BufferedImage []leftAttack= new BufferedImage[12];  // Cadre pentru animația de atac spre stânga (12 imagini)
    public BufferedImage []rightAttack = new BufferedImage[12];   // Cadre pentru animația de atac spre dreapta (12 imagini)
    public BufferedImage []leftDie= new BufferedImage[15];  // Cadre pentru animația de moarte spre stânga (15 imagini)
    public BufferedImage []rightDie = new BufferedImage[15]; // Cadre pentru animația de moarte spre dreapta (15 imagini)
    public BufferedImage idle_left, idle_right;  // Imagine statică pentru starea de repaus cu fața la stânga și la dreapta
    public String direction;  // Direcția actuală a entității
    boolean isMoving;  // Indicator dacă entitatea este în mișcare
    boolean isDying;  // Indicator dacă entitatea este în procesul de moarte
    public boolean isDead;  // Indicator dacă entitatea este complet moartă
    int frame;  // Cadru curent pentru animațiile de mișcare sau stare
    int attackFrame;  // Cadru curent pentru animația de atac
    int dieFrame;  // Cadru curent pentru animația de moarte
}
