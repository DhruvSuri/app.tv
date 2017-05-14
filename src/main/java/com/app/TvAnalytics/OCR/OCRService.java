package com.app.TvAnalytics.OCR;

import com.app.TvAnalytics.BaseService;
import com.app.TvAnalytics.CreativeIdentificationTask;
import com.app.utils.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dhruv.suri on 12/04/17.
 */
@Service
public class OCRService {

    @Autowired
    MailService mailer;

    private ExecutorService executor = Executors.newFixedThreadPool(5);


    public void doOCR(CreativeIdentificationTask task, Map<String, List<String>> keywordMap) {

        Runnable worker = new OCRRunnable(task);
        executor.execute(worker);
    }

    public void doOCRThreaded(CreativeIdentificationTask task) {
        OCRAPIWrapper ocr = APIExecutor.getInstance().process(task.getImageUrl());

        synchronized (this) {
            if (ocr.getErroredOnProcessing()) {
                task.setIdentified(false);
                System.out.println("Current Thread " + Thread.currentThread().getId() + "Image URL : " + ocr.getImageUrl() + "  Status : Unidentified");
                return;
            }
            task.setIdentified(true);
            String parsedText = ocr.getParsedResults().get(0).getParsedText();
            parsedText = parsedText.replace("\n", " ");
            parsedText = parsedText.replace("\r", " ");

            task.setKeywords(Arrays.asList(parsedText.split(" ")));
            System.out.println("Current Thread " + Thread.currentThread().getId() + "Image URL : " + ocr.getImageUrl() + "  Status : Identified" + "     KEYWORDS EXTRACTED : " + task.getKeywords());
            mailer.sendMail("Image URL : " + ocr.getImageUrl() + "  Status : Identified" + "     KEYWORDS EXTRACTED : " + task.getKeywords(), true);

            Map<String, Integer> majorityMap = new HashMap<>();
            for (String keyword : task.getKeywords()) {
                if (BaseService.keywordMap.containsKey(keyword)) {
                    List<String> creatives = BaseService.keywordMap.get(keyword);
                    for (String creative : creatives) {
                        if (!majorityMap.containsKey(creative)) {
                            majorityMap.put(creative, 1);
                            continue;
                        }
                        majorityMap.put(creative, majorityMap.get(creative) + 1);
                    }
                }
            }


            if (majorityMap.size() == 1) {
                Map.Entry<String, Integer> entry = majorityMap.entrySet().iterator().next();
                //TODO Build timeline here
                System.out.println("[IDENTIFIED] : " + task.getImageUrl() + " " + entry.getKey());
                mailer.sendMail("[IDENTIFIED] : " + entry.getKey(), false);
                mailer.sendMail("[IDENTIFIED] : " + task.getImageUrl() + entry.getKey(), true);
            }
        }


    }

    public class OCRRunnable implements Runnable {


        CreativeIdentificationTask task;

        OCRRunnable(CreativeIdentificationTask task) {
            this.task = task;
        }

        @Override
        public void run() {

            doOCRThreaded(task);
        }
    }
}
