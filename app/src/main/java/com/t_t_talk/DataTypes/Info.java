package com.t_t_talk.DataTypes;



public class Info {

    enum InfoType {
        INFO,
        STRATEGIES,
        PARTNERS
    }

    //private InfoType type;
    private String title;
    private String[] content;

//    public Info(InfoType type, String title, String[] content) {
//        this.title = title;
//        this.content = content;
//        this.type = type;
//    }

//    public InfoType getType() {
//        return type;
//    }
//
//    public void setType(InfoType type) {
//        this.type = type;
//    }

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
