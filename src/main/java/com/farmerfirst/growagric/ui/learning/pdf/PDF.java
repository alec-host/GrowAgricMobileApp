package com.farmerfirst.growagric.ui.learning.pdf;

public class PDF {
    private String url;
    private String name;
    private int courseCount;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
    }

    public int getCourseCount() {
        return courseCount;
    }
}
