package PaooGame.Graphics;

import java.awt.image.BufferedImage;

public class LevelBackgroundFactory {

    public static LevelBackground createLevelBackground(int level) {
        switch (level) {
            case 1:
                return new Level1Background();
            case 2:
                return new Level2Background();
            case 3:
                return new Level3Background();
            case 4:
                return new Level4Background();
            case 5:
                return new Level5Background();
            default:
                throw new IllegalArgumentException("Nivel necunoscut: " + level);
        }
    }
}