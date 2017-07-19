package com.lz.hlz.bean;

/**
 * Created by TB on 2017/7/17.
 */

public class main_lv_bean2 {
    private String imgurl;
    private String title;
    private String nr;

    public main_lv_bean2() {
        super();
    }

    public main_lv_bean2(String imgurl, String title, String nr) {
        this.imgurl = imgurl;
        this.title = title;
        this.nr = nr;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }
}
