package PaooGame.Entity;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class Enemylvl1  extends Entity{
    private static BufferedImage[] sharedLeftRunning = new BufferedImage[12];
    private static BufferedImage[] sharedRightRunning = new BufferedImage[12];
    private static BufferedImage[] sharedLeftAttack = new BufferedImage[12];
    private static BufferedImage[] sharedRightAttack = new BufferedImage[12];
    private static BufferedImage sharedIdleRight;
    private static BufferedImage sharedIdleLeft;
    private static boolean imagesLoaded = false;

    boolean isAttacking = false;


    public Enemylvl1() {
        x = 700;
        y = 300;
        speed = 6;
        width = 150;
        height = 120;
        health = 10;
        damage = 0.002;
        direction = "left";
        frame = 0;
        attackFrame = 0;
        isMoving = false;

        if (!imagesLoaded) {
            loadSharedImages(); // încarcă o singură dată imaginile statice
        }

        // Folosește imaginile partajate
        leftRunning = sharedLeftRunning;
        rightRunning = sharedRightRunning;
        leftAttack = sharedLeftAttack;
        rightAttack = sharedRightAttack;
        idle_right = sharedIdleRight;
        idle_left = sharedIdleLeft;
    }

    private void loadSharedImages() {
        //System.out.println("111111111");
        try {
            for (int i = 0; i < 12; i++) {
                sharedLeftRunning[i] = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_run_left_" + i + ".png"));
                sharedRightRunning[i] = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_run_right_" + i + ".png"));
                sharedLeftAttack[i] = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_attack_left_" + i + ".png"));
                sharedRightAttack[i] = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_attack_right_" + i + ".png"));
            }
            sharedIdleRight = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_idle_right.png"));
            sharedIdleLeft = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_idle_left.png"));

            imagesLoaded = true;
            System.out.println("Imaginile Enemy1 au fost încărcate o singură dată.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getHealth() {
        return health;
    }

    public void takeDamage(double amount) {
        health -= amount;
        if (health < 0) health = 0;
    }
    public boolean getIsAttacking()
    {
        return isAttacking;
    }

    public void update(int xPlayer, int yPlayer, int screenWidth, int screenHeight) {
        int distanceX = this.x - xPlayer;
        int distanceY = this.y - yPlayer;

        if (Math.abs(distanceX) > 20) {
            if (distanceX > 0) {
                direction = "left";
                x -= speed;
            } else {
                direction = "right";
                x += speed;
            }
            isMoving = true;
        }

        if (Math.abs(distanceY) > 20) {
            if (distanceY > 0) {
                y -= speed;
            } else {
                y += speed;
            }
            isMoving = true;
        }

        isAttacking = Math.abs(distanceX) < 50 && Math.abs(distanceY) < 50;

        if (isAttacking) {
            attackFrame++;
            if (attackFrame >= 10) {
                attackFrame = 0;
            }
        } else if (isMoving) {
            frame++;
            if (frame >= 10) {
                frame = 0;
            }
        }
    }

    public void draw(Graphics g) {
        BufferedImage image = null;
        if (isAttacking) {
            if (direction.equals("right")) {
                image = rightAttack[attackFrame];
            } else if (direction.equals("left")) {
                image = leftAttack[attackFrame];
            }
        } else if(isMoving){
            if(direction.equals("right")){
                image = rightRunning[frame];
            } else if(direction.equals("left")){
                image =leftRunning[frame];
            }
        }
        else{
            if(direction.equals("right")){
                image = idle_right;
            } else if(direction.equals("left")){
                image = idle_left;
            }
        }
        g.drawImage(image,x,y,width,height,null);





    }
}

