package com.t_t_talk.DataTypes;

import java.util.ArrayList;

public class Level {
    private int levelNumber;
    private int age;
    private int color;
    private ArrayList<Phoneme> phonemeList;

    public Level(int levelNumber, int age, int color, ArrayList<Phoneme> phonemeList) {
        this.levelNumber = levelNumber;
        this.age = age;
        this.color = color;
        this.phonemeList = phonemeList;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public ArrayList<Phoneme> getPhonemeList() {
        return phonemeList;
    }

    public void setPhonemeList(ArrayList<Phoneme> phonemeList) {
        this.phonemeList = phonemeList;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
