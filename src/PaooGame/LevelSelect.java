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
        String imagePath = "res/Map1.png";  // Calea fișierului de imagine
        ImageIcon originalIcon = new ImageIcon(imagePath);

        // Redimensionăm imaginea
        Image scaledImage = originalIcon.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Creăm JLabel cu imaginea redimensionată
        JLabel backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setBounds(0, 0, 1000, 600);  // Setăm poziția și dimensiunile imaginii


        // Creăm butoane pentru fiecare nivel
        JButton acasaBtn = createImageButton("res/butoane/Acasa.png", 70, 70);
        JButton scorBtn = createImageButton("res/butoane/Scor.png", 60, 60);
        JButton steleBtn = createImageButton("res/butoane/Stele.png", 60, 60);
        JButton level1Btn = createImageButton("res/butoane/Nivel1.png", 100, 50, "1");
        JButton level2Btn = createImageButton("res/butoane/Nivel1.png", 100, 50, "2");
        JButton level3Btn = createImageButton("res/butoane/Nivel1.png", 100, 50, "3");
        JButton level4Btn = createImageButton("res/butoane/Nivel1.png", 100, 50, "4");
        JButton level5Btn = createImageButton("res/butoane/Nivel1.png", 100, 50, "5");
        JButton setariBtn = createImageButton("res/butoane/Setari.png", 45, 45);

        // Setăm pozițiile butoanelor
        acasaBtn.setBounds(0, 0, 240, 140);
        scorBtn.setBounds(600, 30, 140, 80);
        steleBtn.setBounds(750, 30, 140, 80);
        level1Btn.setBounds(210, 90, 140, 80); // Poziționăm butonul Nivel 1
        level2Btn.setBounds(400, 160, 140, 80); // Poziționăm butonul Nivel 2
        level3Btn.setBounds(250, 270, 140, 80); // Poziționăm butonul Nivel 3
        level4Btn.setBounds(500, 280, 140, 80); // Poziționăm butonul Nivel 4
        level5Btn.setBounds(700, 330, 140, 80); // Poziționăm butonul Nivel 5
        setariBtn.setBounds(20, 490, 140, 80);

        // Adăugăm butoanele pe panoul principal (după adăugarea imaginii de fundal)
        levelPanel.add(acasaBtn);
        levelPanel.add(scorBtn);
        levelPanel.add(steleBtn);
        levelPanel.add(level1Btn);
        levelPanel.add(level2Btn);
        levelPanel.add(level3Btn);
        levelPanel.add(level4Btn);
        levelPanel.add(level5Btn);
        levelPanel.add(setariBtn);

        // Adăugăm imaginea ca fundal
        levelPanel.add(backgroundLabel);


        int[] star= game.getStar();
        acasaBtn.addActionListener(e -> {
            game.setState(GameState.START_MENU); // Start joc nou

        });

        scorBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, String.valueOf(game.getTotalScore())); // Start joc nou

        });

        steleBtn.addActionListener(e -> {
            int sum = 0;
            for (int i = 1; i <= 6; i++) {
                sum += star[i];
            }
            String mesaj = "<html><body style='font-size:20px;'>" + sum + "   ⭐</body></html>";
            JOptionPane.showMessageDialog(null, mesaj);

        });


        // Setăm acțiunile pentru butoane
        level1Btn.addActionListener(e -> {
            if(game.nrLevel==1) {
                game.setState(GameState.LEVEL_1);
            }
            else {
                String nrStar = "⭐".repeat(star[1]);
                String mesaj = "<html><body style='font-size:20px;'>Nivel 1 câștigat!<br>" + nrStar + "</body></html>";
                JOptionPane.showMessageDialog(null, mesaj);

            }
        });

        level2Btn.addActionListener(e -> {
            if(game.nrLevel==2) {
                game.setState(GameState.LEVEL_2);
            }
            else
            if(game.nrLevel>2){
                String nrStar = "⭐".repeat(star[2]);
                String mesaj = "<html><body style='font-size:20px;'>Nivel 2 câștigat!<br>" + nrStar + "</body></html>";
                JOptionPane.showMessageDialog(null, mesaj);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Nivel 2 blocat momentan!");
            }
        });

        level3Btn.addActionListener(e -> {
            if(game.nrLevel==3) {
                game.setState(GameState.LEVEL_3);
            }
            else
            if(game.nrLevel>3){
                String nrStar = "⭐".repeat(star[3]);
                String mesaj = "<html><body style='font-size:20px;'>Nivel 3 câștigat!<br>" + nrStar + "</body></html>";
                JOptionPane.showMessageDialog(null, mesaj);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Nivel 3 blocat momentan!");
            }
        });

        level4Btn.addActionListener(e -> {
            if(game.nrLevel==4) {
                game.setState(GameState.LEVEL_4);
            }
            else
            if(game.nrLevel>4){
                String nrStar = "⭐".repeat(star[4]);
                String mesaj = "<html><body style='font-size:20px;'>Nivel 4 câștigat!<br>" + nrStar + "</body></html>";
                JOptionPane.showMessageDialog(null, mesaj);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Nivel 4 blocat momentan!");
            }
        });

        level5Btn.addActionListener(e -> {
            if(game.nrLevel==5) {
                game.setState(GameState.LEVEL_5);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Nivel 5 blocat momentan!");
            }
        });

        // Recalculăm și redimensionăm panoul după adăugarea butoanelor
        levelPanel.revalidate();
        levelPanel.repaint();
    }

    private JButton createImageButton(String path, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton button = new JButton(scaledIcon);
        button.setPreferredSize(new Dimension(width, height));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        return button;
    }

    private JButton createImageButton(String imagePath, int width, int height, String text) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton button = new JButton(text, scaledIcon);
        button.setPreferredSize(new Dimension(width, height));

        button.setText(text);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.NORTH);
        button.setIconTextGap(-20);
        button.setForeground(new Color(255, 215, 0)); // Auriu mai strălucitor
        button.setFont(new Font("Georgia", Font.BOLD, 18)); // Font îngroșat

        // Schimbări importante:
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false); // NU vrei să deseneze fundal


        button.setPreferredSize(new Dimension(width, height));

        return button;
    }

    public void show() {
        System.out.println("Afișez Selectarea Nivelului");
        JFrame frame = game.getWnd().getFrame();
        frame.getContentPane().removeAll();
        // Ascunde canvas-ul
      //  game.getWnd().GetCanvas().setVisible(false);



        frame.getContentPane().add(levelPanel);// Folosește setContentPane în loc de add

        // Forțează actualizarea
        frame.revalidate();
        frame.repaint();

        // Focus pe primul buton
        for (int i = 1; i < levelPanel.getComponentCount() && i <= 5; i++) {
            levelPanel.getComponent(i).requestFocusInWindow();
        }



    }
    public void hide() {
        levelPanel.setVisible(false);

    }
    public JPanel getPanel() {
        return levelPanel;
    }


}
