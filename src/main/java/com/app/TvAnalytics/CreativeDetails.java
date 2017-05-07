package com.app.TvAnalytics;

import java.util.List;

/**
 * Created by dhruv.suri on 12/04/17.
 */
public class CreativeDetails {
    private String creativeName;
    private String creativeBrand;
    private String productName;
    private String url;
    private List<String> keywords;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreativeName() {
        return creativeName;
    }

    public void setCreativeName(String creativeName) {
        this.creativeName = creativeName;
    }

    public String getCreativeBrand() {
        return creativeBrand;
    }

    public void setCreativeBrand(String creativeBrand) {
        this.creativeBrand = creativeBrand;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
