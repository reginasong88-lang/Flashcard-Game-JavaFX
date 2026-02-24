package com.flashcard.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/flashcard_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // 請修改為您的 MySQL 帳號
    private static final String PASSWORD = "1234"; // 請修改為您的 MySQL 密碼

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}