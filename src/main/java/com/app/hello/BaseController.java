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
import java.util.LinkedList;
import java.util.Queue;

@RestController
public class BaseController {
    public static Boolean isPollingEnabled = true;

    public Queue<Message> q = new LinkedList<>();

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


    @RequestMapping(method = RequestMethod.GET, value = "testMulti")
    public void testMulti(@RequestParam("num") int num,@RequestParam("msg") String msg){
        if (num == 1){
            new Worker(new Message(msg)).process();
        }
        if (num == 2){
            System.out.println(q.size());
            Message poll = q.poll();
            synchronized (poll){
                poll.notify();
                System.out.println("Notifier");
            }
        }
    }

    class    Worker{
        private Message msg;

        public Worker(Message msg){
            this.msg = msg;
        }

        public void process(){
            synchronized (msg){
                try {
                    q.add(msg);
                    System.out.println("Adding to Queue : " + msg);
                    msg.wait();
                    System.out.println("Notified");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Message{
        private String msg;
        public Message(String msg){
            this.msg = msg;
        }
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
