package PaooGame;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:game_progress.db";

    public DatabaseManager() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Tabela pentru scoruri
            String sql1 = "CREATE TABLE IF NOT EXISTS LevelScores (" +
                    "level INTEGER PRIMARY KEY," +
                    "score INTEGER)";
            stmt.execute(sql1);

            // Tabela pentru progres (ultimul nivel jucat)
            String sql2 = "CREATE TABLE IF NOT EXISTS GameState (" +
                    "id INTEGER PRIMARY KEY CHECK(id = 1)," +  // un singur rând
                    "currentLevel INTEGER)";
            stmt.execute(sql2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


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

    public void resetScores() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM LevelScores");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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


}
