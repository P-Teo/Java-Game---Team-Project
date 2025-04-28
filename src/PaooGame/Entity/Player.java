package PaooGame.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Player extends Entity{
    boolean isAttacking = false;


    public Player(){
        x = 200;
        y = 300;
        speed = 2;
        width = 200;
        height = 120;
        direction = "right";
        frame = 0;
        attackFrame = 0;
        isMoving = false;
        getPlayerImage();
    }
    public void getPlayerImage(){
        try {
            for( int i =0;i<10;i++){
                leftRunning[i] = ImageIO.read(getClass().getResource("/PlayerSprite/Player_run_left_" + i + ".png"));
                rightRunning[i] = ImageIO.read(getClass().getResource("/PlayerSprite/Player_run_right_" + i + ".png"));
                leftAttack[i] = ImageIO.read(getClass().getResource("/PlayerSprite/Player_attack_left_" + i + ".png"));
                rightAttack[i] = ImageIO.read(getClass().getResource("/PlayerSprite/Player_attack_right_" + i + ".png"));
                leftJump[i] = ImageIO.read(getClass().getResource("/PlayerSprite/Player_jump_left_" + i + ".png"));
                rightJump[i] = ImageIO.read(getClass().getResource("/PlayerSprite/Player_jump_right_" + i + ".png"));
            }

            idle_right = ImageIO.read(getClass().getResource("/PlayerSprite/Player_idle_right.png"));
            idle_left = ImageIO.read(getClass().getResource("/PlayerSprite/Player_idle_left.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void update(boolean moveLeft, boolean moveRight, boolean attackKey, int screenWidth) {
        isMoving = false;
        isAttacking = false;
        if (moveRight && x + width < screenWidth - 50) {
            direction = "right";
            x += speed;
        } else if (moveLeft && x > 50) {
            direction = "left";
            x -= speed;
        }
        if(moveRight||moveLeft)
            isMoving = true;
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
    }
}
