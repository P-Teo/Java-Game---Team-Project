package PaooGame.Levels;


import PaooGame.Entity.Enemylvl5;
import PaooGame.Object.StarFinal;
import PaooGame.Entity.Player;
import PaooGame.Game;
import PaooGame.GameState;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Level5Background;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList; // Importă ArrayList și List


public class Level5 extends Level {
    private Player player;
    private Game game;
    private Level5Background background;
    private GameWindow wnd;
    private Image messageImage;
    private boolean showMessage;
    private boolean levelCompleted;
    private boolean gameOver;
    Enemylvl5 enemy ;
    int maxEnemies = 2;
    //List<Enemylvl4_last> enemyPool = new ArrayList<>(maxEnemies);
    int maxNowEnemies;
    int currentEnemyIndex = 0;
    private boolean previousAttackState = false;
    private Rectangle continueButtonBounds = new Rectangle(650, 350, 50, 50); // Poziția și dimensiunea butonului de continuare
    private boolean showLevelCompleteMessage = false;
    private int score = 0;
    private int star=0;
    private int maxPlayerX = 0;
    private long startTime;
    private long levelCompleteTime;
    private boolean enemiesSpawned = false;
    private ArrayList<StarFinal> starsFinal;
    private int collectedStarsCount;
    // Lista pentru a stoca pozițiile prințesei pe care le vom desena pe mini-harta
    private List<Point> princessPath = new ArrayList<>();
    private boolean isPaused = false;
    private Rectangle pauseButtonBounds = new Rectangle(930, 8, 50, 50);
    private Rectangle resumeButtonBounds = new Rectangle(780, 530, 200, 50); // Poziția și dimensiunea butonului de reluare

    public Level5(Game game,GameWindow wnd) {
        this.game = game;
        this.wnd = wnd;
        maxNowEnemies=maxEnemies;
        loadAssets();
        score=0;
        star=0;
        player = new Player();
        background = new Level5Background();
        showMessage = true;
        levelCompleted = false;
        gameOver = false;
        startTime = System.currentTimeMillis();
        player.setHealth(500);
        player.speed = 8;
        collectedStarsCount = 0;
        starsFinal = new ArrayList<>();
        int[][] positions = {
                {900, 200}, {100, 300}, {500, 350}, {700, 250}, {250, 450},
                {200, 200}, {800, 300}, {400, 500}, {100, 450}, {700, 480},
                {300, 300}, {900, 500}, {400, 200}, {600, 380}, {500, 250}
        };
        for (int[] pos : positions) {
            starsFinal.add(new StarFinal(pos[0], pos[1]));
        }


    }

    public void update(boolean left, boolean right, boolean up, boolean down, boolean attack, int wndWidth, int wndHeight) {

        if (isPaused) {
            return; // Dacă jocul este în pauză, nu actualizăm nimic
        }

        if (!showMessage && !gameOver && !levelCompleted) {

            player.update(left, right, up, down, attack, wndWidth, wndHeight, background.getX());
        }

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
            maxNowEnemies = 1; // Dacă folosești acest contor
        }


        // Înainte de eliminarea inamicilor
        if(enemy!=null) {
            enemy.update(player.x, player.y, wndWidth, wndHeight);

            // Dacă inamicul e în range și atacă, lovește jucătorul
            if (enemy.getIsAttacking() && areEntitiesColliding(player, enemy)) {
                player.takeDamage(enemy.damage);


                // Dacă jucătorul atacă și e aproape, lovește inamicul
                if (attack && !previousAttackState && areEntitiesColliding(player, enemy)) {
                    enemy.takeDamage(player.damage);
                }
            }
        }
        // System.out.println("Număr inamici după eliminare: " + enemies.size());

        if (!showMessage && !levelCompleted && !gameOver) {
            if (maxNowEnemies == 0 && collectedStarsCount ==15 ) {
                System.out.println("Nivel finalizat!");
                levelCompleted = true;
                levelCompleteTime = System.currentTimeMillis();
                // Calculează scorul final
                long timeInSeconds = (levelCompleteTime - startTime) / 1000;
                int timeBonus = Math.max(0, 10000 - (int)timeInSeconds * 10);
                score += timeBonus;
                score +=player.getHealth()*100;

            }
        }

        for (StarFinal starFinal : starsFinal) {
            if (!starFinal.isCollected() && player.getBounds().intersects(starFinal.getBounds())) {
                starFinal.collect();
                score++;
                collectedStarsCount ++;
            }
        }

        if (player.isDead ) {
            gameOver = true;
        }
        previousAttackState = attack;


        // Elimină inamicii uciși
        if (enemy != null && enemy.isDead) {
            enemy = null; // gata, a dispărut complet
            maxNowEnemies--;
        }

        //actualizeaza scorul
        if (absoluteX > maxPlayerX) {
            maxPlayerX = absoluteX;
            score = maxPlayerX + (maxEnemies - maxNowEnemies) * 300;

        }
    }



    public void draw(Graphics g) {
        if (player.getHealth() ==100) {
            gameOver = false;
        }
        Graphics2D g2d = (Graphics2D) g.create();
        if (background != null && background.getImage() != null) {
            background.draw(g2d);
        } else {
            System.out.println("Background sau imaginea background-ului nu este încărcată corect.");
        }

        for (StarFinal starFinal : starsFinal) {
            starFinal.draw(g);
        }
        //System.out.println("showMessage: " + showMessage);
        ///System.out.println("gameOver: " + gameOver);
        ///System.out.println("levelCompleted: " + levelCompleted);
        if (!showMessage && !gameOver && !levelCompleted) {

            player.draw(g);
            drawScore(g);

        }

        if (showMessage) {
            drawMessage(g);
        }

        // Desenează inamicii
        if(!showMessage){
            if(enemy!= null)
                enemy.draw(g);
        }

        if (!showMessage && !gameOver && !levelCompleted && !isPaused) {
            drawFogOfWar(g);
        }
        //background.draw(g2d);
        if(!showMessage && !gameOver && !levelCompleted)
        {
            if (!isPaused) {
                drawPauseButton(g2d);
            }
            if (isPaused) {
                drawPauseMenu(g2d);
            }
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

        // Afișează mesajul de Game Over
        if (gameOver) {
            game.setState(GameState.GAME_OVER);
        }

    }

    public void reset() {
        startTime = System.currentTimeMillis();
         player = new Player();
        background = new Level5Background();
        showMessage = true;
        levelCompleted = false;
        gameOver = false;
        maxPlayerX = 0;
        /*enemies.clear();

        enemyPool.clear();  // Clear existing enemies in pool
        for (int i = 0; i < maxEnemies; i++) {
            enemyPool.add(new Enemylvl4_last());  // Add new enemies to reach maxEnemies
        }*/
        if(enemy!= null) {
            enemy = null;
        }
        maxNowEnemies = maxEnemies;
        currentEnemyIndex = 0;
        score=0;
        star=0;
        previousAttackState = false;
        princessPath.clear();
        enemiesSpawned=false;
        player.setHealth(500);
        player.speed = 8;
    }


    private void drawScore(Graphics g) {
        String scoreText = "Scor: " + score;
        Font font = new Font("Arial", Font.BOLD, 24);
        g.setFont(font);
        g.setColor(Color.YELLOW);

        int x = 20; // Stânga
        int y = wnd.GetWndHeight() - 20; // Jos

        g.drawString(scoreText, x, y);
    }


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

            // Fundal overlay întunecat
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

            // Imaginea de fundal centrată
            g2d.drawImage(messageImage, x, y, newWidth, newHeight, null);

            // Textul de felicitări
            String message = "Felicitări! Ai câștigat!";
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
            int[] xPoints = {
                    triangleCenterX - 20, triangleCenterX - 20, triangleCenterX + 10
            };
            int[] yPoints = {
                    triangleBaseY - 15, triangleBaseY + 15, triangleBaseY
            };

            // Hitbox pentru click pe triunghi (ajustat la noile coordonate)
            continueButtonBounds = new Rectangle(triangleCenterX - 20, triangleBaseY - 15, 30, 30);

            g2d.fillPolygon(xPoints, yPoints, 3);

            g2d.dispose();
        }
    }

    private boolean isMouseClickedInButton(int mouseX, int mouseY, int buttonX, int buttonY, int width, int height) {
        return mouseX >= buttonX && mouseX <= buttonX + width && mouseY >= buttonY && mouseY <= buttonY + height;
    }

    private boolean areEntitiesColliding(Player p, Enemylvl5 e) {
        Rectangle rectP = new Rectangle(p.x, p.y, p.width, p.height);
        Rectangle rectE = new Rectangle(e.x, e.y, e.width, e.height);
        return rectP.intersects(rectE);
    }

    private void loadAssets() {
        try {
            messageImage = ImageIO.read(new File("res/butoane/Untitled.png"));
            System.out.println("Imaginea a fost încărcată cu succes.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eroare la încărcarea imaginii!");
        }
    }

    private void drawMessage(Graphics g) {
        if (messageImage != null) {
            int imageWidth = messageImage.getWidth(null);
            int imageHeight = messageImage.getHeight(null);

            int newWidth = imageWidth / 3;
            int newHeight = imageHeight / 3;

            int x = (wnd.GetWndWidth() - newWidth) / 2;
            int y = (wnd.GetWndHeight() - newHeight) / 2;

            Graphics2D g2d = (Graphics2D) g.create();

            // Desenează imaginea redimensionată
            g2d.drawImage(messageImage, x, y, newWidth, newHeight, null);

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
        } else {
            System.out.println("Imaginea mesajului este null.");
        }
    }

    public void mouseClicked(int x, int y) {

        if (isMouseClickedInButton(x, y, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height)) {
            isPaused = !isPaused;
        }
        if (isPaused && isMouseClickedInButton(x, y, resumeButtonBounds.x, resumeButtonBounds.y, resumeButtonBounds.width, resumeButtonBounds.height)) {
            isPaused = false; // Reluăm jocul
        }
        if (showMessage && messageImage != null) {
            int imageWidth = messageImage.getWidth(null) / 3;
            int imageHeight = messageImage.getHeight(null) / 3;
            int imgX = (wnd.GetWndWidth() - imageWidth) / 2;
            int imgY = (wnd.GetWndHeight() - imageHeight) / 2;

            if (x >= imgX && x <= imgX + imageWidth && y >= imgY && y <= imgY + imageHeight) {
                hideMessage();
            }
        } else if (showLevelCompleteMessage && messageImage != null) {
            int imageWidth = messageImage.getWidth(null) / 3;
            int imageHeight = messageImage.getHeight(null);
            int imgX = (wnd.GetWndWidth() - imageWidth) / 2;
            int imgY = (wnd.GetWndHeight() - imageHeight) / 2;

            if (x >= imgX && x <= imgX + imageWidth && y >= imgY && y <= imgY + imageHeight) {
                game.setState(GameState.LEVEL_SELECT);
            }
        } else {
            System.out.println("mouse click.");
        }
    }


    public void hideMessage() {
        showMessage = false;
    }
    public int getScore(){
        return score;
    }
    public int getStar()
    {
        if(score>=24000)
            star=3;
        if(score>=21000 && score<24000)
            star=2;
        if(score>=18000 && score<21000)
            star=1;
        if(score<18000)
            star=0;
        return star;
    }

    private void drawPauseButton(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height);

        // Desenează chenarul negru
        g.setColor(Color.BLACK);
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

    private void drawPauseMenu(Graphics g) {
        // Fundal semi-transparent peste ecran
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        // Butonul de reluare - fundal albastru
        g.setColor(Color.BLUE);
        g.fillRect(resumeButtonBounds.x, resumeButtonBounds.y, resumeButtonBounds.width, resumeButtonBounds.height);

        // Chenar negru
        g.setColor(Color.BLACK);
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

    private void drawFogOfWar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        int screenWidth = wnd.GetWndWidth();
        int screenHeight = wnd.GetWndHeight();

        BufferedImage fog = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D fogGraphics = fog.createGraphics();

        fogGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Întunecă tot ecranul
        fogGraphics.setColor(new Color(0, 0, 0, 220));
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
