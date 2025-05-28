package PaooGame.Levels;

import java.awt.Graphics;

/**
 * Clasa abstractă care definește comportamentul de bază al unui nivel din joc.
 * Fiecare nivel concret extinde această clasă și implementează metodele sale.
 */
public abstract class Level {

    ///Metodă apelată pentru a actualiza logica nivelului.
    public abstract void update(boolean left, boolean right, boolean up, boolean down, boolean attack, int wndWidth, int wndHeight);

    /// Metodă apelată pentru a desena conținutul nivelului.
    public abstract void draw(Graphics g);

    ///Metodă apelată când se dă click în interiorul ferestrei jocului
    public abstract void mouseClicked(int x, int y);
}
