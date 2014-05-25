package com.avenwu.ereader.model;

import com.j256.ormlite.field.DatabaseField;

public class BaseDetailItem {
    @DatabaseField(id = true)
    private String feed_id;
    @DatabaseField
    private String title;
    @DatabaseField
    private String summary;
    @DatabaseField
    private String publised_time;
    @DatabaseField
    private String updated_time;
    @DatabaseField(foreign = true)
    private AuthorInfo user;
    @DatabaseField
    private String content;

    public String getId() {
        return feed_id;
    }

    public void setId(String id) {
        this.feed_id = id;
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
