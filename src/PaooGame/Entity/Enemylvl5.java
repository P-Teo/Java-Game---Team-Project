package PaooGame.Entity;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class Enemylvl5 extends Entity{
    private static BufferedImage[] sharedLeftRunning = new BufferedImage[12];
    private static BufferedImage[] sharedRightRunning = new BufferedImage[12];
    private static BufferedImage[] sharedLeftAttack = new BufferedImage[12];
    private static BufferedImage[] sharedRightAttack = new BufferedImage[12];
    private static BufferedImage[] sharedLeftDie = new BufferedImage[15];
    private static BufferedImage[] sharedRightDie = new BufferedImage[15];
    private static BufferedImage sharedIdleRight;
    private static BufferedImage sharedIdleLeft;
    private static boolean imagesLoaded = false;

    boolean isAttacking = false;


    public Enemylvl5() {
        x = 700;
        y = 300;
        speed = 4;
        width = 360;
        height = 200;
        health = 300;
        damage = 0.5;
        direction = "left";
        frame = 0;
        attackFrame = 0;
        isMoving = false;
        isDying = false;
        isDead = false;
        dieFrame = 0;
        if (!imagesLoaded) {
            loadSharedImages(); // încarcă o singură dată imaginile statice
            loadHeartImages();
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

            for (int i = 0; i < 10; i++) {
                sharedLeftRunning[i] = ImageIO.read(getClass().getResource("/Characters/Enemy5/King/King_run_left_" + i + ".png"));
                sharedRightRunning[i] = ImageIO.read(getClass().getResource("/Characters/Enemy5/King/King_run_right_" + i + ".png"));
                sharedLeftAttack[i] = ImageIO.read(getClass().getResource("/Characters/Enemy5/King/King_attack_left_" + i + ".png"));
                sharedRightAttack[i] = ImageIO.read(getClass().getResource("/Characters/Enemy5/King/King_attack_right_" + i + ".png"));
                sharedLeftDie[i] = ImageIO.read(getClass().getResource("/Characters/Enemy5/King/King_die_left_" + i + ".png"));
                sharedRightDie[i] = ImageIO.read(getClass().getResource("/Characters/Enemy5/King/King_die_right_" + i + ".png"));

            }
            sharedIdleRight = ImageIO.read(getClass().getResource("/Characters/Enemy5/King/King_idle_right.png"));
            sharedIdleLeft = ImageIO.read(getClass().getResource("/Characters/Enemy5/King/King_idle_left.png"));

            imagesLoaded = true;
            System.out.println("Imaginile Enemy1 au fost încărcate o singură dată.");
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
        System.out.println("damage_enemy" + health);
        if (health <= 0) {
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
            if (dieFrame >= 10) {
                dieFrame = 9;
                isDying = false;
                isDead = true;
            }
            return;
        }
        // calculam distanta dintre centrul axelor celor 2 caractere din cauza diferentei de marime

        int centerXEnemy = this.x + this.width / 2;
        int centerYEnemy = this.y + this.height / 2;
        int centerXPlayer = xPlayer + 200 / 2;
        int centerYPlayer = yPlayer + 140 / 2;

        int distanceX = centerXEnemy - centerXPlayer;
        int distanceY = centerYEnemy - centerYPlayer ;

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


        // === Desenare Health Bar ===
        int barWidth = 100;
        int barHeight = 10;
        int barX = x + width / 2 - barWidth / 2;  // centrat deasupra inamicului
        int barY = y - 20;  // puțin deasupra

        // Calculează procentajul de viață
        double healthPercent = (double) health / 300;
        int currentBarWidth = (int) (barWidth * healthPercent);

        // Fundalul barei (gri)
        g.setColor(Color.GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        // Viața actuală (verde)
        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, currentBarWidth, barHeight);

        // Contur negru
        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);



    }
}

