package PaooGame;

import javax.swing.*;
import java.awt.*;

/**
 * Clasa GameWin este responsabilă pentru interfața grafică afișată atunci când jucătorul câștigă jocul.
 * Afișează scorul final, numărul de stele obținut, permite introducerea numelui jucătorului și salvarea rezultatului.
 */

public class GameWin {
    private Game game;
    private JPanel menuPanel;
    private JButton scoreBtn;   //buton scor
    private JButton starBtn;    //buton stele

    /// Constructor
    public GameWin(Game game) {
        this.game = game;
        createUI(); // Inițializează interfața
    }

    /// Creează și configurează interfața grafică pentru ecranul de câștig.
    private void createUI() {
        menuPanel = new JPanel(new BorderLayout());
        menuPanel.setOpaque(false);

        // Imagine de fundal
        String imagePath = "res/Game_Win.png";
        ImageIcon originalIcon = new ImageIcon(imagePath);
        if (originalIcon.getIconWidth() == -1 || originalIcon.getIconHeight() == -1) {
            System.out.println("Imaginea nu a fost găsită: " + imagePath);
        }

        Image scaledImage = originalIcon.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setLayout(new BorderLayout());
        menuPanel.add(backgroundLabel, BorderLayout.CENTER);

        // === SUS: "AI CÂȘTIGAT!" centrat ===
        JButton mesajBtn = createImageButton("res/butoane/Untitled3.png", 340, 75, "AI CÂȘTIGAT!", 0);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);
        topPanel.add(mesajBtn);
        backgroundLabel.add(topPanel, BorderLayout.NORTH);

        // === JOS: butoane separate în stânga și dreapta (scor, stele, acasă, ieșire) ===
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        // Stânga jos: scor și stele
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        scoreBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "SCOR: " , 1);
        starBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "STELE: " , 1);
        leftPanel.add(scoreBtn);
        leftPanel.add(Box.createVerticalStrut(15)); // spațiu între butoane
        leftPanel.add(starBtn);

        // Dreapta jos: acasă și ieșire
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        JButton homeBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "ACASĂ", 1);
        JButton exitBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "IEȘIRE", 1);
        rightPanel.add(homeBtn);
        rightPanel.add(Box.createVerticalStrut(15)); // spațiu între butoane
        rightPanel.add(exitBtn);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30)); // margini

        backgroundLabel.add(bottomPanel, BorderLayout.SOUTH);

        // === Panou central pentru introducerea numelui ===
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new GridBagLayout());
        namePanel.setOpaque(true);
        namePanel.setBackground(new Color(0, 0, 0, 180)); // fundal semi-transparent
        namePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        GridBagConstraints nameGbc = new GridBagConstraints();
        nameGbc.insets = new Insets(10, 10, 10, 10);
        nameGbc.gridx = 0;
        nameGbc.gridy = 0;

        JLabel nameLabel = new JLabel("Nume jucător:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        namePanel.add(nameLabel, nameGbc);

        nameGbc.gridy++;
        JTextField nameField = new JTextField(15);
        nameField.setFont(new Font("Georgia", Font.PLAIN, 22));
        namePanel.add(nameField, nameGbc);

        nameGbc.gridy++;
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Georgia", Font.BOLD, 22));
        okButton.setFocusPainted(false);
        namePanel.add(okButton, nameGbc);

        // Adaugă acest panou în centrul fundalului
        backgroundLabel.add(namePanel, BorderLayout.CENTER);

        // Acțiune la apăsarea OK
        okButton.addActionListener(e -> {
            String playerName = nameField.getText().trim();
            if (!playerName.isEmpty()) {
                game.setPlayerName(playerName);
                System.out.println("Nume introdus și setat: " + playerName);
                int totalScore = game.getTotalScore();
                int[] star = game.getStar();
                // Salvează în baza de date
                int totalStars = 0;
                for (int i = 1; i <= 6; i++) {
                    totalStars += star[i];
                }
                game.getDb().saveFinalResult(game.getPlayerName(), totalScore, totalStars);

                namePanel.setVisible(false); // Ascunde chenarul
            } else {
                JOptionPane.showMessageDialog(null, "Introduceți un nume valid!", "Eroare", JOptionPane.WARNING_MESSAGE);
            }

        });


        /// Funcționalitate butoane
        homeBtn.addActionListener(e -> game.setState(GameState.START_MENU));
        exitBtn.addActionListener(e -> System.exit(0));

    }

    ///Creează un buton cu imagine, text și stil personalizat.
    private JButton createImageButton(String imagePath, int width, int height, String text, int x) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton button = new JButton(text, scaledIcon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);

        if (x == 0) {
            button.setForeground(new Color(0, 102, 0)); // Verde închis
            button.setFont(new Font("Georgia", Font.BOLD, 30));
        } else {
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

    /// Afișează panoul GameWin pe ecran și actualizează scorul și stelele.
    public void show() {
        int scorTotal=game.getTotalScore();
        scoreBtn.setText("SCOR: " + scorTotal);
        int[] star= game.getStar();
        int steleTotal = 0;
        for (int i = 1; i <= 6; i++) {
            steleTotal += star[i];
        }
        starBtn.setText("STELE: " + steleTotal);

        // Înlocuiește conținutul ferestrei cu meniul GameWin
        JFrame frame = game.getWnd().getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    /// Ascunde panoul GameWin
    public void hide() {
        menuPanel.setVisible(false);
    }
}
