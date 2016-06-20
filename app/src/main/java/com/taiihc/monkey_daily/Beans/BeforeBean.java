package com.taiihc.monkey_daily.Beans;

import java.util.List;


public class BeforeBean {
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<NewsEntry> getStories() {
        return stories;
    }

    public void setStories(List<NewsEntry> stories) {
        this.stories = stories;
    }

    private String date;
    private List<NewsEntry> stories;


}
