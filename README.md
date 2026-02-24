# 🎴 Flashcard Learning Game (JavaFX)

這是一個基於 **JavaFX + Maven** 開發的互動式閃卡學習遊戲，採用 **MVC + DAO** 設計模式，並整合 **MySQL** 資料庫。
專案模擬了類似網頁前端的卡片翻轉動畫與慶祝特效，適合用來學習外語單字（預設為法語/西班牙語）。

## ✨ 功能特色 (Features)

* **MVC 架構**：程式碼結構清晰，分離視圖、邏輯與資料存取。
* **互動式動畫**：
    * 3D 卡片翻轉效果 (Flip Animation)。
    * 學習完成後的紙屑慶祝特效 (Confetti Effect)。
* **資料庫整合**：使用 MySQL 儲存使用者帳號與單字卡片內容。
* **使用者系統**：包含註冊 (Register) 與登入 (Login) 功能。
* **進度追蹤**：即時顯示已記住的卡片數量與剩餘進度。

## 🛠️ 技術棧 (Tech Stack)

* **Language**: Java 17 (Compatible with Java 11)
* **Framework**: JavaFX
* **Build Tool**: Maven
* **Database**: MySQL 8.0
* **IDE**: Eclipse / IntelliJ IDEA

## 🚀 如何執行 (Getting Started)

### 1. 環境準備
請確保您的電腦已安裝：
* Java JDK 11 或更高版本
* Maven
* MySQL Server

### 2. 設定資料庫
請在 MySQL 中執行以下 SQL 指令以建立資料庫與預設資料：

```sql
CREATE DATABASE IF NOT EXISTS flashcard_db;
USE flashcard_db;

-- 建立使用者表
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

-- 建立卡片表
CREATE TABLE IF NOT EXISTS flashcards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    front_text VARCHAR(100),
    back_text VARCHAR(100),
    emoji VARCHAR(20)
);

-- 匯入預設單字 (法語範例)
INSERT INTO flashcards (front_text, back_text, emoji) VALUES 
('Bonjour', '你好 / 早安', '👋'),
('Bonsoir', '晚安 (見面時)', '🌆'),
('Bonne nuit', '晚安 (睡前)', '🌙'),
('Ça va?', '你好嗎？(隨意)', '😎'),
('Merci', '謝謝', '🙏'),
('De rien', '不客氣', '💁');

### 3. 修改資料庫連線
Java
private static final String USER = "root";    // 帳號
private static final String PASSWORD = "1234"; // 密碼

### 4. 啟動專案
使用 Maven 執行：
執行 Launcher.java (如果有建立) / App.java。