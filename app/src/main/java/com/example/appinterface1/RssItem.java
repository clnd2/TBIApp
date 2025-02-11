package com.example.appinterface1;
// HR: ChatGPT
public class RssItem {
    private CharSequence title;
    private String link;
    private String description;
    private String pubDate;

    public RssItem(CharSequence title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }

    // Getters
    public CharSequence getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }
}
