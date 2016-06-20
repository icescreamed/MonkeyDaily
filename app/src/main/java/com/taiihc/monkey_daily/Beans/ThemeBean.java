package com.taiihc.monkey_daily.Beans;


public class ThemeBean {
    public ThemeBean(String colorname){
        this(colorname,false);
    }
    public ThemeBean(String colorname,boolean used){
        this.used =used;
        this.colorname = colorname;
    }


    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }


    private boolean used;

    public String getColorname() {
        return colorname;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }

    private String colorname;

}
