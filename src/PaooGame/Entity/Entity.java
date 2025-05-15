package PaooGame.Entity;

import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;
    public int width, height;
    public int health;
    public double damage;
    public BufferedImage fullHeart, halfHeart,emptyHeart;
    public BufferedImage []leftRunning = new BufferedImage[12];
    public BufferedImage []rightRunning = new BufferedImage[12];
    public BufferedImage []leftAttack= new BufferedImage[12];
    public BufferedImage []rightAttack = new BufferedImage[12];
    public BufferedImage []leftDie= new BufferedImage[15];
    public BufferedImage []rightDie = new BufferedImage[15];
    public BufferedImage idle_left, idle_right;
    public String direction;
    boolean isMoving;
    boolean isDying;
    public boolean isDead;
    int frame;
    int attackFrame;
    int dieFrame;

}
