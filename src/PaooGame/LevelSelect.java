package PaooGame;

import javax.swing.*;
import java.awt.*;

public class LevelSelect {
    private Game game;
    private JPanel levelPanel;

    public LevelSelect(Game game) {
        this.game = game;
        createUI();
    }

    private void createUI() {
        // Creăm panoul principal pentru niveluri
        levelPanel = new JPanel();
        levelPanel.setLayout(null); // Setăm layout-ul la null pentru a putea poziționa elementele manual
        levelPanel.setOpaque(true); // Asigurăm că panoul este complet opac

        // Căutăm imaginea de fundal
        String imagePath = "paoo-proiect-condurache_papa-master/res/map.png";  // Calea fișierului de imagine
        ImageIcon originalIcon = new ImageIcon(imagePath);

        // Redimensionăm imaginea
        Image scaledImage = originalIcon.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Creăm JLabel cu imaginea redimensionată
        JLabel backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setBounds(0, 0, 1000, 600);  // Setăm poziția și dimensiunile imaginii

        // Adăugăm imaginea ca fundal
        levelPanel.add(backgroundLabel);

        // Creăm butoane pentru fiecare nivel
        JButton level1Btn = createImageButton("paoo-proiect-condurache_papa-master/res/butoane/Untitled.png", 240, 50, "1");
        JButton level2Btn = createImageButton("paoo-proiect-condurache_papa-master/res/butoane/Untitled.png", 240, 50, "2");
        JButton level3Btn = createImageButton("paoo-proiect-condurache_papa-master/res/butoane/Untitled.png", 240, 50, "3");
        JButton level4Btn = createImageButton("paoo-proiect-condurache_papa-master/res/butoane/Untitled.png", 240, 50, "4");
        JButton level5Btn = createImageButton("paoo-proiect-condurache_papa-master/res/butoane/Untitled.png", 240, 50, "5");

        // Setăm pozițiile butoanelor
        level1Btn.setBounds(400, 100, 240, 50); // Poziționăm butonul Nivel 1
        level2Btn.setBounds(400, 160, 240, 50); // Poziționăm butonul Nivel 2
        level3Btn.setBounds(400, 220, 240, 50); // Poziționăm butonul Nivel 3
        level4Btn.setBounds(400, 280, 240, 50); // Poziționăm butonul Nivel 4
        level5Btn.setBounds(400, 340, 240, 50); // Poziționăm butonul Nivel 5

        // Adăugăm butoanele pe panoul principal (după adăugarea imaginii de fundal)
        levelPanel.add(level1Btn);
        levelPanel.add(level2Btn);
        levelPanel.add(level3Btn);
        levelPanel.add(level4Btn);
        levelPanel.add(level5Btn);

        // Setăm acțiunile pentru butoane
        level1Btn.addActionListener(e -> {
            game.setState(GameState.LEVEL_1);  // Poți începe efectiv nivelul 1 aici
        });

        level2Btn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Nivel 2 blocat momentan!");
        });

        level3Btn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Nivel 3 blocat momentan!");
        });

        level4Btn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Nivel 4 blocat momentan!");
        });

        level5Btn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Nivel 5 blocat momentan!");
        });

        // Recalculăm și redimensionăm panoul după adăugarea butoanelor
        levelPanel.revalidate();
        levelPanel.repaint();
    }

    private JButton createImageButton(String imagePath, int width, int height, String text) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton button = new JButton(text, scaledIcon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setForeground(new Color(255, 215, 0)); // Auriu mai strălucitor
        button.setFont(new Font("Georgia", Font.BOLD, 28)); // Font îngroșat

        // Schimbări importante:
        button.setContentAreaFilled(true); // Activează umplerea
        button.setBackground(new Color(30, 30, 70, 200)); // Fundal semi-transparent
        button.setOpaque(true); // Face butonul opac

        button.setPreferredSize(new Dimension(width, height));
        button.setBorderPainted(true);
        button.setFocusPainted(true);

        return button;
    }

    public void show() {
        System.out.println("Afișez Selectarea Nivelului");
        JFrame frame = game.getWnd().getFrame();

        // Ascunde canvas-ul
        game.getWnd().GetCanvas().setVisible(false);

        // Configurează conținutul ferestrei
        frame.setContentPane(levelPanel); // Folosește setContentPane în loc de add

        // Forțează actualizarea
        frame.revalidate();
        frame.repaint();

        // Focus pe primul buton
        for(int i=1; i<=5; i++) {
            levelPanel.getComponent(i).requestFocusInWindow();
        }


    }
    public void hide() {
        levelPanel.setVisible(false);

    }

}
