package PaooGame.Object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

public class StarFinal {
    private int x, y;
    private int width = 20;
    private int height = 20;
    private boolean collected = false;

    public StarFinal(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (!collected) {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void collect() {
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }
}
