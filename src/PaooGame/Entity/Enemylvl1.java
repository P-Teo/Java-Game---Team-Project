package PaooGame.Entity;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class Enemylvl1  extends Entity{
    boolean isAttacking = false;


    public Enemylvl1(){
        x = 700;
        y = 300;
        speed = 2;
        width = 150;
        height = 120;
        health = 100;
        getPlayerImage();
        direction = "left";
        frame = 0;
        attackFrame = 0;
        isMoving = false;
        getPlayerImage();
    }
    public void getPlayerImage(){
        try {
            for( int i =0;i<12;i++){
                leftRunning[i] = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_run_left_" + i + ".png"));
                rightRunning[i] = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_run_right_" + i + ".png"));
                leftAttack[i] = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_attack_left_" + i + ".png"));
                rightAttack[i] = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_attack_right_" + i + ".png"));

            }

            idle_right = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_idle_right.png"));
            idle_left = ImageIO.read(getClass().getResource("/Characters/Enemy1/Enemy1_idle_left.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public int getHealth() {
        return health;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
    }
/*

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
    }*/
    public void update(int xPlayer, int yPlayer, int screenWidth, int screenHeight){
        int distanceX =this.x - xPlayer;
        int distanceY = this.y - yPlayer;
        if(Math.abs(distanceX)>20){
            if(distanceX>0&& x + width < screenWidth - 50){
                direction = "left";
                x -= speed;
             }
            else {
                direction = "right";
                x += speed;
            }
            isMoving = true;
        }
        if(Math.abs(distanceY)>20){
            if(distanceY>0 && y + height < screenHeight - 80){
                y -= speed;

        }
            else {
                y += speed;
        }
            isMoving = true;
        }
        if(Math.abs(distanceX)<50&&Math.abs(distanceY)<50) {
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





    }
}

