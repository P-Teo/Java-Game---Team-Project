package PaooGame;

import javax.swing.*;
import java.awt.*;

public class StartMenu {
    private Game game;
    private JPanel menuPanel;

    public StartMenu(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game instance cannot be null.");
        }
        this.game = game;
        createUI();
    }

    private void createUI() {

        // Creăm un panou pentru meniul start
        menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setOpaque(false);   // Panou transparent

        // 1. Adaugă imaginea de fundal redimensionată
        String imagePath = "res/Start_Menu.png";  // Calea fișierului de imagine
        ImageIcon originalIcon = new ImageIcon(imagePath);

        // Redimensionăm imaginea la 1000x600
        Image scaledImage = originalIcon.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Creăm JLabel cu imaginea redimensionată
        JLabel backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setLayout(new BorderLayout());

        // Adaugă imaginea redimensionată ca fundal în fereastră
        menuPanel.add(backgroundLabel, BorderLayout.CENTER);

        //setam scris

        // 2. Adăugăm textul peste imagine (centrat pe fundal)
        JLabel titleLabel1 = new JLabel("Prințesa Războinică și");
        titleLabel1.setForeground(new Color(255, 180, 20)); // Portocaliu auriu
        titleLabel1.setFont(new Font("Georgia", Font.BOLD, 28));

        JLabel titleLabel2 = new JLabel("Blestemul Regelui Întunecat");
        titleLabel2.setForeground(new Color(255, 180, 20)); // Portocaliu auriu
        titleLabel2.setFont(new Font("Georgia", Font.BOLD, 28));

// 2. Setăm textul pentru titlu
        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setOpaque(false); // Transparent

// Configurare GridBagConstraints pentru centrare
        GridBagConstraints gbcx1 = new GridBagConstraints();
        gbcx1.gridwidth = GridBagConstraints.REMAINDER;
        gbcx1.anchor = GridBagConstraints.CENTER;
        gbcx1.insets = new Insets(45, 0, 5, 450); // Spațiu între linii

        textPanel.add(titleLabel1, gbcx1);


        GridBagConstraints gbcx2 = new GridBagConstraints();
        gbcx2.gridwidth = GridBagConstraints.REMAINDER;
        gbcx2.anchor = GridBagConstraints.CENTER;
        gbcx2.insets = new Insets(10, 0, 0, 460); // Spațiu între linii

        textPanel.add(titleLabel2, gbcx2);

// Adăugăm panoul cu text peste fundal

        backgroundLabel.add(textPanel, BorderLayout.NORTH);


        // 2. Panou pentru butoane (transparent)
        JPanel buttonPanel = new JPanel(new GridBagLayout());  // Folosim GridBagLayout pentru a alinia butoanele
        buttonPanel.setOpaque(false);

        // Setări pentru alinierea la dreapta

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Colona unde vor fi plasate butoanele
        gbc.gridy = 0; // Linia în care va apărea fiecare buton
        gbc.anchor = GridBagConstraints.NORTH; // Aliniere la dreapta
        gbc.insets = new Insets(15, 645, 10, 16);  // Margini între butoane

        //butoane
        JButton newGameBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "JOC NOU");
        JButton continueBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "CONTINUARE");
        JButton scoresBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "SCORURI");
        JButton exitBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "IEȘIRE");
        JButton infoBtn = createImageButton("res/butoane/Informatii.png", 50, 50);
        /// JButton settingsBtn = createImageButton("butoane/Setari.png",50,50);


        // Adăugare butoane în panou
        buttonPanel.add(newGameBtn, gbc);
        gbc.gridy++;  // Mergem la următoarea linie
        buttonPanel.add(continueBtn, gbc);
        gbc.gridy++;
        buttonPanel.add(scoresBtn, gbc);
        gbc.gridy++;
        buttonPanel.add(exitBtn, gbc);
        gbc.gridy++;
        gbc.weighty = 1;
        buttonPanel.add(infoBtn, gbc);
        gbc.gridy++;

        backgroundLabel.add(buttonPanel, BorderLayout.WEST);

        // Funcționalitate
        newGameBtn.addActionListener(e -> {
            game.setState(GameState.LEVEL_SELECT); // Start joc nou

        });

        continueBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Funcția 'Continuare' nu e implementată încă.");
        });

        scoresBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Scorurile sunt goale momentan.");
        });


        infoBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Joc dezvoltat de Papă Teodora și Maria Condurache\nVersiunea 1.0\nToate drepturile rezervate.");
        });

        exitBtn.addActionListener(e -> System.exit(0));
    }

    //creeare butoane
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

    //resize imagine
    private JButton createImageButton(String imagePath, int width, int height, String text) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton button = new JButton(text, scaledIcon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setForeground(new Color(255, 180, 20)); // Portocaliu
        button.setFont(new Font("Georgia", Font.PLAIN, 28)); // Font îngroșat
        button.setPreferredSize(new Dimension(width, height));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        return button;
    }

    public void show() {
        JFrame frame = game.getWnd().getFrame(); // You'll need to add this method to GameWindow
        frame.getContentPane().removeAll();
        frame.getContentPane().add(menuPanel);
        frame.revalidate();
        frame.repaint();
         }
    public void hide() {
        menuPanel.setVisible(false); // Ascunde panoul meniului
    }


}