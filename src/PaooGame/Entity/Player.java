package PaooGame.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Player extends Entity{

    private static BufferedImage[] leftRunning = new BufferedImage[10];
    private static BufferedImage[] rightRunning = new BufferedImage[10];
    private static BufferedImage[] leftAttack = new BufferedImage[10];
    private static BufferedImage[] rightAttack = new BufferedImage[10];
    private static BufferedImage[] leftJump = new BufferedImage[10];
    private static BufferedImage[] rightJump = new BufferedImage[10];
    private static BufferedImage idle_right;
    private static BufferedImage idle_left;

    private static BufferedImage fullHeart;
    private static BufferedImage halfHeart;
    private static BufferedImage emptyHeart;

    private static boolean imagesLoaded = false;

    boolean isAttacking = false;


    public Player(){
        x = 200;
        y = 300;
        speed = 2;
        width = 200;
        height = 120;
        health = 100;
        damage = 3.5;
        direction = "right";
        frame = 0;
        attackFrame = 0;
        isMoving = false;
        if (!imagesLoaded) {
            getPlayerImage();
            loadHeartImages();
            imagesLoaded = true;
        }
    }
    public void getPlayerImage(){
        try {
            for( int i =0;i<10;i++){
                leftRunning[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_run_left_" + i + ".png"));
                rightRunning[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_run_right_" + i + ".png"));
                leftAttack[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_attack_left_" + i + ".png"));
                rightAttack[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_attack_right_" + i + ".png"));
                leftJump[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_jump_left_" + i + ".png"));
                rightJump[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_jump_right_" + i + ".png"));
            }

            idle_right = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_idle_right.png"));
            idle_left = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_idle_left.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadHeartImages() {
        try {
            fullHeart = ImageIO.read(getClass().getResource("/heart/Full.png"));
            halfHeart = ImageIO.read(getClass().getResource("/heart/Half.png"));
            emptyHeart = ImageIO.read(getClass().getResource("/heart/Empty.png"));
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

    public void heal(int amount) {
        health += amount;
        if (health > 100) health = 100;
    }
    public boolean getIsAttacking()
    {
        return isAttacking;
    }
    public void update(boolean moveLeft, boolean moveRight,boolean moveUp, boolean moveDown, boolean attackKey, int screenWidth,int screenHeight) {
        isMoving = false;
        isAttacking = false;
        if (moveRight && x + width < screenWidth - 50) {
            direction = "right";
            x += speed;
        } else if (moveLeft && x > 50) {
            direction = "left";
            x -= speed;
        }
        if(moveRight||moveLeft || moveUp || moveDown)
            isMoving = true;

        if (moveUp && y > 140) {
            y -= 3;
        } else if (moveDown && y + height < screenHeight - 80) {
            y += 3;
        }

        if (attackKey ) {
            isAttacking = true;
        }
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



        //pentru desenare inimi
        int heartX = 20;
        int heartY = 20;
        int heartWidth = 40;
        int heartHeight = 40;
        int maxHearts = 3;
        int hpPerHeart = 100 / maxHearts; // 33 (cu rest)

        // Variabilă temporară pentru a calcula viața rămasă pentru fiecare inimă
        int remainingHealth =health;

        for (int i = 0; i < maxHearts; i++) {
            if (remainingHealth >= hpPerHeart) {
                g.drawImage(fullHeart, heartX + i * (heartWidth + 10), heartY, heartWidth, heartHeight, null);
            } else if (remainingHealth >= hpPerHeart / 2) {
                g.drawImage(halfHeart, heartX + i * (heartWidth + 10), heartY, heartWidth, heartHeight, null);
            } else {
                g.drawImage(emptyHeart, heartX + i * (heartWidth + 10), heartY, heartWidth, heartHeight, null);
            }
            remainingHealth -= hpPerHeart;
        }
    }
}
