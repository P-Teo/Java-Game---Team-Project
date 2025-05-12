package PaooGame.Entity;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class Enemylvl4  extends Entity{
    private static BufferedImage[] sharedLeftRunning = new BufferedImage[15];
    private static BufferedImage[] sharedRightRunning = new BufferedImage[15];
    private static BufferedImage[] sharedLeftAttack = new BufferedImage[15];
    private static BufferedImage[] sharedRightAttack = new BufferedImage[15];
    private static BufferedImage[] sharedLeftDie = new BufferedImage[15];
    private static BufferedImage[] sharedRightDie = new BufferedImage[15];
    private static BufferedImage sharedIdleRight;
    private static BufferedImage sharedIdleLeft;
    private static boolean imagesLoaded = false;

    boolean isAttacking = false;


    public Enemylvl4() {
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
        isDying = false;
        isDead = false;
        dieFrame = 0;
        if (!imagesLoaded) {
            loadSharedImages(); // încarcă o singură dată imaginile statice
        }

        // Folosește imaginile partajate
        leftRunning = sharedLeftRunning;
        rightRunning = sharedRightRunning;
        leftAttack = sharedLeftAttack;
        rightAttack = sharedRightAttack;
        rightDie = sharedRightDie;
        leftDie = sharedLeftDie;
        idle_right = sharedIdleRight;
        idle_left = sharedIdleLeft;
    }

    private void loadSharedImages() {
        //System.out.println("111111111");
        try {
            for (int i = 0; i < 12; i++) {
                sharedLeftRunning[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Enemy4_run_left_" + i + ".png"));
                sharedRightRunning[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Enemy4_run_right_" + i + ".png"));
                sharedLeftAttack[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Enemy4_attack_left_" + i + ".png"));
                sharedRightAttack[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Enemy4_attack_right_" + i + ".png"));
                sharedLeftDie[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Enemy4_die_left_" + i + ".png"));
                sharedRightDie[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Enemy4_die_right_" + i + ".png"));

            }
            sharedIdleRight = ImageIO.read(getClass().getResource("/Characters/Enemy3/Enemy3_idle_right.png"));
            sharedIdleLeft = ImageIO.read(getClass().getResource("/Characters/Enemy3/Enemy3_idle_left.png"));

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
        System.out.println("damage!!" + health);
        if (health <= 0 && !isDying) {
            health = 0;
            isDying =true;
        }
    }
    public boolean getIsAttacking()
    {
        return isAttacking;
    }

    public void update(int xPlayer, int yPlayer, int screenWidth, int screenHeight) {
        if(isDead){
            return;
        }
        if(isDying){
            System.out.println("is dying!!!");
            dieFrame++;
            if (dieFrame >= 12) {
                dieFrame = 11;
                isDying = false;
                isDead = true;
            }
            return;
        }

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
        if(isDying){
            if(direction.equals("right")){
                image = rightDie[dieFrame];
            } else if(direction.equals("left")){
                image =leftDie[dieFrame];
            }
        }
        else if (isAttacking) {
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

