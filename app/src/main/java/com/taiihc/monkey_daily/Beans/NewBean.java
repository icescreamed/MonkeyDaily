package com.taiihc.monkey_daily.Beans;

import java.util.List;


public class NewBean {
    private String date;
    private List<NewsEntry> stories;
    private List<TopNewsEntry> top_stories;
    public List<TopNewsEntry> getTop_stories() {
        return top_stories;
    }
    public void setTop_stories(List<TopNewsEntry> top_stories) {
        this.top_stories = top_stories;
    }
    public List<NewsEntry> getStories() {
        return stories;
    }
    public void setStories(List<NewsEntry> stories) {
        this.stories = stories;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }


}
