package com.avenwu.rssreader.model;

public class EntryItem {
    private String id;
    private String title;
    private String summary;
    private String publised_time;
    private String updated_time;
    private AuthorInfo user;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublised_time() {
        return publised_time;
    }

    public void setPublised_time(String publised_time) {
        this.publised_time = publised_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public AuthorInfo getUser() {
        return user;
    }

    public void setUser(AuthorInfo user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
