package com.t_t_talk.DataTypes;

// This class represents the information in the InformationActivity

public class Info {

    enum InfoType {
        INFO,
        STRATEGIES,
        PARTNERS
    }

    private String title;
    private String[] content;

    public Info(String title, String[] content) {
        this.title = title;
        this.content = content;
    }

    public Info(String title, String content) {
        this.title = title;
        this.content = new String[]{content};
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }
}
