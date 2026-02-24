package com.flashcard.controller;

import com.flashcard.App;
import com.flashcard.dao.UserDAO;
import com.flashcard.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() throws IOException {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (userDAO.login(user, pass)) {
            App.setRoot("game"); // 切換到遊戲頁面
        } else {
            messageLabel.setText("登入失敗，請檢查帳號");
        }
    }

    @FXML
    private void handleRegister() {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        if (!user.isEmpty() && !pass.isEmpty()) {
            if (userDAO.register(new User(user, pass))) {
                messageLabel.setText("註冊成功！請登入");
            } else {
                messageLabel.setText("註冊失敗 (帳號可能已存在)");
            }
        } else {
            messageLabel.setText("請輸入帳號密碼");
        }
    }
}
