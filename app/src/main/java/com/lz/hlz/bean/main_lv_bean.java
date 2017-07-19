package com.lz.hlz.bean;

/**
 * Created by TB on 2017/7/17.
 */

public class main_lv_bean {
    private String[] imgurls; //图片url
    private String[] titles;//标题列表
    private String[] nrs;//内容列表

    public main_lv_bean() {
        super();
    }

    public main_lv_bean(String[] imgurls, String[] titles, String[] nrs) {
        this.imgurls = imgurls;
        this.titles = titles;
        this.nrs = nrs;
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public String[] getNrs() {
        return nrs;
    }

    public void setNrs(String[] nrs) {
        this.nrs = nrs;
    }

    public String[] getImgurls() {
        return imgurls;
    }

    public void setImgurls(String[] imgurls) {
        this.imgurls = imgurls;
    }
}
