package PaooGame;

import javax.swing.*;
import java.awt.*;

public class GameWin {
    private Game game;
    private JPanel menuPanel;

    public GameWin(Game game) {
        this.game = game;
        createUI();
    }

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

        // === JOS: butoane separate în stânga și dreapta ===
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        // Stânga jos: scor și stele
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        JButton scoreBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "SCOR: ", 1);
        JButton starBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "STELE: ", 1);
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

        // Funcționalitate butoane
        homeBtn.addActionListener(e -> game.setState(GameState.START_MENU));
        exitBtn.addActionListener(e -> System.exit(0));
    }

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

    public void show() {
        JFrame frame = game.getWnd().getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    public void hide() {
        menuPanel.setVisible(false);
    }
}
