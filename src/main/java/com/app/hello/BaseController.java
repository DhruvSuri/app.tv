package com.app.hello;

import com.app.Redis.RedisFactory;
import com.app.TvAnalytics.BaseService;
import com.app.TvAnalytics.FeedService;
import com.app.TvAnalytics.ImageService;
import com.app.proxy.ProxyService;
import com.app.proxy.SocketService;
import com.app.utils.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.io.FileOutputStream;

@RestController
public class BaseController {
    public static Boolean isPollingEnabled = true;

    @Autowired
    ImageService imageService;

    @Autowired
    ProxyService proxyService;

    @Autowired
    BaseService baseService;

    @Autowired
    SocketService socketService;

    @Autowired
    MailService mailService;

    @RequestMapping(method = RequestMethod.GET, value = "/fetchFeed")
    public void startPollingFeed(@RequestParam("chunkUrl") String chunkUrl) {
        isPollingEnabled = true;
        FeedService.getInstance().fetchVideoFeeds(chunkUrl);
    }

    @RequestMapping(method = RequestMethod.GET, value = "convertVideoToImage")
    public void convertVtoI() {
        ImageService.getInstance().convertVideoFeedToImage();
    }

    @RequestMapping(method = RequestMethod.GET, value = "analyze")
    public void startAnalyzing() {
        baseService.identifyCreative();
    }

    @RequestMapping(method = RequestMethod.GET, value = "proxy")
    public String doProxy(WebRequest request, @RequestParam("url") String url) {
        return proxyService.doProxy(request,url);
    }

    @RequestMapping(method = RequestMethod.GET, value = "mail")
    public void mail(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("size") String size, @RequestParam("msg") String msg) {
        String text = name + "  " + email + "  " + size + "  " + msg + "  ";
        RedisFactory.mail(text);
        new MailService().sendProxyMail(text);
    }

    @RequestMapping(method = RequestMethod.GET, value = "clearProxyList")
    public void clearProxyList() {
        socketService.cleanConnectionPool();
        System.out.println("Cleared connection pool");
    }

    @RequestMapping(method = RequestMethod.GET, value = "bonny")
    public String bonny() {
        return "HTTP/1.1 200 OK\n" +
                "Date: Thu, 01 Jun 2017 20:10:20 GMT\n" +
                "Server: Apache/2.4.7 (Ubuntu)\n" +
                "Last-Modified: Tue, 30 May 2017 14:15:12 GMT\n" +
                "ETag: \"7c22-550be6e8c69fa\"\n" +
                "Accept-Ranges: bytes\n" +
                "Content-Length: 31778\n" +
                "Vary: Accept-Encoding\n" +
                "Content-Type: text/html\n\n" +
                "<h1>Hello, client! Welcome to the Virtual Machine Web..</h1>";
    }

    @RequestMapping(method = RequestMethod.GET, value = "testPath")
    public void testPath() {
        try {
            new FileOutputStream("testFile.txt").close();
            new FileOutputStream("Video/testFile.txt").close();
            new FileOutputStream("Image/testFile.txt").close();
            new FileOutputStream("config.properties").close();
        } catch (Exception e) {

        }
    }

}
