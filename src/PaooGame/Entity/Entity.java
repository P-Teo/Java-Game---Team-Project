package PaooGame.Entity;

import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;
    public int width, height;
    public BufferedImage []leftRunning = new BufferedImage[10];
    public BufferedImage []rightRunning = new BufferedImage[10];
    public BufferedImage []leftAttack= new BufferedImage[10];
    public BufferedImage []rightAttack = new BufferedImage[10];
    public BufferedImage []leftJump= new BufferedImage[10];
    public BufferedImage []rightJump = new BufferedImage[10];
    public BufferedImage idle_left, idle_right;
    public String direction;
    boolean isMoving;
    int frame;
    int attackFrame;
}
