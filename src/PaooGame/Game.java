package PaooGame;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import PaooGame.Entity.Player;

import javax.imageio.ImageIO;

enum GameState { // NEW
    START_MENU,
    LEVEL_SELECT,
    LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5,
    PAUSE,
    GAME_OVER
}

/*! \class Game
    \brief Clasa principala a intregului proiect. Implementeaza Game - Loop (Update -> Draw)

                ------------
                |           |
                |     ------------
    60 times/s  |     |  Update  |  -->{ actualizeaza variabile, stari, pozitii ale elementelor grafice etc.
        =       |     ------------
     16.7 ms    |           |
                |     ------------
                |     |   Draw   |  -->{ deseneaza totul pe ecran
                |     ------------
                |           |
                -------------
    Implementeaza interfata Runnable:

        public interface Runnable {
            public void run();
        }

    Interfata este utilizata pentru a crea un nou fir de executie avand ca argument clasa Game.
    Clasa Game trebuie sa aiba definita metoda "public void run()", metoda ce va fi apelata
    in noul thread(fir de executie). Mai multe explicatii veti primi la curs.

    In mod obisnuit aceasta clasa trebuie sa contina urmatoarele:
        - public Game();            //constructor
        - private void init();      //metoda privata de initializare
        - private void update();    //metoda privata de actualizare a elementelor jocului
        - private void draw();      //metoda privata de desenare a tablei de joc
        - public run();             //metoda publica ce va fi apelata de noul fir de executie
        - public synchronized void start(); //metoda publica de pornire a jocului
        - public synchronized void stop()   //metoda publica de oprire a jocului
 */
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
    private boolean messageDisplayed = false;
    private Image messageImage;
    private Graphics g;          /*!< Referinta catre un context grafic.*/
    private Player player;
    private GameState currentState = GameState.START_MENU; // NEW
    private Level1Background level1Background;
    private StartMenu startMenu;
    private LevelSelect levelSelect;

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

        player = new Player();
        level1Background = new Level1Background();
        startMenu = new StartMenu(this);
        levelSelect = new LevelSelect(this);

        currentState = GameState.START_MENU;
        startMenu.show();
        wnd.GetCanvas().setVisible(true);
    }


    private void setupKeyListener() {
        wnd.GetCanvas().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (currentState == GameState.START_MENU) {
                    // TODO: Start menu control
                } else if (currentState == GameState.LEVEL_SELECT) {
                    // TODO: Level select control
                } else if (currentState == GameState.LEVEL_1) {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = true;
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = true;
                    if (e.getKeyCode() == KeyEvent.VK_UP) moveUp = true;
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) moveDown = true;
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) attackKey = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (currentState == GameState.LEVEL_1) {
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
                if (currentState == GameState.LEVEL_1) {
                    messageDisplayed = false; // Ascunde mesajul când este apăsat mouse-ul
                }
            }
        });
        wnd.GetCanvas().setFocusable(true);
        wnd.GetCanvas().requestFocusInWindow();
    }


    private void InitGame() {
        System.out.println("Game Initialized");
        /// Este construita fereastra grafica.

        /// Se incarca toate elementele grafice
        loadAssets();
        setupKeyListener();
        Assets.Init();

    }


    public void run() {
        InitGame();
        System.out.println("Entering run method.");

        Canvas canvas = wnd.GetCanvas();

        int attempts = 0;
        while (!canvas.isDisplayable() ) {
            try {
                Thread.sleep(5);
                attempts++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!canvas.isDisplayable()) {
            System.out.println("Canvas still not displayable after waiting!");
            return;
        }

        System.out.println("Canvas displayable: " + canvas.isDisplayable());
        System.out.println("Canvas showing: " + canvas.isShowing());
        // Now create buffer strategy
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();


        if (bs == null) {
            System.out.println("BufferStrategy not successfully created!");
            return; // Adaugă un return aici
        } else {
            System.out.println("BufferStrategy created successfully."); // Mesaj de debug
        }

        while (runState) {
            Update();
            Draw(bs.getDrawGraphics());
            bs.show();

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
        currentState = newState;

        switch (currentState) {
            case START_MENU:
                startMenu.show();
                wnd.GetCanvas().setVisible(true);
                break;
            case LEVEL_SELECT:

               // wnd.GetCanvas().setVisible(false);
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
                // Confirm the visibility and display state after everything
                messageDisplayed = true;
                System.out.println("Canvas displayable after revalidation: " + wnd.GetCanvas().isDisplayable());
                System.out.println("Canvas showing after revalidation: " + wnd.GetCanvas().isShowing());
                break;
            case LEVEL_2:
                // drawLevel(g, 2);
                break;
            case LEVEL_3:
                //drawLevel(g, 3);
                break;
            case LEVEL_4:
                // drawLevel(g, 4);
                break;
            case LEVEL_5:
                // drawLevel(g, 5);
                break;
            case PAUSE:
                break;
            case GAME_OVER:
                // Nu desenezi nimic în stările respective (de exemplu, un meniu)
                break;
            default:
                throw new IllegalStateException("Stare necunoscută: " + currentState);
        }

    }


    private void Update() {
        if (currentState == GameState.LEVEL_1) {
            System.out.println("Updating LEVEL_1");

            level1Background.update(moveLeft, moveRight,wnd.GetWndWidth());
            player.update(moveLeft, moveRight,moveUp,moveDown,attackKey, wnd.GetWndWidth(),wnd.GetWndHeight());



        }
    }


    private void loadAssets() {
        try {
            // Folosește calea corectă pentru imaginea ta
            messageImage = ImageIO.read(new File("res/butoane/Untitled.png"));
            System.out.println("Imaginea a fost încărcată cu succes.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void drawMessage(Graphics g) {
        if (messageImage != null) {
            // Dimensiunile originale ale imaginii
            int imageWidth = messageImage.getWidth(null);
            int imageHeight = messageImage.getHeight(null);

            // Setează noile dimensiuni dorite (micsorate)
            int newWidth = imageWidth / 3; // De exemplu, reducere la jumătate
            int newHeight = imageHeight / 3; // De exemplu, reducere la jumătate

            // Calculăm poziția pentru a centra imaginea
            int x = (wnd.GetWndWidth() - newWidth) / 2; // Centrarea pe axa X
            int y = (wnd.GetWndHeight() - newHeight) / 2; // Centrarea pe axa Y

            // Creează un context grafic 2D pentru scalare
            Graphics2D g2d = (Graphics2D) g.create();

            // Aplică redimensionarea (scalare) și desenează imaginea
            g2d.drawImage(messageImage, x, y, newWidth, newHeight, null);

            // Textul mesajului
            String message = "Începe lupta";
            Font font = new Font("Georgia", Font.BOLD, 24);  // Alege o fontă potrivită
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);

            // Calculăm poziția pentru a centra mesajul pe imagine
            int textX = x + (newWidth - metrics.stringWidth(message)) / 2; // Centrarea textului pe axa X
            int textY = y + (newHeight + metrics.getHeight()) / 2; // Centrarea textului pe axa Y

            // Setăm culoarea fontului
            g2d.setColor(new Color(255, 215, 0));
            g2d.drawString(message, textX, textY);

            // Eliberează contextul grafic
            g2d.dispose();
        } else {
            System.out.println("Imaginea mesajului este null.");
        }
    }


    private void Draw(Graphics g) {
        if (g == null) {
            System.out.println("Graphics context is null in Draw");
            return;  // Safety check
        }

        // Clear the screen

        if (currentState == GameState.LEVEL_1) {
            System.out.println("Draw LEVEL_1");
            level1Background.draw(g);

            // Draw player
            player.draw(g);
            if (messageDisplayed) {
                drawMessage(g);
            }
            g.dispose();
        }
    }

    public GameState getState() {
        return currentState;
    }
    public GameWindow getWnd() {
        return wnd;
    }

}

