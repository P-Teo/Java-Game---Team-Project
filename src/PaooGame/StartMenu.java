package PaooGame;

import java.util.Map;
import javax.swing.*;
import java.awt.*;

/**
 * Clasa StartMenu este responsabilă pentru afișarea meniului principal al jocului.
 * Oferă opțiuni precum: Joc Nou, Continuare, Scoruri, Informații și Ieșire.
 * Include un fundal personalizat, un titlu vizual atractiv și butoane interactive
 * care permit navigarea către diverse stări ale jocului.
 * Interacțiunea cu baza de date este folosită pentru a continua un joc salvat
 * sau pentru a încărca clasamentul cu cele mai bune scoruri.
 */

public class StartMenu {
    private Game game;
    private JPanel menuPanel;

    /// Constructor - primește instanța jocului și creează UI-ul
    public StartMenu(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game instance cannot be null.");
        }
        this.game = game;
        createUI(); // Inițializare UI
    }

    /// Creează interfața grafică a meniului
    private void createUI() {
        // Creăm un panou pentru meniul start
        menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setOpaque(false);   // Panou transparent

        /// 1. Adaugă imaginea de fundal redimensionată
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

        /// 2. Adăugăm textul peste imagine (centrat pe fundal)
        JLabel titleLabel1 = new JLabel("Prințesa Războinică și");
        titleLabel1.setForeground(new Color(255, 180, 20)); // Portocaliu auriu
        titleLabel1.setFont(new Font("Georgia", Font.BOLD, 28));

        JLabel titleLabel2 = new JLabel("Blestemul Regelui Întunecat");
        titleLabel2.setForeground(new Color(255, 180, 20)); // Portocaliu auriu
        titleLabel2.setFont(new Font("Georgia", Font.BOLD, 28));

        // Setăm textul pentru titlu
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


        /// 3. Panou pentru butoane (transparent)
        JPanel buttonPanel = new JPanel(new GridBagLayout());  // Folosim GridBagLayout pentru a alinia butoanele
        buttonPanel.setOpaque(false);

        // Setări pentru alinierea la dreapta
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Colona unde vor fi plasate butoanele
        gbc.gridy = 0; // Linia în care va apărea fiecare buton
        gbc.anchor = GridBagConstraints.NORTH; // Aliniere la dreapta
        gbc.insets = new Insets(15, 645, 10, 16);  // Margini între butoane

        //creăm butoane
        JButton newGameBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "JOC NOU");
        JButton continueBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "CONTINUARE");
        JButton scoresBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "SCORURI");
        JButton exitBtn = createImageButton("res/butoane/Untitled.png", 240, 50, "IEȘIRE");
        JButton infoBtn = createImageButton("res/butoane/Informatii.png", 50, 50);

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

        // Adăugăm panoul cu butoane peste fundal
        backgroundLabel.add(buttonPanel, BorderLayout.WEST);

        /// Funcționalitate butoane
        // Butonul de joc nou - resetează jocul
        newGameBtn.addActionListener(e -> {
            game.nrLevel = 1;
            game.setTotalScore (0);
            int[] stars = game.getStar();
            for (int i = 0; i < stars.length; i++) {
                stars[i] = 0;
            }
            game.reset();
            game.setState(GameState.LEVEL_SELECT);

        });

        // Buton continuare joc - încarcă nivelul și scorurile salvate
        continueBtn.addActionListener(e -> {
            int lastLevel = game.getDb().loadCurrentLevel(); // citește nivelul salvat

            if (lastLevel < 1 || lastLevel>=6) {
                JOptionPane.showMessageDialog(null, "Nu ai joc început. Începe joc nou!");
            } else {
                game.nrLevel = lastLevel; // setează nivelul curent
                Map<Integer, Integer> scores = game.getDb().loadAllScores(); // încarcă scorurile
                int totalScore = scores.values().stream().mapToInt(Integer::intValue).sum();
                game.setTotalScore(totalScore);
                game.setState(GameState.LEVEL_SELECT);
            }
        });

        // Buton scoruri - afișează top 10 scoruri într-o fereastră
        scoresBtn.addActionListener(e -> {
            DatabaseManager db = new DatabaseManager();
            java.util.List<String[]> topScores = db.getTopScores(); // Nume, scor, stele

            JPanel scorePanel = new JPanel();
            scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
            scorePanel.setBackground(Color.WHITE);
            scorePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            JLabel titleLabel = new JLabel("Top 10 Scoruri");
            titleLabel.setFont(new Font("Georgia", Font.BOLD, 20));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            scorePanel.add(titleLabel);
            scorePanel.add(Box.createVerticalStrut(10));

            if (topScores.isEmpty()) {
                JLabel emptyLabel = new JLabel("Nu există scoruri salvate.");
                emptyLabel.setFont(new Font("Georgia", Font.PLAIN, 14));
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                scorePanel.add(emptyLabel);
            } else {
                for (int i = 0; i < topScores.size(); i++) {
                    String[] row = topScores.get(i);
                    String nume = row[0];
                    String scor = row[1];
                    String stele = row[2];
                    JLabel label = new JLabel((i + 1) + ". " + nume + " - Scor: " + scor + " | Stele: " + stele);
                    label.setFont(new Font("Georgia", Font.PLAIN, 14));
                    scorePanel.add(label);
                    scorePanel.add(Box.createVerticalStrut(5));
                }
            }
            JOptionPane.showMessageDialog(null, scorePanel, "Clasament", JOptionPane.PLAIN_MESSAGE);
        });


        // Buton informații - afișează un mesaj cu autorii
        infoBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Joc dezvoltat de Papă Teodora și Maria Condurache\nVersiunea 1.0\nToate drepturile rezervate.");
        });

        // Buton ieșire - închide aplicația
        exitBtn.addActionListener(e -> System.exit(0));
    }

    /// Metodă auxiliară pentru crearea butonului fără text
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

    /// Metodă pentru butoane cu imagine și text
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

    /// Afișează meniul în fereastra principală
    public void show() {
        JFrame frame = game.getWnd().getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(menuPanel);
        frame.revalidate();
        frame.repaint();
         }

    /// Ascunde panoul meniului
    public void hide() {
        menuPanel.setVisible(false);}


}