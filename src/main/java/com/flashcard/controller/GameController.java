package com.flashcard.controller;

import com.flashcard.App;


import com.flashcard.dao.FlashcardDAO;
import com.flashcard.model.Flashcard;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import java.util.Random;

import javafx.util.Duration;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameController {
    @FXML private Label progressLabel;
    @FXML private Label knownCountLabel;
    @FXML private Label remainingCountLabel;
    
    // 卡片相關元件
    @FXML private StackPane cardContainer;
    @FXML private VBox cardFace; // 卡片內容容器
    @FXML private Label emojiLabel;
    @FXML private Label mainTextLabel;
    @FXML private Label hintLabel;
    
    @FXML private Pane confettiLayer; // 對應在 FXML 加的特效層
    private Random random = new Random();

    @FXML private Button btnPrev;
    @FXML private Button btnNext;

    private List<Flashcard> cards;
    private int currentIndex = 0;
    private boolean isFlipped = false;
    private Set<Integer> knownCards = new HashSet<>();
    private FlashcardDAO flashcardDAO = new FlashcardDAO();

    @FXML
    public void initialize() {
        cards = flashcardDAO.getAllCards();
        updateUI();
    }

 // ★ 新增這個產生紙屑的方法
    private void spawnConfetti1() {
        // 每次產生 30 片紙屑
        for (int i = 0; i < 30; i++) {
            // 1. 建立小方塊
            Rectangle rect = new Rectangle(8, 8);
            
            // 隨機顏色 (紅、黃、綠、紫、藍)
            Color[] colors = {
                Color.web("#e94560"), Color.web("#fbbf24"), 
                Color.web("#28a745"), Color.web("#533483"), Color.web("#ff6b6b")
            };
            rect.setFill(colors[random.nextInt(colors.length)]);
            
            // 2. 設定起始位置 (隨機寬度，高度從畫面外上方 -20 開始)
            double startX = random.nextDouble() * confettiLayer.getWidth();
            rect.setTranslateX(startX);
            rect.setTranslateY(-20);
            
            // 加到畫面上
            confettiLayer.getChildren().add(rect);

            // 3. 建立動畫
            // 掉落動畫 (時間 1~2秒隨機)
            TranslateTransition fall = new TranslateTransition(Duration.seconds(1 + random.nextDouble()), rect);
            fall.setByY(confettiLayer.getHeight() + 100); // 掉到底部
            
            // 旋轉動畫
            RotateTransition spin = new RotateTransition(Duration.seconds(0.5 + random.nextDouble()), rect);
            spin.setByAngle(360); // 轉一圈
            spin.setCycleCount(4); // 轉好幾次

            // 4. 同時執行掉落和旋轉
            ParallelTransition pt = new ParallelTransition(fall, spin);
            
            // 動畫結束後，把紙屑從畫面上移除 (避免記憶體佔用)
            pt.setOnFinished(e -> confettiLayer.getChildren().remove(rect));
            pt.play();
        }
    }
    
    
    private void updateUI() {
        if (cards.isEmpty()) return;

        Flashcard card = cards.get(currentIndex);
        
        // 如果是背面，先翻回來再更新文字，避免穿幫 (這裡簡化直接更新)
        isFlipped = false;
        setCardStyle(false); 
        
        emojiLabel.setText(card.getEmoji());
        mainTextLabel.setText(card.getFrontText());
        hintLabel.setText("點擊翻轉查看答案");

        progressLabel.setText("卡片 " + (currentIndex + 1) + " / " + cards.size());
        knownCountLabel.setText(String.valueOf(knownCards.size()));
        remainingCountLabel.setText(String.valueOf(cards.size() - knownCards.size()));

        btnPrev.setDisable(currentIndex == 0);
        btnNext.setDisable(currentIndex == cards.size() - 1);
    }

    @FXML
    private void handleFlip() {
        RotateTransition rotator = new RotateTransition(Duration.millis(300), cardContainer);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(90);
        
        rotator.setOnFinished(e -> {
            isFlipped = !isFlipped;
            Flashcard card = cards.get(currentIndex);
            
            if (isFlipped) {
                // 顯示背面
                setCardStyle(true);
                emojiLabel.setText("💡");
                mainTextLabel.setText(card.getBackText());
                hintLabel.setText("點擊再次翻轉");
            } else {
                // 顯示正面
                setCardStyle(false);
                emojiLabel.setText(card.getEmoji());
                mainTextLabel.setText(card.getFrontText());
                hintLabel.setText("點擊翻轉查看答案");
            }

            // 完成後半段旋轉
            cardContainer.setRotate(270);
            RotateTransition rotator2 = new RotateTransition(Duration.millis(300), cardContainer);
            rotator2.setAxis(Rotate.Y_AXIS);
            rotator2.setFromAngle(270);
            rotator2.setToAngle(360);
            rotator2.play();
        });
        
        rotator.play();
    }

    private void setCardStyle(boolean isBack) {
        cardFace.getStyleClass().removeAll("card-front", "card-back");
        cardFace.getStyleClass().add(isBack ? "card-back" : "card-front");
    }

    @FXML
    private void handleNext() {
        if (currentIndex < cards.size() - 1) {
            currentIndex++;
            updateUI();
        }
    }

    @FXML
    private void handlePrev() {
        if (currentIndex > 0) {
            currentIndex--;
            updateUI();
        }
    }

    @FXML
    private void handleMarkKnown() {
        if (!knownCards.contains(currentIndex)) {
            knownCards.add(currentIndex);
            
            // ★ 呼叫紙屑特效！
            spawnConfetti1();

            // 更新數字
            updateUI();
            
            // 如果還有下一張，稍微延遲 0.6 秒再切換，讓使用者欣賞一下動畫
            if (currentIndex < cards.size() - 1) {
                 new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(() -> handleNext());
                        }
                    }, 
                    600 // 延遲 600 毫秒
                );
            }
        }
    }
    
    @FXML
    private void handleLogout() throws IOException {
        App.setRoot("login");
    }


private void spawnConfetti() {
    // 產生 30 片紙屑
    for (int i = 0; i < 30; i++) {
        // 1. 建立紙屑 (小方塊)
        Rectangle rect = new Rectangle(8, 8);
        // 隨機顏色 (紅、黃、綠、紫、藍)
        Color[] colors = {
            Color.web("#e94560"), Color.web("#fbbf24"), 
            Color.web("#28a745"), Color.web("#533483"), Color.web("#ff6b6b")
        };
        rect.setFill(colors[random.nextInt(colors.length)]);

        // 2. 隨機起始位置 (在畫面寬度內隨機，高度從上方 -20 開始)
        double startX = random.nextDouble() * confettiLayer.getWidth();
        rect.setTranslateX(startX);
        rect.setTranslateY(-20);

        confettiLayer.getChildren().add(rect);

        // 3. 掉落動畫
        // 隨機掉落時間 (1~2秒)
        TranslateTransition fall = new TranslateTransition(Duration.seconds(1 + random.nextDouble()), rect);
        fall.setByY(confettiLayer.getHeight() + 100); // 掉落到底部

        // 4. 旋轉動畫
        RotateTransition spin = new RotateTransition(Duration.seconds(0.5 + random.nextDouble()), rect);
        spin.setByAngle(360);
        spin.setCycleCount(4); // 轉好幾圈

        // 5. 同時執行並在結束後移除
        ParallelTransition pt = new ParallelTransition(fall, spin);
        pt.setOnFinished(e -> confettiLayer.getChildren().remove(rect));
        pt.play();
    }
}}
