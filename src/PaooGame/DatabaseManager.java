package PaooGame;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:game_progress.db";

    /// Constructor - creează tabelele necesare dacă nu există deja
    public DatabaseManager() {
        createTableIfNotExists();
    }

    /// Creează tabelele din baza de date dacă nu există deja
    private void createTableIfNotExists() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            //Tabela pentru scoruri pe nivel
            String sql1 = "CREATE TABLE IF NOT EXISTS LevelScores (" +
                    "level INTEGER PRIMARY KEY," +
                    "score INTEGER)";
            stmt.execute(sql1);

            // Tabela pentru progres (ultimul nivel jucat)
            String sql2 = "CREATE TABLE IF NOT EXISTS GameState (" +
                    "id INTEGER PRIMARY KEY CHECK(id = 1)," +  // un singur rând
                    "currentLevel INTEGER)";
            stmt.execute(sql2);

            // Tabel pentru scoruri finale (top 10 scoruri)
            String sql3 = "CREATE TABLE IF NOT EXISTS GameResults (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "playerName TEXT," +
                    "totalScore INTEGER," +
                    "totalStars INTEGER," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sql3);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /// Salvează scorul pentru un anumit nivel
    public void saveLevelScore(int level, int score) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "REPLACE INTO LevelScores (level, score) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, level);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /// Încarcă toate scorurile salvate pentru nivele
    public Map<Integer, Integer> loadAllScores() {
        Map<Integer, Integer> scores = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM LevelScores")) {
            while (rs.next()) {
                scores.put(rs.getInt("level"), rs.getInt("score"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

    /// Resetează scorurile salvate
    public void resetScores() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM LevelScores");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /// Salvează nivelul curent în tabela GameState
    public void saveCurrentLevel(int level) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "REPLACE INTO GameState (id, currentLevel) VALUES (1, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, level);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /// Încarcă nivelul curent din GameState
    public int loadCurrentLevel() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT currentLevel FROM GameState WHERE id = 1")) {
            if (rs.next()) {
                return rs.getInt("currentLevel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Default la 1 dacă nu e nimic salvat
    }

    /// Salvează un rezultat final în GameResults
    public void saveFinalResult(String name, int score, int stars) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // 1. Verificăm câte scoruri există deja
            String countSql = "SELECT COUNT(*) AS total FROM GameResults";
            int count = 0;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(countSql)) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }

            if (count < 10) {
                // Mai puțin de 10 scoruri -> inserează normal
                String insertSql = "INSERT INTO GameResults (playerName, totalScore, totalStars) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setString(1, name);
                    pstmt.setInt(2, score);
                    pstmt.setInt(3, stars);
                    pstmt.executeUpdate();
                }

            } else {
                // Sunt deja 10 scoruri -> verificăm cel mai mic
                String minSql = "SELECT id, totalScore FROM GameResults ORDER BY totalScore ASC, timestamp DESC LIMIT 1";
                int minId = -1;
                int minScore = 0;

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(minSql)) {
                    if (rs.next()) {
                        minId = rs.getInt("id");
                        minScore = rs.getInt("totalScore");
                    }
                }

                if (score > minScore) {
                    // Înlocuiește scorul cel mai mic
                    String updateSql = "UPDATE GameResults SET playerName = ?, totalScore = ?, totalStars = ?, timestamp = CURRENT_TIMESTAMP WHERE id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                        pstmt.setString(1, name);
                        pstmt.setInt(2, score);
                        pstmt.setInt(3, stars);
                        pstmt.setInt(4, minId);
                        pstmt.executeUpdate();
                    }
                } else {
                    System.out.println("Scorul " + score + " nu a intrat în top 10.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /// Returnează o listă cu top 10 scoruri
    public List<String[]> getTopScores() {
        List<String[]> topScores = new ArrayList<>();
        String sql = "SELECT playerName, totalScore, totalStars FROM GameResults ORDER BY totalScore DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("playerName");
                String score = String.valueOf(rs.getInt("totalScore"));
                String stars = String.valueOf(rs.getInt("totalStars"));
                topScores.add(new String[]{name, score, stars});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topScores;
    }

    /// Returnează suma totală a stelelor câștigate
    public int getTotalStarsFromDB() {
        int totalStars = 0;
        String sql = "SELECT SUM(stars) AS totalStars FROM LevelScores";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalStars = rs.getInt("totalStars");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalStars;
    }
}
