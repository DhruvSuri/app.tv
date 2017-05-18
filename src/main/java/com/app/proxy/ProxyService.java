package com.app.proxy;

import com.app.proxy.Beans.RequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * Created by dhruv.suri on 11/05/17.
 */
@Service
public class ProxyService {

    @Autowired
    SocketService socketService;

    public void doProxy(Map<String, String[]> headers) {
        String response = socketService.sendProxyRequest(headers.get("url")[0]);
        System.out.println(response);

        if (!headers.containsKey("url")) {
            headers.remove("url");
        }
        for (Map.Entry<String, String[]> entry : headers.entrySet()) {
            System.out.print(entry.getKey());
        }
    }

    public boolean authorizeUser(String token) {
        return true;
    }
}
