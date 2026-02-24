package com.flashcard.dao;

import com.flashcard.model.Flashcard;
import com.flashcard.util.DBConnection;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class FlashcardDAO {
    public List<Flashcard> getAllCards() {
        List<Flashcard> cards = new ArrayList<>();
        String sql = "SELECT * FROM flashcards";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cards.add(new Flashcard(
                    rs.getInt("id"),
                    rs.getString("front_text"),
                    rs.getString("back_text"),
                    rs.getString("emoji")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }
}