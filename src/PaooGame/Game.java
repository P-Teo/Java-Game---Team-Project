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


public class Game implements Runnable {
    private GameWindow wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    private boolean runState;   /*!< Flag ce starea firului de executie.*/
    private Thread gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private BufferStrategy bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean attackKey = false;
    private boolean jumpKey = false;
    private boolean jumpKeyHeld = false;
    private Graphics g;          /*!< Referinta catre un context grafic.*/
    private GameState currentState = GameState.START_MENU; // NEW
    private Level1 level1;
    private Level2 level2;
    private Level3 level3;
    private Level4 level4;
    private Level5 level5;
    public int nrLevel=0;
    public int[] star = new int[7];
    private StartMenu startMenu;
    private GameOver gameOver;
    private GameWin gameWin;
    private LevelSelect levelSelect;
    private int totalScore=0;

    public Game(String title, int width, int height) {
        /// Obiectul GameWindow este creat insa fereastra nu este construita
        /// Acest lucru va fi realizat in metoda init() prin apelul
        /// functiei BuildGameWindow();
        wnd = new GameWindow(title, width, height);

        /// Resetarea flagului runState ce indica starea firului de executie (started/stoped)
        runState = false;



        wnd.BuildGameWindow();
        System.out.println("Canvas displayable after BuildGameWindow: " + wnd.GetCanvas().isDisplayable());
        System.out.println("Canvas showing after BuildGameWindow: " + wnd.GetCanvas().isShowing());

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


    private void setupKeyListener() {
        wnd.GetCanvas().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (currentState == GameState.START_MENU) {
                    // TODO: Start menu control
                } else if (currentState == GameState.LEVEL_SELECT) {
                    // TODO: Level select control
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
                if (currentState == GameState.LEVEL_1 ) {
                    level1.mouseClicked(e.getX(), e.getY()); // Ascunde mesajul când este apăsat mouse-ul
                }
                if (currentState == GameState.LEVEL_2) {
                    level2.mouseClicked(e.getX(), e.getY()); // Ascunde mesajul când este apăsat mouse-ul
                }
                if (currentState == GameState.LEVEL_3 ) {
                    level3.mouseClicked(e.getX(), e.getY()); // Ascunde mesajul când este apăsat mouse-ul
                }
                if (currentState == GameState.LEVEL_4) {
                    level4.mouseClicked(e.getX(), e.getY()); // Ascunde mesajul când este apăsat mouse-ul
                }
                if (currentState == GameState.LEVEL_5 ) {
                    level5.mouseClicked(e.getX(), e.getY()); // Ascunde mesajul când este apăsat mouse-ul
                }
            }
        });
        wnd.GetCanvas().setFocusable(true);
        wnd.GetCanvas().requestFocusInWindow();
    }



    private void InitGame() {
        System.out.println("Game Initialized");
        setupKeyListener();
        Assets.Init();

    }


    public void run() {
        InitGame();
        System.out.println("Entering run method.");
        Canvas canvas = wnd.GetCanvas();

        if (!canvas.isDisplayable()) {
            System.out.println("Canvas still not displayable after waiting!");
            return;
        }

        System.out.println("Canvas displayable: " + canvas.isDisplayable());
        System.out.println("Canvas showing: " + canvas.isShowing());

        while (runState) {
            Update();

            // Dacă nu ești într-un nivel cu redare pe canvas, sari desenul
            if (currentState != GameState.LEVEL_1 && currentState != GameState.LEVEL_2 && currentState != GameState.LEVEL_3 && currentState != GameState.LEVEL_4 && currentState != GameState.LEVEL_5) {
                // Resetează BufferStrategy dacă nu ești într-un nivel grafic
                bs = null;
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            if (bs == null) {
                canvas = wnd.GetCanvas(); // Asigură-te că ai referința actuală
                bs = canvas.getBufferStrategy();
                if (bs == null) {
                    try {
                        canvas.createBufferStrategy(2);
                        bs = canvas.getBufferStrategy();
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue; // Sari această iterație dacă eșuează
                    }
                    if (bs == null) {
                        System.out.println("BufferStrategy creation failed.");
                        continue;
                    }
                }
            }
            if (bs == null) {
                canvas = wnd.GetCanvas();
                canvas.createBufferStrategy(2);
                bs = canvas.getBufferStrategy();
                if (bs == null) {
                    System.out.println("Still null after createBufferStrategy.");
                    continue;
                }
            }


            Graphics g = null;
            try {
                g = bs.getDrawGraphics();////Aici da NULLPOINTEREXCEPTION
                if (g == null) {
                    System.out.println("Graphics context was null. Skipping draw.");
                    continue;
                }
                Draw(g);
            } catch (Exception ex) {
                ex.printStackTrace();
                bs = null; // reset
            } finally {
                if (g != null) g.dispose();
            }


            try {
                bs.show();
            } catch (NullPointerException e) {
                System.out.println("Caught NullPointerException on bs.show(). Skipping frame.");
                bs = null; // Resetează strategia pentru siguranță
                continue;
            }

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public synchronized void StartGame() {
        System.out.println("Starting game...");
        if (runState == false) {
            /// Se actualizeaza flagul de stare a threadului
            runState = true;
            /// Se construieste threadul avand ca parametru obiectul Game. De retinut faptul ca Game class
            /// implementeaza interfata Runnable. Threadul creat va executa functia run() suprascrisa in clasa Game.
            gameThread = new Thread(this);
            /// Threadul creat este lansat in executie (va executa metoda run())
            gameThread.start();
        } else {
            /// Thread-ul este creat si pornit deja
            return;
        }
    }


    public synchronized void StopGame() {
        if (runState == true) {
            /// Actualizare stare thread
            runState = false;
            /// Metoda join() arunca exceptii motiv pentru care trebuie incadrata intr-un block try - catch.
            try {
                /// Metoda join() pune un thread in asteptare panca cand un altul isi termina executie.
                /// Totusi, in situatia de fata efectul apelului este de oprire a threadului.
                gameThread.join();
            } catch (InterruptedException ex) {
                /// In situatia in care apare o exceptie pe ecran vor fi afisate informatii utile pentru depanare.
                ex.printStackTrace();
            }
        } else {
            /// Thread-ul este oprit deja.
            return;
        }
    }


    public void setState(GameState newState) {
        System.out.println("Changing state from " + currentState + " to " + newState);
        System.out.println(nrLevel);
        GameState oldState;
        oldState=currentState;
        currentState = newState;
        bs = null;
        moveRight = false;
        moveLeft = false;
        moveUp = false;
        moveDown = false;
        attackKey = false;
        switch (currentState) {
            case START_MENU:
                startMenu.show();
                wnd.GetCanvas().setVisible(true);
                break;
            case LEVEL_SELECT:
                switch (oldState) {
                    case LEVEL_1:
                        totalScore += level1.getScore(); // Adaugă scorul obținut la nivelul 1
                        star[1]=level1.getStar();
                        break;
                    case LEVEL_2:
                        totalScore += level2.getScore(); // Adaugă scorul obținut la nivelul 2
                        star[2]=level2.getStar();
                        break;
                    case LEVEL_3:
                        totalScore += level3.getScore(); // Adaugă scorul obținut la nivelul 3
                        star[3]=level3.getStar();
                        break;
                    case LEVEL_4:
                        totalScore += level4.getScore(); // Adaugă scorul obținut la nivelul 4
                        star[4]=level4.getStar();
                        break;
                    case LEVEL_5:
                        totalScore += level5.getScore(); // Adaugă scorul obținut la nivelul 5
                        star[5]=level5.getStar();
                        break;
                    default:
                        // Dacă starea nu este una validă, nu adăugăm nimic
                        System.out.println("Stare de joc necunoscută!");
                        break;
                }
                wnd.GetCanvas().setVisible(false); // Ascundem canvas-ul
                wnd.getFrame().getContentPane().removeAll(); // Ștergem tot
                wnd.getFrame().getContentPane().add(levelSelect.getPanel()); // Adăugăm panoul pentru alegerea nivelului
                wnd.getFrame().revalidate(); // Actualizăm UI-ul
                wnd.getFrame().repaint();
                levelSelect.show();
                System.out.println("Canvas displayable after: " + wnd.GetCanvas().isDisplayable());
                System.out.println("Canvas showing after: " + wnd.GetCanvas().isShowing());
                break;
            case LEVEL_1:
                //   levelSelect.hide(); // Hide level selection panel
                // Ensure the canvas is displayed properly
                wnd.GetCanvas().setFocusable(true);
                wnd.GetCanvas().requestFocusInWindow();
                wnd.getFrame().getContentPane().removeAll();
                wnd.getFrame().getContentPane().add(wnd.GetCanvas());
                wnd.getFrame().revalidate();
                wnd.getFrame().repaint();
                wnd.GetCanvas().setVisible(true);

                bs = null;
                BufferStrategy newBs = wnd.GetCanvas().getBufferStrategy();
                if (newBs == null) {
                    wnd.GetCanvas().createBufferStrategy(2);
                }

                // Confirm the visibility and display state after everything
                System.out.println("Canvas displayable after revalidation: " + wnd.GetCanvas().isDisplayable());
                System.out.println("Canvas showing after revalidation: " + wnd.GetCanvas().isShowing());
                break;
            case LEVEL_2:
                wnd.GetCanvas().setFocusable(true);
                wnd.GetCanvas().requestFocusInWindow();
                wnd.getFrame().getContentPane().removeAll();
                wnd.getFrame().getContentPane().add(wnd.GetCanvas());
                wnd.getFrame().revalidate();
                wnd.getFrame().repaint();
                wnd.GetCanvas().setVisible(true);
                // Confirm the visibility and display state after everything
                System.out.println("Canvas displayable after revalidation: " + wnd.GetCanvas().isDisplayable());
                System.out.println("Canvas showing after revalidation: " + wnd.GetCanvas().isShowing());
                break;
            case LEVEL_3:
                wnd.GetCanvas().setFocusable(true);
                wnd.GetCanvas().requestFocusInWindow();
                wnd.getFrame().getContentPane().removeAll();
                wnd.getFrame().getContentPane().add(wnd.GetCanvas());
                wnd.getFrame().revalidate();
                wnd.getFrame().repaint();
                wnd.GetCanvas().setVisible(true);
                // Confirm the visibility and display state after everything
                System.out.println("Canvas displayable after revalidation: " + wnd.GetCanvas().isDisplayable());
                System.out.println("Canvas showing after revalidation: " + wnd.GetCanvas().isShowing());
                break;
            case LEVEL_4:
                wnd.GetCanvas().setFocusable(true);
                wnd.GetCanvas().requestFocusInWindow();
                wnd.getFrame().getContentPane().removeAll();
                wnd.getFrame().getContentPane().add(wnd.GetCanvas());
                wnd.getFrame().revalidate();
                wnd.getFrame().repaint();
                wnd.GetCanvas().setVisible(true);
                // Confirm the visibility and display state after everything
                System.out.println("Canvas displayable after revalidation: " + wnd.GetCanvas().isDisplayable());
                System.out.println("Canvas showing after revalidation: " + wnd.GetCanvas().isShowing());
                break;
            case LEVEL_5:
                // drawLevel(g, 5);

                wnd.GetCanvas().setFocusable(true);
                wnd.GetCanvas().requestFocusInWindow();
                wnd.getFrame().getContentPane().removeAll();
                wnd.getFrame().getContentPane().add(wnd.GetCanvas());
                wnd.getFrame().revalidate();
                wnd.getFrame().repaint();
                wnd.GetCanvas().setVisible(true);
                // Confirm the visibility and display state after everything
                System.out.println("Canvas displayable after revalidation: " + wnd.GetCanvas().isDisplayable());
                System.out.println("Canvas showing after revalidation: " + wnd.GetCanvas().isShowing());
                break;
            case GAME_OVER:
                gameOver.show();
                nrLevel=1;
                totalScore=0;
                level1.reset();
                level2.reset();
                level3.reset();
                level4.reset();
                level5.reset();
                break;
            case GAME_WIN:
                gameWin.show();
                nrLevel=1;
                totalScore=0;
                break;
            default:
                throw new IllegalStateException("Stare necunoscută: " + currentState);
        }

    }


    private void Update() {
        if (currentState == GameState.LEVEL_1) {
            level1.update(moveLeft, moveRight, moveUp, moveDown, attackKey, wnd.GetWndWidth(), wnd.GetWndHeight());
        }
        if (currentState == GameState.LEVEL_2) {
            level2.update(moveLeft, moveRight, moveUp, moveDown, attackKey, wnd.GetWndWidth(), wnd.GetWndHeight());
        }
        if (currentState == GameState.LEVEL_3) {
            level3.update(moveLeft, moveRight, moveUp, moveDown, attackKey, wnd.GetWndWidth(), wnd.GetWndHeight());
        }
        if (currentState == GameState.LEVEL_4) {
            level4.update(moveLeft, moveRight, moveUp, moveDown, attackKey, wnd.GetWndWidth(), wnd.GetWndHeight());
        }
        if (currentState == GameState.LEVEL_5) {
            level5.update(moveLeft, moveRight, moveUp, moveDown, attackKey, wnd.GetWndWidth(), wnd.GetWndHeight());
        }
    }






    private void Draw(Graphics g) {
        if (g == null) {
            System.out.println("Graphics context is null in Draw");
            return;  // Safety check
        }

        // Clear the screen

        if (currentState == GameState.LEVEL_1) {
            level1.draw(g);
            g.dispose();
        }
        if (currentState == GameState.LEVEL_2) {
            level2.draw(g);
            g.dispose();
        }
        if (currentState == GameState.LEVEL_3) {
            level3.draw(g);
            g.dispose();
        }
        if (currentState == GameState.LEVEL_4) {
            level4.draw(g);
            g.dispose();
        }
        if (currentState == GameState.LEVEL_5) {
            level5.draw(g);
            g.dispose();
        }


    }

    public GameState getState() {
        return currentState;
    }
    public GameWindow getWnd() {
        return wnd;
    }
    private void waitForCanvasReady(Canvas canvas) {
        while (!canvas.isDisplayable() || !canvas.isShowing()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public int getTotalScore(){
        return totalScore;
    }
    public int[] getStar(){
        return star;
    }

    public void setTotalScore(int x){
        totalScore=x;
    }
    public void reset()
    {
        level1.reset();
        level2.reset();
        level3.reset();
        level4.reset();
        level5.reset();
    }

}
