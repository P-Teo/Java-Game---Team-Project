package PaooGame.Levels;


import java.awt.Graphics;

public abstract class Level {
    /**
     * Metodă apelată pentru a actualiza logica nivelului.
     * @param left  tasta stângă
     * @param right tasta dreaptă
     * @param up    tasta sus
     * @param down  tasta jos
     * @param attack  apăsarea pentru atac
     * @param wndWidth  lățimea ferestrei
     * @param wndHeight înălțimea ferestrei
     */
    public abstract void update(boolean left, boolean right, boolean up, boolean down, boolean attack, int wndWidth, int wndHeight);

    /**
     * Metodă apelată pentru a desena conținutul nivelului.
     */
    public abstract void draw(Graphics g);

    /**
     * Metodă apelată când se dă click în interiorul ferestrei jocului.
     */
    public abstract void mouseClicked(int x, int y);
}
