package PaooGame.Entity;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/// Clasa pentru inamicul de nivel 4, moștenește clasa de bază Entity

public class Enemylvl4  extends Entity{
    // Imagini partajate între toți inamicii de acest tip
    private static BufferedImage[] sharedLeftRunning = new BufferedImage[15];
    private static BufferedImage[] sharedRightRunning = new BufferedImage[15];
    private static BufferedImage[] sharedLeftAttack = new BufferedImage[15];
    private static BufferedImage[] sharedRightAttack = new BufferedImage[15];
    private static BufferedImage[] sharedLeftDie = new BufferedImage[15];
    private static BufferedImage[] sharedRightDie = new BufferedImage[15];
    private static BufferedImage sharedIdleRight;
    private static BufferedImage sharedIdleLeft;
    // Indicator pentru a evita încărcarea multiplă
    private static boolean imagesLoaded = false;
    // Stare de atac a inamicului
    boolean isAttacking = false;

    /// Constructor
    public Enemylvl4() {
        x = 700;
        y = 300;
        speed = 7;
        width = 150;
        height = 120;
        health = 15;
        damage = 0.001;
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
        // Setare referințe către imaginile comune
        leftRunning = sharedLeftRunning;
        rightRunning = sharedRightRunning;
        leftAttack = sharedLeftAttack;
        rightAttack = sharedRightAttack;
        rightDie = sharedRightDie;
        leftDie = sharedLeftDie;
        idle_right = sharedIdleRight;
        idle_left = sharedIdleLeft;
    }

    /// Încărcare imagini partajate
    private void loadSharedImages() {
        try {
            for (int i = 0; i < 12; i++) {
                sharedLeftRunning[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Skeletons/Enemy4_run_left_" + i + ".png"));
                sharedRightRunning[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Skeletons/Enemy4_run_right_" + i + ".png"));
                sharedLeftAttack[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Skeletons/Enemy4_attack_left_" + i + ".png"));
                sharedRightAttack[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Skeletons/Enemy4_attack_right_" + i + ".png"));
            }
            for (int i = 0; i < 15; i++) {
                sharedLeftDie[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Skeletons/Enemy4_die_left_" + i + ".png"));
                sharedRightDie[i] = ImageIO.read(getClass().getResource("/Characters/Enemy4/Skeletons/Enemy4_die_right_" + i + ".png"));
            }
            sharedIdleRight = ImageIO.read(getClass().getResource("/Characters/Enemy4/Skeletons/Enemy4_idle_right.png"));
            sharedIdleLeft = ImageIO.read(getClass().getResource("/Characters/Enemy4/Skeletons/Enemy4_idle_left.png"));
            imagesLoaded = true;
            System.out.println("Imaginile Enemy1 au fost încărcate o singură dată.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getHealth() {
        return health;
    } /// Getter pentru sănătate

    /// Metodă pentru a aplica daune inamicului
    public void takeDamage(double amount) {
        health -= amount;
        if (health <= 0 && !isDying) {
            health = 0;
            isDying =true;
        }
    }
    public boolean getIsAttacking()
    {
        return isAttacking;
    } /// Returnează dacă inamicul atacă

    /// Actualizează logica inamicului (mișcare, atac, moarte)
    public void update(int xPlayer, int yPlayer, int screenWidth, int screenHeight) {
        if(isDead){ return; }
        // Animație de moarte
        if(isDying){
            dieFrame++;
            if (dieFrame >= 15) {
                dieFrame = 14;
                isDying = false;
                isDead = true;
            }
            return;
        }
        // Calculează distanța față de jucător
        int distanceX = this.x - xPlayer;
        int distanceY = this.y - yPlayer;
        // Mișcare orizontală către jucător
        if (Math.abs(distanceX) > 20) {
            if (distanceX > 0) {  direction = "left"; x -= speed;
            } else { direction = "right"; x += speed; }
            isMoving = true;
        }
        // Mișcare verticală către jucător
        if (Math.abs(distanceY) > 20) {
            if (distanceY > 0) { y -= speed;
            } else { y += speed; }
            isMoving = true;
        }
        // Atacă dacă este suficient de aproape de jucător
        isAttacking = Math.abs(distanceX) < 50 && Math.abs(distanceY) < 50;
        // Actualizare frame pentru animație
        if (isAttacking) {
            attackFrame++;
            if (attackFrame >= 10) {  attackFrame = 0; }
        } else if (isMoving) {
            frame++;
            if (frame >= 10) { frame = 0; }
        }
    }

    /// Desenează inamicul pe ecran
    public void draw(Graphics g) {
        BufferedImage image = null;
        // Selectează imaginea corespunzătoare stării inamicului
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
        // Desenează inamicul
        g.drawImage(image,x,y,width,height,null);

        // === Desenare Health Bar ===
        int barWidth = 40;
        int barHeight = 5;
        int barX = x + width / 2 - barWidth / 2;  // centrat deasupra inamicului
        int barY = y - 20;  // puțin deasupra

        // Calculează procentajul de viață
        double healthPercent = (double) health / 15;
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
