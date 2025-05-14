package PaooGame.Castle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Castle1 {
    int x;
    int y;
    int height;
    int width;
    private BufferedImage castle_image;
    public Castle1(int x, int y,int height, int width, String name){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        try {
        castle_image =  ImageIO.read(getClass().getResource(name));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics g, int backgroundX) {
        int screenX = x + backgroundX;
        g.drawImage(castle_image, screenX, y,width,height, null);
    }
}
