package com.flashcard.dao;

import com.flashcard.model.User;
import com.flashcard.util.DBConnection;
import java.sql.*;

public class UserDAO {
    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, "hashed_" + user.getUsername()); // 簡單模擬，實際應雜湊密碼
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // 這裡簡化驗證，實際應比對密碼
                return true; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
