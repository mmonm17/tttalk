package com.t_t_talk.DB.Models;

import java.io.Serializable;
import java.util.List;

public class Phoneme implements Serializable {
    private String code;
    private List<String> sentences;
    private int starCount;

    public Phoneme(List<String> sentences, int starCount, String code) {
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

    public List<String> getSentences() {
        return sentences;
    }

    public void addSentence(String sentences) {
        this.sentences.add(sentences);
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }
}
