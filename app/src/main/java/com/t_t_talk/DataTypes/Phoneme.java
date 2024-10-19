package com.t_t_talk.DataTypes;

import java.io.Serializable;

public class Phoneme implements Serializable {
    private String code;
    private String[] sentences;
    private int starCount;

    public Phoneme() {

    }

    public Phoneme(String[] sentences, int starCount, String code) {
        this.sentences = sentences;
        this.starCount = starCount;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getSentences() {
        return sentences;
    }

    public void setSentences(String[] sentences) {
        this.sentences = sentences;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }
}
