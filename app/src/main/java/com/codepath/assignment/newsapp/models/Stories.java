package com.codepath.assignment.newsapp.models;

import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Stories {
    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("copyright")
    @Expose
    private String copyright;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public List<NewsStory> getNewsStories() {
        List<NewsStory> newsStories = new ArrayList<>();
        List<Doc> docs = getResponse().getDocs();
        for (Doc doc : docs) {
            NewsStory newsStory = new NewsStory();
            if (doc.getMultimedia().size() > 0) {
                String thumbnailUrl = getThumbnailImageUrl(doc.getMultimedia());
                if (thumbnailUrl != null) {
                    newsStory.setThumbnailUrl(thumbnailUrl);
                }
            }

            if (doc.getByline() != null)
                newsStory.setByline(doc.getByline().getOriginal());
            if (doc.getHeadline() != null)
                newsStory.setHeadline(doc.getHeadline().getMain());
            /*if (response.getMeta() != null) {
                newsStory.setHits(response.getMeta().getHits());
            }*/
            if(doc.getNewDesk() != null){
                if(doc.getNewDesk().equals("None")){
                    newsStory.setNewsDesk(null);
                }else{
                    newsStory.setNewsDesk(doc.getNewDesk());
                }

            }
            if(doc.getSnippet() != null){
                newsStory.setBriefStory(doc.getSnippet());
            }
            newsStory.setWebUrl(doc.getWebUrl());
            if (doc.getPubDate() != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ",
                        Locale.getDefault());
                Date pubDate = null;
                try {
                    pubDate = formatter.parse(doc.getPubDate());
                    String date = DateFormat.format("EEE, MMM dd yyyy", pubDate).toString();
                    newsStory.setPubDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'",
                            Locale.getDefault());
                    try {
                        pubDate = formatter2.parse(doc.getPubDate());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    String date = DateFormat.format("EEE, MMM dd yyyy", pubDate).toString();
                    newsStory.setPubDate(date);
                }
            } else {
                newsStory.setPubDate("Not available");
            }
            newsStories.add(newsStory);
            Log.d("RESPONSE",newsStory.toString());
        }
        return newsStories;
    }

    private static String getThumbnailImageUrl(List<Multimedia> multimedias){
        for(Multimedia multimedia : multimedias){
            if(multimedia != null)
                return "http://www.nytimes.com/" + multimedia.getUrl();
        }
        return null;
    }

}
