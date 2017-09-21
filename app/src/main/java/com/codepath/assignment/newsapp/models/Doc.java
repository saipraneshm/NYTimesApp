package com.codepath.assignment.newsapp.models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Doc {

    @SerializedName("web_url")
    @Expose
    private String webUrl;
    @SerializedName("multimedia")
    @Expose
    private List<Multimedia> multimedia = null;
    @SerializedName("headline")
    @Expose
    private Headline headline;

    @SerializedName("pub_date")
    @Expose
    private String pubDate;

    @SerializedName("byline")
    @Expose
    private Byline byline;

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("new_desk")
    @Expose
    private String newDesk;

    @SerializedName("snippet")
    @Expose
    private String snippet;


    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<Multimedia> multimedia) {
        this.multimedia = multimedia;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Byline getByline() {
        return byline;
    }

    public void setByline(Byline byline) {
        this.byline = byline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewDesk() {
        return newDesk;
    }

    public void setNewDesk(String newDesk) {
        this.newDesk = newDesk;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
