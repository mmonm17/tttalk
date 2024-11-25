package com.t_t_talk.DB.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable {
    private int levelNumber;
    private int age;
    private int color;
    private String language;
    private ArrayList<Phoneme> phonemeList;

    public Level(int levelNumber, int age, int color, String language, ArrayList<Phoneme> phonemeList) {
        this.levelNumber = levelNumber;
        this.age = age;
        this.color = color;
        this.language = language;
        this.phonemeList = phonemeList;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public ArrayList<Phoneme> getPhonemeList() {
        return phonemeList;
    }

    public int getAge() {
        return age;
    }

    public int getColor() {
        return color;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getCode() {
        return this.language + "-" + String.valueOf(this.levelNumber);
    }

    public String toString() {
        return "Level " + this.levelNumber + " - " + this.language + " - " + this.age + " years old" + " - " + this.color + " - " + this.phonemeList.size() + " phonemes";
    }
}
