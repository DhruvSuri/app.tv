package com.app.hello;

import com.app.TvAnalytics.BaseService;
import com.app.TvAnalytics.FeedService;
import com.app.TvAnalytics.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileOutputStream;

@RestController
public class BaseController {
    public static Boolean isPollingEnabled = true;

    @Autowired
    ImageService imageService;




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
        BaseService.getInstance().identifyCreative();
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


//    public static void main(String args[]) {
////        ImageService.getInstance().convertVideoFeedToImage();
//        while (true) {
//            String imageUrl = RedisFactory.image();
//
//            CreativeIdentificationTask task = new CreativeIdentificationTask(imageUrl);
//            OCRService.instance.doOCR(task);
//            System.out.println(imageUrl + " " + task.getKeywords());
//        }
//
//    }
}
