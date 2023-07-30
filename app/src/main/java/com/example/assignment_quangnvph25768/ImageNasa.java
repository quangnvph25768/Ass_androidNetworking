package com.example.assignment_quangnvph25768;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ImageNasa {

    @SerializedName("title")
    private String title;
    @SerializedName("explanation")
    private String explanation;
    @SerializedName("copyright")
    private String copyright;
    @SerializedName("url")
    private String url;
    @SerializedName("date")
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getUrl() {
        return  url;
    }

    public void setUrl(String url) {
        this.url =url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
