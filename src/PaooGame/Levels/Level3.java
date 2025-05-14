package PaooGame.Levels;


import PaooGame.Castle.Castle1;
import PaooGame.Entity.Enemylvl3;
import PaooGame.Entity.Player;
import PaooGame.Game;
import PaooGame.GameState;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Level3Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList; // Importă ArrayList și List


public class Level3 extends Level {
    private Player player;
    private Game game;
    private Level3Background background;
    private GameWindow wnd;
    private Image messageImage;
    private boolean showMessage;
    private boolean levelCompleted;
    private boolean gameOver;
    int maxEnemies = 12;
    int maxNowEnemies;
    List<Enemylvl3> enemies = new ArrayList<>();
    List<Enemylvl3> enemyPool = new ArrayList<>(maxEnemies);
    int currentEnemyIndex = 0;
    private boolean previousAttackState = false;
    private Rectangle continueButtonBounds = new Rectangle(650, 350, 50, 50); // Poziția și dimensiunea butonului de continuare
    private boolean showLevelCompleteMessage = false;
    private int score = 0;
    private int star=0;
    private int maxPlayerX = 0;
    private long startTime;
    private long levelCompleteTime;
    private final Castle1 castle1;
    private final Castle1 castle2;

    // Lista pentru a stoca pozițiile prințesei pe care le vom desena pe mini-harta
    private List<Point> princessPath = new ArrayList<>();
    private boolean isPaused = false;
    private Rectangle pauseButtonBounds = new Rectangle(930, 8, 50, 50);
    private Rectangle resumeButtonBounds = new Rectangle(780, 530, 200, 50); // Poziția și dimensiunea butonului de reluare

    public Level3(Game game,GameWindow wnd) {
        this.game = game;
        this.wnd = wnd;
        maxNowEnemies=maxEnemies;
        loadAssets();
        player = new Player();
        score=0;
        star=0;
        background = new Level3Background();
        showMessage = true;
        levelCompleted = false;
        gameOver = false;
        startTime = System.currentTimeMillis();
        castle1 = new Castle1(-100,200,250,300, "/BackgroundCastle/Castle1.png");
        castle2 = new Castle1(background.getWidth()-200,200,275,175,"/BackgroundCastle/Castle2.png");

        for(int i = 0;i< maxEnemies;i++){
            enemyPool.add(new Enemylvl3());
        }

    }

    public void update(boolean left, boolean right, boolean up, boolean down, boolean attack, int wndWidth, int wndHeight) {

        if (isPaused) {
            return; // Dacă jocul este în pauză, nu actualizăm nimic
        }

        background.update(left, right, wndWidth);
        if (!showMessage && !gameOver && !levelCompleted) {
            player.update(left, right, up, down, attack, wndWidth, wndHeight, background.getX());
        }

        // Actualizează traseul prințesei
        int absoluteX = -background.getX() + player.x;
        princessPath.add(new Point(absoluteX, player.y));

        // Spawn-uieste inamicii în continuare
        while (currentEnemyIndex < maxEnemies && absoluteX > 500 + currentEnemyIndex * 320) {
            Enemylvl3 newEnemy = enemyPool.remove(0);

            // Poziții random pe Y între 100 și 500
            int randomY = 100+ (int)(Math.random() * 400);
            newEnemy.y = randomY;

            // Alternăm spawn-ul: unii din stânga, unii din dreapta
            if (currentEnemyIndex % 3 == 0) {
                newEnemy.x = -newEnemy.width - 10; // din stânga
            } else {
                newEnemy.x = wndWidth + 10; // din dreapta
            }

            System.out.println("Inamic nou: X = " + newEnemy.x + ", Y = " + newEnemy.y);
            enemies.add(newEnemy);
            currentEnemyIndex++;
        }

        // Înainte de eliminarea inamicilor
        for (Enemylvl3 enemy : enemies) {
            enemy.update(player.x, player.y, wndWidth, wndHeight);

            // Dacă inamicul e în range și atacă, lovește jucătorul
            if (enemy.getIsAttacking() && areEntitiesColliding(player, enemy)) {
                player.takeDamage(enemy.damage);
            }

            // Dacă jucătorul atacă și e aproape, lovește inamicul
            if (attack&&!previousAttackState  && areEntitiesColliding(player, enemy)) {
                enemy.takeDamage(player.damage);
                previousAttackState = true;
            }
            else if (!attack) {
                previousAttackState = false; // Permite atacul din nou dacă jucătorul nu mai atacă
            }
        }
        // System.out.println("Număr inamici după eliminare: " + enemies.size());

        if (!showMessage && !levelCompleted && !gameOver) {
            if (maxNowEnemies == 0 && enemies.isEmpty()) {
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

        if (player.isDead ) {
            gameOver = true;
        }
        previousAttackState = attack;


        // Elimină inamicii uciși
        enemies.removeIf(e -> {
            if (e.isDead) {
                maxNowEnemies--;
                return true;
            }
            return false;
        });


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
        //System.out.println("showMessage: " + showMessage);
        ///System.out.println("gameOver: " + gameOver);
        ///System.out.println("levelCompleted: " + levelCompleted);
        if (!showMessage && !gameOver && !levelCompleted) {

            castle1.draw(g, background.getX());
            castle2.draw(g, background.getX());
            player.draw(g);
            drawScore(g);
            drawMiniMap(g);// Desenarea mini-hărții

        }

        if (showMessage) {
            drawMessage(g);
        }

        // Desenează inamicii
        for (Enemylvl3 enemy : enemies) {
            if(!showMessage){
                enemy.draw(g);
            }
        }

        // Afișează mesajul de felicitări
        if (levelCompleted) {
            game.nrLevel=4;
            drawLevelCompleteMessage(g);
        }

        // Afișează mesajul de Game Over
        if (gameOver) {
            game.setState(GameState.GAME_OVER);
        }

    }

    public void reset() {
        startTime = System.currentTimeMillis();
        // player = new Player();
        //background = new Level1Background();
        showMessage = true;
        levelCompleted = false;
        gameOver = false;
        // maxPlayerX = 0;
        //enemies.clear();
        //maxNowEnemies = maxEnemies;
        //currentEnemyIndex = 0;
        score=0;
        star=0;
        previousAttackState = false;
        //princessPath.clear();
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

    private boolean areEntitiesColliding(Player p, Enemylvl3 e) {
        Rectangle rectP = new Rectangle(p.x, p.y, p.width, p.height);
        Rectangle rectE = new Rectangle(e.x, e.y, e.width, e.height);
        return rectP.intersects(rectE);
    }

    private void drawMiniMap(Graphics g) {
        // Redimensionează fundalul pentru mini-hartă (o versiune mai mică)
        int miniMapWidth = 375; // Lățimea mini-hărții
        int miniMapHeight = (int) (miniMapWidth * ((double) background.getHeight() / background.getWidth())); // Înălțimea mini-hărții
        int miniMapX = 530; // Poziția pe axa X
        int miniMapY = 10; // Poziția pe axa Y

        // Desenează fundalul mini-hărții
        g.drawImage(background.getImage(), miniMapX, miniMapY, miniMapWidth, miniMapHeight, null);

        // Scalează pozițiile din coordonatele mari ale jocului pe mini-harta
        double scaleX = (double) miniMapWidth / background.getWidth();
        double scaleY = (double) miniMapHeight / background.getHeight();

        // Desenarea traseului prințesei pe mini-hartă
        g.setColor(Color.RED);
        for (int i = 1; i < princessPath.size(); i++) {
            Point p1 = princessPath.get(i - 1);
            Point p2 = princessPath.get(i);

            int x1 = (int) (p1.x * scaleX);
            int y1 = (int) (p1.y * scaleY);
            int x2 = (int) (p2.x * scaleX);
            int y2 = (int) (p2.y * scaleY);

            g.drawLine(miniMapX + x1, miniMapY + y1, miniMapX + x2, miniMapY + y2);
        }
        // Desenarea poziției curente a prințesei pe mini-hartă
        int absolutePlayerX = -background.getX() + player.x;
        int playerMiniMapX = (int) (absolutePlayerX * scaleX);
        int playerMiniMapY = (int) (player.y * scaleY);
        g.setColor(Color.GREEN);
        g.fillRect(miniMapX + playerMiniMapX - 5, miniMapY + playerMiniMapY - 5, 10, 10); // Un pătrat verde pentru jucător

        // Desenează chenarul îngroșat
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.ORANGE); // Culoarea chenarului
        g2d.setStroke(new BasicStroke(1)); // Grosimea chenarului
        g2d.drawRect(miniMapX, miniMapY, miniMapWidth, miniMapHeight);
        g2d.dispose();


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

            g2d.drawImage(messageImage, x, y, newWidth, newHeight, null);

            String message = "Începe lupta";
            Font font = new Font("Georgia", Font.BOLD, 24);
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);

            int textX = x + (newWidth - metrics.stringWidth(message)) / 2;
            int textY = y + (newHeight + metrics.getHeight()) / 2;

            g2d.setColor(new Color(255, 215, 0));
            g2d.drawString(message, textX, textY);

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


}