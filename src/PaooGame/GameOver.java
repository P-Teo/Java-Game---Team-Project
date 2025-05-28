package PaooGame;

import javax.swing.*;
import java.awt.*;


/**
 * Clasa GameOver este responsabilă pentru interfața grafică afișată atunci când jucătorul pierde jocul.
 * Afișează scorul final și butoane pentru acasă și ieșire
 */

public class GameOver {
    private Game game;
    private JPanel menuPanel;
    private JButton scoreBtn; //buton scor

    /// Constructor care primește instanța jocului pentru a avea acces la starea jocului
    public GameOver(Game game) {
        this.game = game;
        createUI(); // Inițializează interfața Game Over
    }

    /// Creează interfața grafică pentru ecranul de Game Over
    private void createUI() {
        // Creăm un panou principal pentru meniul Game Over
        menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setOpaque(false);  // Panoul este transparent pentru a permite afișarea fundalului

        /// 1. Adaugă imaginea de fundal redimensionată
        String imagePath = "res/Game_Over.png";  // Calea fișierului de imagine
        ImageIcon originalIcon = new ImageIcon(imagePath);
        if (originalIcon.getIconWidth() == -1 || originalIcon.getIconHeight() == -1) {
            System.out.println("Imaginea nu a fost găsită sau nu a putut fi încărcată: " + imagePath);
        } else {
            System.out.println("Imaginea a fost încărcată cu succes: " + imagePath);
        }

        // Redimensionăm imaginea la 1000x600
        Image scaledImage = originalIcon.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Creăm JLabel cu imaginea redimensionată
        JLabel backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setLayout(new BorderLayout());

        // Adaugă imaginea redimensionată ca fundal în fereastră
        menuPanel.add(backgroundLabel, BorderLayout.CENTER);

        /// 2. Panou pentru butoane (transparent)
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
        // Adaugă panoul de butoane peste fundal
        backgroundLabel.add(buttonPanel, BorderLayout.EAST);

        /// Funcționalitate
        // Acțiune pentru butonul "Acasă"
        homeBtn.addActionListener(e -> {
            game.setState(GameState.START_MENU); // Start joc nou
        });
        // Acțiune pentru butonul "Ieșire"
        exitBtn.addActionListener(e -> System.exit(0));
    }

    /// Metodă auxiliară pentru a crea un buton cu imagine de fundal și text suprapus
    private JButton createImageButton(String imagePath, int width, int height, String text,int x) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton button = new JButton(text, scaledIcon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);

        // Stilizarea butonului în funcție de tipul său
        if(x==0)
        {
            button.setForeground(new Color(136, 0, 27)); // culoare pentru mesajul principal
            button.setFont(new Font("Georgia", Font.PLAIN, 30));
        }
        else
        {
            button.setForeground(new Color(255, 180, 20)); // culoare pentru celelalte butoane
            button.setFont(new Font("Georgia", Font.PLAIN, 25));
        }

        // Setări suplimentare pentru transparență și stil
        button.setPreferredSize(new Dimension(width, height));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        return button;
    }

    /// Metodă care afișează ecranul Game Over și actualizează scorul
    public void show() {

        int scorTotal=game.getTotalScore(); // Obține scorul total al jucătorului
        scoreBtn.setText("SCOR: " + scorTotal); // Afișează scorul pe buton

        // Înlocuiește conținutul ferestrei cu meniul Game Over
        JFrame frame = game.getWnd().getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    /// Ascunde meniul Game Over
    public void hide() {
        menuPanel.setVisible(false);}

}