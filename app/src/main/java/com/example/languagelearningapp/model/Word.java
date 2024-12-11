package com.example.languagelearningapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words_table")
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String word;
    private String meaning;

    public Word(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getWord() { return word; }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() { return meaning; }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}

