package PaooGame;

import PaooGame.Entity.Enemylvl5;
import PaooGame.Entity.Player;
import PaooGame.Graphics.Level2Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TrapObject {
    int x;
    int y;
    int width;
    int height;
    double damage = 0.001;
    private BufferedImage image;

    public TrapObject(int x, int y, int width, int height,String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try {
            image =  ImageIO.read(getClass().getResource(name));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean areEntitiesColliding(Player p, int absoluteX) {
        int feetHeight = 15; // înălțimea zonei pentru picioare (poți ajusta după nevoie)
        Rectangle rectFeet = new Rectangle(absoluteX+10, p.y + p.height - feetHeight, p.width-20, feetHeight);
        Rectangle rectE = new Rectangle(this.x+20, this.y-10, this.width-40, this.height+20);
        return rectFeet.intersects(rectE);
    }
    public int getX(){
        return this.x;
    }
    public int gety(){
        return this.x;
    }
    public double getDamage(){
        return this.damage;
    }
    public void draw(Graphics g,int backgroundX) {
        int screenX = x + backgroundX;
        g.drawImage(image, screenX, y,width,height, null);
    }
}
