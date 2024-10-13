package com.t_t_talk.DataTypes;

public class Phoneme {
    private PhonemeCode code;
    private String[] sentences;
    private int starCount;

    public Phoneme(String[] sentences, int starCount, PhonemeCode code) {
        this.sentences = sentences;
        this.starCount = starCount;
        this.code = code;
    }

    public PhonemeCode getCode() {
        return code;
    }

    public void setCode(PhonemeCode code) {
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
