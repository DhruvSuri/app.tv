package com.app.TvAnalytics;

import java.util.List;

/**
 * Created by dhruv.suri on 12/04/17.
 */
public class CreativeIdentificationTask {
    private String imageUrl;
    private List<String> keywords;
    private Boolean identified;
    private String createdAt;

    public Boolean getIdentified() {
        return identified;
    }

    public void setIdentified(Boolean identified) {
        this.identified = identified;
    }

    public CreativeIdentificationTask(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
