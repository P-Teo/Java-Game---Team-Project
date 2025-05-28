package PaooGame.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Rectangle;

/// Clasa pentru palyer, moștenește clasa de bază Entity

public class Player extends Entity{
    // Array-uri statice pentru animații
    private static BufferedImage[] leftRunning = new BufferedImage[10];
    private static BufferedImage[] rightRunning = new BufferedImage[10];
    private static BufferedImage[] leftAttack = new BufferedImage[10];
    private static BufferedImage[] rightAttack = new BufferedImage[10];
    private static BufferedImage[] leftDie = new BufferedImage[10];
    private static BufferedImage[] rightDie = new BufferedImage[10];
    private static BufferedImage idle_right;
    private static BufferedImage idle_left;
    // Inimile care indică starea de sănătate
    private static BufferedImage fullHeart;
    private static BufferedImage halfHeart;
    private static BufferedImage emptyHeart;
    // Indicator pentru a evita încărcarea multiplă
    private static boolean imagesLoaded = false;
    // Stări și atribute ale jucătorului
    boolean isAttacking = false; // Stare de atac a inamicului
    private int deathFrameDelay = 0;
    private final int maxDeathFrameDelay = 3;
    private int maxHealth;

    /// Constructorul jucătorului
    public Player(){
        x = 200;
        y = 300;
        speed = 5;
        width = 200;
        height = 120;
        health = 100;
        maxHealth=health;
        damage = 6;
        direction = "right";
        frame = 0;
        dieFrame = 0;
        attackFrame = 0;
        isMoving = false;
        isDying = false;
        // Încarcă imaginile doar o singură dată
        if (!imagesLoaded) {
            getPlayerImage();
            loadHeartImages();
            imagesLoaded = true;
        }
    }

    /// Încarcă animațiile jucătorului
    public void getPlayerImage(){
        try {
            for( int i =0;i<10;i++){
                leftRunning[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_run_left_" + i + ".png"));
                rightRunning[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_run_right_" + i + ".png"));
                leftAttack[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_attack_left_" + i + ".png"));
                rightAttack[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_attack_right_" + i + ".png"));
                leftDie[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_die_left_" + i + ".png"));
                rightDie[i] = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_die_right_" + i + ".png"));
            }
            idle_right = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_idle_right.png"));
            idle_left = ImageIO.read(getClass().getResource("/Characters/PlayerSprite/Player_idle_left.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /// Încarcă imaginile pentru inimile de viață
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
    } ///returnează viața
    public int getX() {
        return x;
    } ///returnează poziția x
    public int getY() {
        return y;
    } ///returnează poziția y
    public int getHeight() {
        return height;
    } ///returnează înălțimea jucătorului
    public int getWidth() {
        return width;
    } ///returnează lățimea jucătorului
    public void setHealth(int newHealth){ health= newHealth; maxHealth=newHealth;} ///setează viața

    /// Scade sănătatea jucătorului și pornește animația de moarte dacă este cazul
    public void takeDamage(double amount) {
        health -= amount;
        if (health <= 0&& !isDying) {
            health = 0;
            isDying = true;
            dieFrame = 0;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    } /// Returnează hitbox-ul jucătorului
    public void heal(int amount) { health += amount; if (health > 100) health = 100;} /// Vindecă jucătorul
    public boolean getIsAttacking()
    {
        return isAttacking;
    } //returnează atacul

    /// Metoda principală de actualizare a jucătorului
    public void update(boolean moveLeft, boolean moveRight,boolean moveUp, boolean moveDown, boolean attackKey, int screenWidth,int screenHeight,int background_x) {
        isMoving = false;
        isAttacking = false;
        isDead = false;
        // Zonele centrale ale ecranului pentru restricții de mișcare
        int centerMin = screenWidth / 2 - 100;
        int centerMax = screenWidth / 2 + 100;
        // Logica de mișcare a jucătorului (în funcție de fundal)
        if (background_x == 0) {
            // Fundalul nu mai poate merge în stânga — deci jucătorul are voie să se miște liber pe ecran
            if (moveRight && x + width < screenWidth) { direction = "right"; x += speed;
            } else if (moveLeft && x > 0) { direction = "left"; x -= speed; }
        } else {
            // PLAYER SE MIȘCĂ DOAR ÎN CENTRU — restul mișcă fundalul
            if (moveRight && x + width < centerMax && background_x > -3950) { direction = "right"; x += speed;
            } else if (moveRight && background_x > -3950) { direction = "right"; // fundalul se va mișca în afară de asta
            } else if (moveRight) {  direction = "right"; x += speed; }

            if (moveLeft && x > centerMin && background_x < 0) { direction = "left"; x -= speed;
            } else if (moveLeft && background_x < 0) { direction = "left"; // fundalul se va mișca în afară de asta
            } else if (moveLeft) { direction = "left";  x -= speed; }
        }
        // Limite laterale
        if(x<50) x = 50;
        if (x + width > screenWidth - 50) { x = screenWidth - 50 - width; }
        // Verifică dacă jucătorul se mișcă
        if(moveRight||moveLeft || moveUp || moveDown) isMoving = true;
        // Mișcare verticală
        if (moveUp && y > 140) { y -= 3;
        } else if (moveDown && y + height < screenHeight - 80) {  y += 3;}
        // Atac
        if (attackKey ) { isAttacking = true; }
        // Animație moarte
        if(isDying){
            deathFrameDelay++;
            if (deathFrameDelay >= maxDeathFrameDelay) { dieFrame++; deathFrameDelay = 0; }
            if (dieFrame >= 10) { dieFrame = 9; isDying = false; isDead = true; }
        }
        // Animație atac sau mers
        if (isAttacking) {
            attackFrame++;
            if (attackFrame >= 10) { attackFrame = 0;  }
        } else if (isMoving) {
            frame++;
            if (frame >= 10) { frame = 0; }
        }
    }

    /// Metodă de desenare a jucătorului și a inimilor
    public void draw(Graphics g) {
        BufferedImage image = null;
        // Selectează imaginea corectă în funcție de starea jucătorului
        if(isDying){
            if(direction.equals("right")){ image = rightDie[dieFrame];
            } else if(direction.equals("left")){ image =leftDie[dieFrame];}
        }
        else if (isAttacking) {
            if (direction.equals("right")) { image = rightAttack[attackFrame];
            } else if (direction.equals("left")) {  image = leftAttack[attackFrame]; }
        } else if(isMoving){
            if(direction.equals("right")){ image = rightRunning[frame];
            } else if(direction.equals("left")){ image =leftRunning[frame];}
        }
        else{
            if(direction.equals("right")){ image = idle_right;
            } else if(direction.equals("left")){ image = idle_left; }
        }
        // Desenează jucătorul
        g.drawImage(image,x,y,width,height,null);
        //pentru desenare inimi
        int heartX = 20;
        int heartY = 20;
        int heartWidth = 40;
        int heartHeight = 40;
        int maxHearts = 3;
        int hpPerHeart = maxHealth / maxHearts; // 33 (cu rest)
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
