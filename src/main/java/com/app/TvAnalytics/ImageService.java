package com.app.TvAnalytics;

import com.app.Redis.RedisFactory;
import org.springframework.stereotype.Service;

/**
 * Created by dhruv.suri on 13/04/17.
 */
@Service
public class ImageService {

    private static ImageService instance = new ImageService();

    public static ImageService getInstance() {
        return instance;
    }

    public void convertVideoFeedToImage() {
        try {
            while (true) {
                String videoUrl = RedisFactory.video();
                if (videoUrl == null) {
                    Thread.sleep(2000);
                    continue;
                }
                try {
                    VideoToImageConvertor.getInstance().convert(videoUrl);
                    System.out.println("Video converted : " + videoUrl);
                } catch (Exception e) {

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
