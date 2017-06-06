package com.app.proxy;

import com.app.proxy.Beans.RequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by dhruv.suri on 11/05/17.
 */
@Service
public class ProxyService {

    @Autowired
    SocketService socketService;

    public String doProxy(WebRequest request, String url) {
        if (url.isEmpty()){
            return "Url should not be emply";
        }
        return socketService.sendProxyRequest(buildRequest(request,url));
    }

    private String buildRequest(WebRequest request, String url){
        String requestString = "curl -X GET -w ____%{size_download} " + url;

//        Iterator<String> temp = request.getHeaderNames();

//        while(temp.hasNext()){
//            String header = temp.next();
//            requestString = requestString + " -H '" + header + ":" + request.getHeader(header) + "' ";
//        }
        return requestString;
    }
}
