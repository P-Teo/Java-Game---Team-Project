package PaooGame.Levels;


import PaooGame.Entity.Enemylvl5;
import PaooGame.Graphics.LevelBackgroundFactory;
import PaooGame.Object.StarFinal;
import PaooGame.Entity.Player;
import PaooGame.Game;
import PaooGame.GameState;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.LevelBackground;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList; // Importă ArrayList și List

/// extinde clasa Level pentru a face nivelul 5

public class Level5 extends Level {
    private Player player; //personajul controlat de jucător
    private Game game;
    private LevelBackground background; // fundalul nivelului
    private GameWindow wnd;
    private Image messageImage;
    private boolean showMessage;
    private boolean levelCompleted;
    private boolean gameOver;
    Enemylvl5 enemy ; // inamic din nivel
    int maxEnemies = 2;
    int maxNowEnemies;
    int currentEnemyIndex = 0;
    private boolean previousAttackState = false;
    private Rectangle continueButtonBounds = new Rectangle(650, 350, 50, 50); // Poziția și dimensiunea butonului de continuare
    private boolean showLevelCompleteMessage = false;
    //scorul și stelele câștigate de jucător
    private int score = 0;
    private int star=0;
    private int maxPlayerX = 0;
    private long startTime;
    private long levelCompleteTime;
    private boolean enemiesSpawned = false;
    private ArrayList<StarFinal> starsFinal;
    private int collectedStarsCount;
    private List<Point> princessPath = new ArrayList<>(); // Lista pentru a stoca pozițiile prințesei pe care le vom desena pe mini-harta
    private boolean isPaused = false;
    private Rectangle pauseButtonBounds = new Rectangle(930, 8, 50, 50);
    private Rectangle resumeButtonBounds = new Rectangle(780, 530, 200, 50); // Poziția și dimensiunea butonului de reluare

    /// constructorul clasei
    public Level5(Game game,GameWindow wnd) {
        this.game = game;
        this.wnd = wnd;
        maxNowEnemies=maxEnemies;
        loadAssets();
        score=0; star=0;
        player = new Player();
        background = LevelBackgroundFactory.createLevelBackground(5);
        showMessage = true;
        levelCompleted = false;
        gameOver = false;
        startTime = System.currentTimeMillis();
        player.setHealth(500);
        player.speed = 8;
        collectedStarsCount = 0;
        starsFinal = new ArrayList<>();
        int[][] positions = {
                {850, 200}, {150, 300}, {500, 350}, {700, 250}, {250, 450},
                {200, 200}, {800, 270}, {400, 450}, {150, 450}, {700, 450},
                {300, 300}, {800, 430}, {400, 200}, {600, 380}, {500, 250}
        };
        for (int[] pos : positions) { starsFinal.add(new StarFinal(pos[0], pos[1])); }
    }

    /// Se ocupă cu logica de joc:
    public void update(boolean left, boolean right, boolean up, boolean down, boolean attack, int wndWidth, int wndHeight) {
        if (isPaused) { return; /* Dacă jocul este în pauză, nu actualizăm nimic*/ }
        if (!showMessage && !gameOver && !levelCompleted) { player.update(left, right, up, down, attack, wndWidth, wndHeight, background.getX()); }
        // Actualizează traseul prințesei
        int absoluteX =  player.x;
        princessPath.add(new Point(absoluteX, player.y));
        // Spawn-uieste inamicii în continuare
        int toSpawn = wndWidth / 4;
        // Spawn inamici doar când playerul ajunge la mijloc și nu au fost spawnați deja
        if (!enemiesSpawned && absoluteX != 200) {
            enemy = new Enemylvl5();
            enemy.x =wndWidth + 10;
            enemy.y = 100 + (int)(Math.random() * 400);
            enemiesSpawned = true;
            maxNowEnemies = 1;
        }

        // Înainte de eliminarea inamicilor
        if(enemy!=null) {
            enemy.update(player.x, player.y, wndWidth, wndHeight);
            // Dacă inamicul e în range și atacă, lovește jucătorul
            if (enemy.getIsAttacking() && areEntitiesColliding(player, enemy)) {
                player.takeDamage(enemy.damage);
                if (attack && !previousAttackState && areEntitiesColliding(player, enemy)) { enemy.takeDamage(player.damage); } // Dacă jucătorul atacă și e aproape, lovește inamicul
            }
        }
        if (!showMessage && !levelCompleted && !gameOver) {
            if (maxNowEnemies == 0 && collectedStarsCount ==15 ) {
                System.out.println("Nivel finalizat!");
                levelCompleted = true;
                levelCompleteTime = System.currentTimeMillis();
                long timeInSeconds = (levelCompleteTime - startTime) / 1000;
                int timeBonus = Math.max(0, 10000 - (int)timeInSeconds * 10);
                score = score + timeBonus + player.getHealth()*100; // Calculează scorul final
            }
        }
        //coleziunea cu stelele/bilele
        for (StarFinal starFinal : starsFinal) {
            if (!starFinal.isCollected()) {
                double dx = player.getX() + player.getWidth() / 2.0 - (starFinal.getX() + 10); // 10 = width/2 la stea
                double dy = player.getY() + player.getHeight() / 2.0 - (starFinal.getY() + 10); // idem
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance < 40) { starFinal.collect(); score++; collectedStarsCount++;
                }
            }
        }
        if (player.isDead ) { gameOver = true;  }
        previousAttackState = attack;
        if (enemy != null && enemy.isDead) { enemy = null;   maxNowEnemies--; } // Elimină inamicii uciși
        if (absoluteX > maxPlayerX) { maxPlayerX = absoluteX; score = maxPlayerX + (maxEnemies - maxNowEnemies) * 300; } //actualizeaza scorul
    }

    /// Desenează pe ecran fundalul, jucătorul, inamicu
    public void draw(Graphics g) {
        if (player.getHealth() ==100) { gameOver = false; }
        Graphics2D g2d = (Graphics2D) g.create();
        if (background != null && background.getImage() != null) { background.draw(g2d); } else { System.out.println("Background sau imaginea background-ului nu este încărcată corect."); }
        for (StarFinal starFinal : starsFinal) { starFinal.draw(g); }
        if (showMessage) { drawMessage(g); }
        if(!showMessage){ if(enemy!= null) enemy.draw(g); } // Desenează inamicii
        if (!showMessage && !gameOver && !levelCompleted && !isPaused) { drawFogOfWar(g); }
        if (!showMessage && !gameOver && !levelCompleted) { player.draw(g); drawScore(g); } //desenez scorul și bilele
        if(!showMessage && !gameOver && !levelCompleted)
        {
            if (!isPaused) { drawPauseButton(g2d); }
            if (isPaused) { drawPauseMenu(g2d); }
        }
        g2d.dispose();
        // Afișează mesajul de felicitări
        if (levelCompleted) {
            game.getDb().saveLevelScore(game.nrLevel+1, score);
            game.getDb().saveCurrentLevel(6);
            game.nrLevel=0;
            drawLevelCompleteMessage(g);
            game.setState(GameState.GAME_WIN);
        }
        if (gameOver) { game.setState(GameState.GAME_OVER); } // Afișează mesajul de Game Over
    }

    /// funcție de resteare nivel
    public void reset() {
        startTime = System.currentTimeMillis();
         player = new Player();
        background = LevelBackgroundFactory.createLevelBackground(5);
        showMessage = true;
        levelCompleted = false;
        gameOver = false;
        maxPlayerX = 0;
        if(enemy!= null) { enemy = null; }
        maxNowEnemies = maxEnemies;
        currentEnemyIndex = 0;
        score=0; star=0;
        previousAttackState = false;
        princessPath.clear();
        enemiesSpawned=false;
        player.setHealth(500);
        player.speed = 8;
        starsFinal.clear();
    }

    /// funcție desenare scor și număr stele/bile rămase de colecționat
    private void drawScore(Graphics g) {
        String scoreText = "Scor: " + score;
        String starsRemainingText = "Bile rămase: " + (15 - collectedStarsCount);
        Font font = new Font("Arial", Font.BOLD, 24);
        g.setFont(font);
        g.setColor(Color.YELLOW);
        int x = 20; // Stânga
        int y = wnd.GetWndHeight() - 20; // Jos
        g.drawString(scoreText, x, y);
        g.setColor(Color.CYAN); // Altă culoare pentru stele
        g.drawString(starsRemainingText, x, y - 30); // Deasupra scorului
    }


    /// desenare nivel complet
    private void drawLevelCompleteMessage(Graphics g) {
        showLevelCompleteMessage = true;
        if (messageImage != null) {
            int imageWidth = messageImage.getWidth(null);
            int imageHeight = messageImage.getHeight(null);
            int newWidth = imageWidth / 3;
            int newHeight = imageHeight ;
            int x = (wnd.GetWndWidth() - newWidth) / 2;
            int y = (wnd.GetWndHeight() - newHeight) / 2;
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(0, 0, 0, 150));  // Fundal overlay întunecat
            g2d.fillRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());
            g2d.drawImage(messageImage, x, y, newWidth, newHeight, null); // Imaginea de fundal centrată
            String message = "Felicitări! Ai câștigat!";  // Textul de felicitări
            Font font = new Font("Georgia", Font.BOLD, 32);
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);
            int textX = x + (newWidth - metrics.stringWidth(message)) / 2;
            int textY = y + (newHeight + metrics.getHeight()) / 2 - 20;
            g2d.setColor(Color.ORANGE);
            g2d.drawString(message, textX, textY);
            // Textul scorului final
            String nrStar = "*".repeat(this.getStar());
            String scoreText = "Scor final: " + score +"   "+nrStar ;
            Font scoreFont = new Font("Georgia", Font.PLAIN, 24);
            g2d.setFont(scoreFont);
            FontMetrics scoreMetrics = g2d.getFontMetrics(scoreFont);
            int scoreTextX = x + (newWidth - scoreMetrics.stringWidth(scoreText)) / 2;
            int scoreTextY = textY + scoreMetrics.getHeight() + 10;
            g2d.setColor(Color.WHITE);
            g2d.drawString(scoreText, scoreTextX, scoreTextY);
            // Desenează triunghiul (buton de continuare) în interiorul imaginii
            g2d.setColor(Color.ORANGE);
            int triangleCenterX = x + newWidth / 2;
            int triangleBaseY = y + newHeight - 45;
            // Desenăm triunghiul cu vârful spre dreapta
            int[] xPoints = { triangleCenterX - 20, triangleCenterX - 20, triangleCenterX + 10 };
            int[] yPoints = { triangleBaseY - 15, triangleBaseY + 15, triangleBaseY };
            continueButtonBounds = new Rectangle(triangleCenterX - 20, triangleBaseY - 15, 30, 30); // Hitbox pentru click pe triunghi (ajustat la noile coordonate)
            g2d.fillPolygon(xPoints, yPoints, 3);
            g2d.dispose();
        }
    }

    /// interacțiune cu utilizatorul
    private boolean isMouseClickedInButton(int mouseX, int mouseY, int buttonX, int buttonY, int width, int height) {
        return mouseX >= buttonX && mouseX <= buttonX + width && mouseY >= buttonY && mouseY <= buttonY + height;
    }

    /// coliziune
    private boolean areEntitiesColliding(Player p, Enemylvl5 e) {
        Rectangle rectP = new Rectangle(p.x, p.y, p.width, p.height);
        Rectangle rectE = new Rectangle(e.x, e.y, e.width, e.height);
        return rectP.intersects(rectE);
    }

    /// încărcare fișiere
    private void loadAssets() {
        try { messageImage = ImageIO.read(new File("res/butoane/Untitled.png")); System.out.println("Imaginea a fost încărcată cu succes.");
        } catch (IOException e) { e.printStackTrace(); System.out.println("Eroare la încărcarea imaginii!"); }
    }

    /// desenez mesajul de început
    private void drawMessage(Graphics g) {
        if (messageImage != null) {
            int imageWidth = messageImage.getWidth(null);
            int imageHeight = messageImage.getHeight(null);
            int newWidth = imageWidth / 3;
            int newHeight = imageHeight / 3;
            int x = (wnd.GetWndWidth() - newWidth) / 2;
            int y = (wnd.GetWndHeight() - newHeight) / 2;
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(messageImage, x, y, newWidth, newHeight, null); // Desenează imaginea redimensionată
            // Mesaj mic - font mai mic, poziționat mai sus în chenar
            String smallMessage = "Colecționează toate bilele și omoară regele!";
            Font smallFont = new Font("Georgia", Font.PLAIN, 16);
            g2d.setFont(smallFont);
            FontMetrics smallMetrics = g2d.getFontMetrics(smallFont);
            int smallTextX = x + (newWidth - smallMetrics.stringWidth(smallMessage)) / 2;
            int smallTextY = y + smallMetrics.getAscent() + 10; // puțin de jos din marginea superioară
            g2d.setColor(new Color(255, 215, 0));
            g2d.drawString(smallMessage, smallTextX, smallTextY);
            // Mesaj mare - font mai mare, poziționat mai jos în chenar
            String bigMessage = "Începe lupta";
            Font bigFont = new Font("Georgia", Font.BOLD, 24);
            g2d.setFont(bigFont);
            FontMetrics bigMetrics = g2d.getFontMetrics(bigFont);
            int bigTextX = x + (newWidth - bigMetrics.stringWidth(bigMessage)) / 2;
            int bigTextY = y + newHeight - bigMetrics.getDescent() - 10; // puțin de sus din marginea inferioară
            g2d.drawString(bigMessage, bigTextX, bigTextY);
            g2d.dispose();
        } else { System.out.println("Imaginea mesajului este null."); }
    }

    /// interacțiunea utilizatorului
    public void mouseClicked(int x, int y) {
        if (isMouseClickedInButton(x, y, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height)) { isPaused = !isPaused; }
        if (isPaused && isMouseClickedInButton(x, y, resumeButtonBounds.x, resumeButtonBounds.y, resumeButtonBounds.width, resumeButtonBounds.height)) { isPaused = false; }
        if (showMessage && messageImage != null) {
            int imageWidth = messageImage.getWidth(null) / 3;
            int imageHeight = messageImage.getHeight(null) / 3;
            int imgX = (wnd.GetWndWidth() - imageWidth) / 2;
            int imgY = (wnd.GetWndHeight() - imageHeight) / 2;
            if (x >= imgX && x <= imgX + imageWidth && y >= imgY && y <= imgY + imageHeight) { hideMessage(); }
        } else if (showLevelCompleteMessage && messageImage != null) {
            int imageWidth = messageImage.getWidth(null) / 3;
            int imageHeight = messageImage.getHeight(null);
            int imgX = (wnd.GetWndWidth() - imageWidth) / 2;
            int imgY = (wnd.GetWndHeight() - imageHeight) / 2;
            if (x >= imgX && x <= imgX + imageWidth && y >= imgY && y <= imgY + imageHeight) { game.setState(GameState.LEVEL_SELECT); }
        } else { System.out.println("mouse click."); }
    }

    public void hideMessage() {
        showMessage = false;
    } ///ascund mesajul

    public int getScore(){
        return score;
    } ///returnează scorul

    /// returnez numărul de stele
    public int getStar()
    {
        if(score>=24000) star=3;
        if(score>=21000 && score<24000) star=2;
        if(score>=18000 && score<21000) star=1;
        if(score<18000) star=0;
        return star;
    }

    /// desenez butonul de pauză
    private void drawPauseButton(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height);
        g.setColor(Color.BLACK); // Desenează chenarul negru
        g.drawRect(pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width - 1, pauseButtonBounds.height - 1);
        // Desenează simbolul pauză: două bare verticale
        g.setColor(Color.ORANGE);
        Graphics2D g2 = (Graphics2D) g;
        int barWidth = 8;
        int barHeight = 30;
        int spaceBetween = 10;
        int bar1X = pauseButtonBounds.x + (pauseButtonBounds.width - 2 * barWidth - spaceBetween) / 2;
        int barY = pauseButtonBounds.y + (pauseButtonBounds.height - barHeight) / 2;
        int bar2X = bar1X + barWidth + spaceBetween;
        g2.fillRect(bar1X, barY, barWidth, barHeight); // prima bară
        g2.fillRect(bar2X, barY, barWidth, barHeight); // a doua bară
    }

    /// desenez meniul de pauză
    private void drawPauseMenu(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150)); // Fundal semi-transparent peste ecran
        g.fillRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());
        g.setColor(Color.BLUE);  // Butonul de reluare - fundal albastru
        g.fillRect(resumeButtonBounds.x, resumeButtonBounds.y, resumeButtonBounds.width, resumeButtonBounds.height);
        g.setColor(Color.BLACK);  // Chenar negru
        g.drawRect(resumeButtonBounds.x, resumeButtonBounds.y, resumeButtonBounds.width - 1, resumeButtonBounds.height - 1);
        // Text portocaliu și font setat
        g.setColor(Color.ORANGE);
        Font font = new Font("Arial", Font.BOLD, 20);
        g.setFont(font);
        // Centrare aproximativă a textului
        FontMetrics metrics = g.getFontMetrics(font);
        String text = "Reluare joc";
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        int textX = resumeButtonBounds.x + (resumeButtonBounds.width - textWidth) / 2;
        int textY = resumeButtonBounds.y + (resumeButtonBounds.height + textHeight) / 2 - metrics.getDescent();
        g.drawString(text, textX, textY);
    }

    /// Metodă pentru ceață
    private void drawFogOfWar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        int screenWidth = wnd.GetWndWidth();
        int screenHeight = wnd.GetWndHeight();
        BufferedImage fog = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D fogGraphics = fog.createGraphics();
        fogGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        fogGraphics.setColor(new Color(0, 0, 0, 250)); // Întunecă tot ecranul
        fogGraphics.fillRect(0, 0, screenWidth, screenHeight);
        // Zona vizibilă din jurul jucătorului
        int radius = 100;
        int centerX = player.x - background.getX() + player.width / 2;
        int centerY = player.y + player.height / 2;
        fogGraphics.setComposite(AlphaComposite.Clear);
        fogGraphics.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        //  Zona de sus pentru inimi – ocolire ceață
        int heartClearX = 10;    // sau 0
        int heartClearY = 10;
        int heartClearWidth = 160;  // destul pentru 3 inimi + spațiu
        int heartClearHeight = 60;
        fogGraphics.fillRect(heartClearX, heartClearY, heartClearWidth, heartClearHeight);
        fogGraphics.dispose();
        g2d.drawImage(fog, 0, 0, null);
        g2d.dispose();
    }
}
