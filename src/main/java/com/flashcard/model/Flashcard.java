package com.flashcard.model;

public class Flashcard {
    private int id;
    private String frontText;
    private String backText;
    private String emoji;

    public Flashcard(int id, String frontText, String backText, String emoji) {
        this.id = id;
        this.frontText = frontText;
        this.backText = backText;
        this.emoji = emoji;
    }

    public String getFrontText() { return frontText; }
    public String getBackText() { return backText; }
    public String getEmoji() { return emoji; }
}
