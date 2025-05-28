package PaooGame;

import PaooGame.Levels.*;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Clasa principala a jocului. Aceasta gestionează fereastra, starea jocului,
 * și firul de execuție pentru actualizare și redare.
 */

public class Game implements Runnable {
    private GameWindow wnd;         /*!< Fereastra in care se va desena tabla jocului*/
    private boolean runState;       /*!< Flag ce indică dacă jocul rulează.*/
    private Thread gameThread;      /*!< Thread-ul jocului care rulează metoda run() */
    private BufferStrategy bs;      /*!< Strategia de buffer pentru redare eficientă pe Canvas */
    private Graphics g;           /*!< Context grafic pentru desenare */

    // Stări de mișcare
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean attackKey = false;
    private boolean jumpKey = false;
    private boolean jumpKeyHeld = false;

    private GameState currentState = GameState.START_MENU;  /*!< Starea curentă a jocului */

    // Obiecte pentru fiecare nivel
    private Level1 level1;
    private Level2 level2;
    private Level3 level3;
    private Level4 level4;
    private Level5 level5;

    public int nrLevel=0;       /*!< Nivelul curent */
    public int[] star = new int[7];     /*!< Numărul de stele obținute pe fiecare nivel */
    private int totalScore=0;       /*!< Scorul total acumulat */

    // Meniuri
    private StartMenu startMenu;
    private GameOver gameOver;
    private GameWin gameWin;
    private LevelSelect levelSelect;

    private final DatabaseManager db = new DatabaseManager();    /*!< Obiect pentru gestiunea bazei de date */
    private String playerName="jucator";    /*!< Numele jucătorului */

    /// Constructorul clasei Game. Inițializează fereastra și componentele jocului.
    public Game(String title, int width, int height) {
        wnd = new GameWindow(title, width, height);
        runState = false;
        wnd.BuildGameWindow();

        // Inițializare componente
        startMenu = new StartMenu(this);
        gameOver = new GameOver(this);
        gameWin = new GameWin(this);
        levelSelect = new LevelSelect(this);
        level1 = new Level1(this,wnd);
        level2 = new Level2(this,wnd);
        level3 = new Level3(this,wnd);
        level4 = new Level4(this,wnd);
        level5 = new Level5(this,wnd);

        nrLevel=1;
        totalScore=0;
        currentState = GameState.START_MENU;

        startMenu.show();
        wnd.GetCanvas().setVisible(true);
        waitForCanvasReady(wnd.GetCanvas());
    }


    /// Adaugă ascultători pentru taste și mouse pe canvas.
    private void setupKeyListener() {
        wnd.GetCanvas().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (currentState == GameState.START_MENU) {
                    //nu avem nevoie
                } else if (currentState == GameState.LEVEL_SELECT) {
                    //nu avem nevoie
                } else if (currentState == GameState.LEVEL_1 || currentState == GameState.LEVEL_2 || currentState == GameState.LEVEL_3 || currentState == GameState.LEVEL_4 || currentState == GameState.LEVEL_5) {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = true;
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = true;
                    if (e.getKeyCode() == KeyEvent.VK_UP) moveUp = true;
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) moveDown = true;
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) attackKey = true;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (currentState == GameState.LEVEL_1 || currentState == GameState.LEVEL_2 || currentState == GameState.LEVEL_3 || currentState == GameState.LEVEL_4 || currentState == GameState.LEVEL_5) {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = false;
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = false;
                    if (e.getKeyCode() == KeyEvent.VK_UP) moveUp = false;
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) moveDown = false;
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) attackKey = false;
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {}
        });
        wnd.GetCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Redirecționează clicul mouse-ului către nivelul activ
                if (currentState == GameState.LEVEL_1 ) { level1.mouseClicked(e.getX(), e.getY()); }
                if (currentState == GameState.LEVEL_2 ) { level2.mouseClicked(e.getX(), e.getY()); }
                if (currentState == GameState.LEVEL_3 ) { level3.mouseClicked(e.getX(), e.getY()); }
                if (currentState == GameState.LEVEL_4 ) { level4.mouseClicked(e.getX(), e.getY()); }
                if (currentState == GameState.LEVEL_5 ) { level5.mouseClicked(e.getX(), e.getY()); }
            }
        });
        wnd.GetCanvas().setFocusable(true);
        wnd.GetCanvas().requestFocusInWindow();
    }

    /// Inițializează resursele și ascultătorii.
    private void InitGame() { System.out.println("Game Initialized");  setupKeyListener();  Assets.Init(); }

    ///Metoda principală de rulare a jocului.
    public void run() {
        InitGame();
        System.out.println("Entering run method.");
        Canvas canvas = wnd.GetCanvas();
        while (runState) {
            Update();
            // Sari desenul dacă nu ești într-un nivel activ
            if (!(currentState == GameState.LEVEL_1 || currentState == GameState.LEVEL_2 || currentState == GameState.LEVEL_3 || currentState == GameState.LEVEL_4 || currentState == GameState.LEVEL_5)) {
                bs = null;  sleepShort(); continue;
            }
            if (bs == null) {
                canvas = wnd.GetCanvas();
                waitForCanvasReady(wnd.GetCanvas());
                try { canvas.createBufferStrategy(2);  bs = canvas.getBufferStrategy(); } catch (Exception e) { e.printStackTrace(); sleepShort(); continue; }
            }
            Graphics g = null;
            try { g = bs.getDrawGraphics(); if (g == null) { bs = null; continue; } Draw(g);
            } catch (Exception ex) { ex.printStackTrace(); bs = null; continue;
            } finally { if (g != null) g.dispose(); }
            try { bs.show();  } catch (Exception e) {  System.out.println("Caught exception on bs.show(). Resetting buffer strategy."); bs = null; }
            sleepShort(); // Controlul FPS-ului (~60)
        }
    }

    ///O mică pauză între cadre pentru a menține 60 FPS.
    private void sleepShort() {
        try {
            Thread.sleep(16); // ~60 FPS
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

   ///Pornește jocul și firul său de execuție.
    public synchronized void StartGame() {
        System.out.println("Starting game...");
        if (runState == false) {
            runState = true;    // Se actualizeaza flagul de stare a threadului
            gameThread = new Thread(this);
            gameThread.start();  // Threadul creat este lansat in executie
        } else { return; }
    }

        /// Oprește execuția jocului.
        public synchronized void StopGame() {
            if (runState == true) {
                runState = false;   // Actualizare stare thread
                try {
                    // Metoda join() pune un thread in asteptare pana cand un altul isi termina executie.
                    gameThread.join(); // Totusi, in situatia de fata efectul apelului este de oprire a threadului.
                } catch (InterruptedException ex) {
                    ex.printStackTrace(); // In situatia in care apare o exceptie
                }
            } else { return; }
        }


        //Setează o nouă stare a jocului și gestionează tranzițiile între ecrane.
    public void setState(GameState newState) {
        System.out.println("Changing state from " + currentState + " to " + newState);
        GameState oldState;
        oldState=currentState;
        currentState = newState;
        bs = null;
        moveRight = false;  moveLeft = false; moveUp = false; moveDown = false; attackKey = false;
        switch (currentState) {
            case START_MENU:
                startMenu.show();
                wnd.GetCanvas().setVisible(true);
                break;
            case LEVEL_SELECT:
                switch (oldState) {
                    case LEVEL_1:
                        totalScore += level1.getScore(); star[1]=level1.getStar(); // Adaugă scorul obținut la nivelul 1
                        break;
                    case LEVEL_2:
                        totalScore += level2.getScore(); star[2]=level2.getStar();// Adaugă scorul obținut la nivelul 2
                        break;
                    case LEVEL_3:
                        totalScore += level3.getScore(); star[3]=level3.getStar(); // Adaugă scorul obținut la nivelul 3
                        break;
                    case LEVEL_4:
                        totalScore += level4.getScore(); star[4]=level4.getStar(); // Adaugă scorul obținut la nivelul 4
                        break;
                    case LEVEL_5:
                        totalScore += level5.getScore(); star[5]=level5.getStar(); // Adaugă scorul obținut la nivelul 5
                        break;
                    default:
                        System.out.println("Stare de joc necunoscută!");// Dacă starea nu este una validă, nu adăugăm nimic
                        break;
                }
                wnd.GetCanvas().setVisible(false); // Ascundem canvas-ul
                wnd.getFrame().getContentPane().removeAll(); // Ștergem tot
                wnd.getFrame().getContentPane().add(levelSelect.getPanel()); // Adăugăm panoul pentru alegerea nivelului
                wnd.getFrame().revalidate(); // Actualizăm UI-ul
                wnd.getFrame().repaint();
                levelSelect.show();
                break;
            case LEVEL_1:
                wnd.GetCanvas().setFocusable(true);
                wnd.GetCanvas().requestFocusInWindow();
                wnd.getFrame().getContentPane().removeAll();
                wnd.getFrame().getContentPane().add(wnd.GetCanvas());
                wnd.getFrame().revalidate();
                wnd.getFrame().repaint();
                wnd.GetCanvas().setVisible(true);
                waitForCanvasReady(wnd.GetCanvas());
                 break;
            case LEVEL_2:
                wnd.GetCanvas().setFocusable(true);
                wnd.GetCanvas().requestFocusInWindow();
                wnd.getFrame().getContentPane().removeAll();
                wnd.getFrame().getContentPane().add(wnd.GetCanvas());
                wnd.getFrame().revalidate();
                wnd.getFrame().repaint();
                wnd.GetCanvas().setVisible(true);
                waitForCanvasReady(wnd.GetCanvas());
                waitForCanvasReady(wnd.GetCanvas());
                break;
            case LEVEL_3:
                wnd.GetCanvas().setFocusable(true);
                wnd.GetCanvas().requestFocusInWindow();
                wnd.getFrame().getContentPane().removeAll();
                wnd.getFrame().getContentPane().add(wnd.GetCanvas());
                wnd.getFrame().revalidate();
                wnd.getFrame().repaint();
                wnd.GetCanvas().setVisible(true);
                waitForCanvasReady(wnd.GetCanvas());
                break;
            case LEVEL_4:
                wnd.GetCanvas().setFocusable(true);
                wnd.GetCanvas().requestFocusInWindow();
                wnd.getFrame().getContentPane().removeAll();
                wnd.getFrame().getContentPane().add(wnd.GetCanvas());
                wnd.getFrame().revalidate();
                wnd.getFrame().repaint();
                wnd.GetCanvas().setVisible(true);
                waitForCanvasReady(wnd.GetCanvas());
               break;
            case LEVEL_5:
                wnd.GetCanvas().setFocusable(true);
                wnd.GetCanvas().requestFocusInWindow();
                wnd.getFrame().getContentPane().removeAll();
                wnd.getFrame().getContentPane().add(wnd.GetCanvas());
                wnd.getFrame().revalidate();
                wnd.getFrame().repaint();
                wnd.GetCanvas().setVisible(true);
                waitForCanvasReady(wnd.GetCanvas());
                 break;
            case GAME_OVER:
                gameOver.show();
                waitForCanvasReady(wnd.GetCanvas());
                this.reset();
                break;
            case GAME_WIN:
                gameWin.show();
                waitForCanvasReady(wnd.GetCanvas());
                nrLevel=1;
                totalScore=0;
                break;
            default:
                throw new IllegalStateException("Stare necunoscută: " + currentState);
        }

    }

    /// Actualizează logica jocului în funcție de starea curentă.
    private void Update() {
        if (currentState == GameState.LEVEL_1) { level1.update(moveLeft, moveRight, moveUp, moveDown, attackKey, wnd.GetWndWidth(), wnd.GetWndHeight()); }
        if (currentState == GameState.LEVEL_2) { level2.update(moveLeft, moveRight, moveUp, moveDown, attackKey, wnd.GetWndWidth(), wnd.GetWndHeight()); }
        if (currentState == GameState.LEVEL_3) { level3.update(moveLeft, moveRight, moveUp, moveDown, attackKey, wnd.GetWndWidth(), wnd.GetWndHeight()); }
        if (currentState == GameState.LEVEL_4) { level4.update(moveLeft, moveRight, moveUp, moveDown, attackKey, wnd.GetWndWidth(), wnd.GetWndHeight()); }
        if (currentState == GameState.LEVEL_5) { level5.update(moveLeft, moveRight, moveUp, moveDown, attackKey, wnd.GetWndWidth(), wnd.GetWndHeight()); }
    }

    /// Desenează nivelul activ pe ecran.
    private void Draw(Graphics g) {
        if (g == null) { return; }
        if (currentState == GameState.LEVEL_1) { level1.draw(g); db.saveCurrentLevel(nrLevel); g.dispose(); }
        if (currentState == GameState.LEVEL_2) { level2.draw(g); db.saveCurrentLevel(nrLevel); g.dispose(); }
        if (currentState == GameState.LEVEL_3) { level3.draw(g); db.saveCurrentLevel(nrLevel); g.dispose(); }
        if (currentState == GameState.LEVEL_4) { level4.draw(g); db.saveCurrentLevel(nrLevel); g.dispose(); }
        if (currentState == GameState.LEVEL_5) { level5.draw(g); db.saveCurrentLevel(nrLevel); g.dispose(); }
    }

    /// Resetează toate nivelurile.
    public void reset()
    {
        level1.reset();
        level2.reset();
        level3.reset();
        level4.reset();
        level5.reset();
    }

    /// Funcții utilitare și accesorii
    private void waitForCanvasReady(Canvas canvas) {
        while (!canvas.isDisplayable() || !canvas.isShowing()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public GameState getState() {
        return currentState;
    }   ///returnează starea
    public GameWindow getWnd() {
        return wnd;
    }   ///returnează fereastra
    public int getTotalScore(){
        return totalScore;
    }   ///returnează scorul total
    public int[] getStar(){
        return star;
    }   ///returnează stelele la fiecare nivel
    public void setTotalScore(int x){
        totalScore=x;
    }   ///setez scorul total
    public void setPlayerName(String name) {
        this.playerName = name;
    }   ///setez numele jucătorului //va fi apelată doar dacă se câștigă jocul
    public String getPlayerName() {
        return playerName;
    }   ///returnează numele jucătorului
    public DatabaseManager getDb() {
        return db;
    }   ///returnează baza de date

}
