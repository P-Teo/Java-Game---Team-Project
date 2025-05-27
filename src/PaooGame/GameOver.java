package PaooGame;

import javax.swing.*;
import java.awt.*;

public class GameOver {
    private Game game;
    private JPanel menuPanel;
    private JButton scoreBtn;


    public GameOver(Game game) {
        this.game = game;
        createUI();
    }

    private void createUI() {

        // Creăm un panou pentru meniul start
        menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setOpaque(false);   // Panou transparent

        // 1. Adaugă imaginea de fundal redimensionată
        String imagePath = "res/Game_Over.png";  // Calea fișierului de imagine
        ImageIcon originalIcon = new ImageIcon(imagePath);

        if (originalIcon.getIconWidth() == -1 || originalIcon.getIconHeight() == -1) {
            System.out.println("Imaginea nu a fost găsită sau nu a putut fi încărcată: " + imagePath);
        } else {
            System.out.println("Imaginea a fost încărcată cu succes: " + imagePath);
            // Aici poți continua cu redimensionarea și afișarea imaginii
        }

        // Redimensionăm imaginea la 1000x600
        Image scaledImage = originalIcon.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Creăm JLabel cu imaginea redimensionată
        JLabel backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setLayout(new BorderLayout());

        // Adaugă imaginea redimensionată ca fundal în fereastră
        menuPanel.add(backgroundLabel, BorderLayout.CENTER);



        // 2. Panou pentru butoane (transparent)
        JPanel buttonPanel = new JPanel(new GridBagLayout());  // Folosim GridBagLayout pentru a alinia butoanele
        buttonPanel.setOpaque(false);

        // Setări pentru alinierea la dreapta

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Colona unde vor fi plasate butoanele
        gbc.gridy = 200; // Linia în care va apărea fiecare buton
        gbc.anchor = GridBagConstraints.CENTER; // Aliniere la dreapta
      gbc.insets = new Insets(30, 500, 5, 100);  // Margini între butoane

        //butoane
        JButton mesajBtn = createImageButton("res/butoane/Untitled1.png", 340, 75, "AI PIERDUT...      ",0);
        scoreBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "SCOR: ",1);
        JButton homeBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "ACASĂ",1);
        JButton exitBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "IEȘIRE",1);

        // Adăugare butoane în panou
        gbc.gridy++;
        gbc.gridy++;
        buttonPanel.add(mesajBtn, gbc);
        gbc.gridy++;
        buttonPanel.add(scoreBtn, gbc);
        gbc.gridy++;
        buttonPanel.add(homeBtn, gbc);
        gbc.gridy++;
        buttonPanel.add(exitBtn, gbc);
        gbc.gridy++;

        backgroundLabel.add(buttonPanel, BorderLayout.EAST);



        // Funcționalitate
        homeBtn.addActionListener(e -> {
            game.setState(GameState.START_MENU); // Start joc nou

        });


        exitBtn.addActionListener(e -> System.exit(0));
    }

    //creeare butoane

    private JButton createImageButton(String imagePath, int width, int height, String text,int x) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton button = new JButton(text, scaledIcon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        // Chenar
        if(x==0)
        {
            button.setForeground(new Color(136, 0, 27));
            button.setFont(new Font("Georgia", Font.PLAIN, 30));
        }
        else
        {
            button.setForeground(new Color(255, 180, 20));
            button.setFont(new Font("Georgia", Font.PLAIN, 25));
        }

        button.setPreferredSize(new Dimension(width, height));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        return button;
    }

    public void show() {

        int scorTotal=game.getTotalScore();
        scoreBtn.setText("SCOR: " + scorTotal);

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