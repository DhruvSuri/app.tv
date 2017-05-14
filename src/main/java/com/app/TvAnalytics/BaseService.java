package com.app.TvAnalytics;

import com.app.Redis.RedisFactory;
import com.app.TvAnalytics.OCR.OCRService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by dhruv.suri on 12/04/17.
 */
public class BaseService {
    public static Map<String, List<String>> keywordMap = initializeMap();
    private static BaseService instance = new BaseService();

    @Autowired
    OCRService ocrService;

    private BaseService() {
    }

    public static BaseService getInstance() {
        return instance;
    }

    public String identifyCreative() {
        while (true) {
            String imageUrl = RedisFactory.image();
            if (imageUrl == null) {
                try {
                    System.out.println("OCR thread sleeping");
                    Thread.sleep(2000);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            CreativeIdentificationTask task = new CreativeIdentificationTask(imageUrl);
            ocrService.doOCR(task, keywordMap);

        }

    }

    private static Map<String, List<String>> initializeMap() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);
            keywordMap = new HashMap();
            for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                String[] keywords = entry.getValue().toString().split(",");
                for (String keyword : keywords) {

                    if (keywordMap.containsKey(keyword)) {
                        List<String> list = keywordMap.get(keyword);
                        list.add(entry.getKey().toString());
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(entry.getKey().toString());
                        keywordMap.put(keyword, list);
                    }
                }
            }
            System.out.println("KeyWord Map : Keys : " + keywordMap.keySet().size() + "  Values : " + keywordMap.values().size());

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return keywordMap;
    }
}
