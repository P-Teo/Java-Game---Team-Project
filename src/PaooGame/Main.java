package PaooGame;

import PaooGame.GameWindow.GameWindow; // Importă clasa pentru fereastra jocului

/**
 * Clasa Main reprezintă punctul de pornire al aplicației.
 */

public class Main
{
    public static void main(String[] args)
    {
        Game paooGame = new Game("PaooGame", 1000, 600);    // Creează o instanță a jocului, cu titlul "PaooGame" și o fereastră de 1000x600 pixeli
        paooGame.StartGame();   // Pornește bucla principală a jocului
    }
}

