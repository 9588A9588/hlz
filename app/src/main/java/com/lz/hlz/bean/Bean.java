package com.lz.hlz.bean;

/**
 * Created by ccwxf on 2016/8/1.
 */
public class Bean {

    private int icon;
    private String time;
    private String title;
    private String summary;
    private String url;

    public Bean(int icon, String time, String title, String summary, String url) {
        this.icon = icon;
        this.time = time;
        this.title = title;
        this.summary = summary;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "icon=" + icon +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public Bean() {

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
