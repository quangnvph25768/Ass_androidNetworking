package com.example.assignment_quangnvph25768.model;
import com.google.gson.annotations.SerializedName;

public class ImageInfoRequest {
    @SerializedName("title")
    private String title;

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("copyright")
    private String copyright;

    public ImageInfoRequest(String title, String explanation, String copyright) {
        this.title = title;
        this.explanation = explanation;
        this.copyright = copyright;
    }

    public ImageInfoRequest() {

    }

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
}
