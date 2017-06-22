package com.app.proxy.Beans;

import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

/**
 * Created by dhruv.suri on 31/05/17.
 */
public class ProxyResponse {
    private String name;
    private String number;
    private int responseStatus;
    private long ttlb;
    private String responseBody;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public long getTtlb() {
        return ttlb;
    }

    public void setTtlb(long ttlb) {
        this.ttlb = ttlb;
    }

    @Override
    public String toString() {
        return "ProxyResponse{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", responseStatus=" + responseStatus +
                ", ttlb=" + ttlb +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
