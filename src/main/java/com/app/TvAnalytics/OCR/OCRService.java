package com.app.TvAnalytics.OCR;

import com.app.TvAnalytics.CreativeIdentificationTask;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dhruv.suri on 12/04/17.
 */
public class OCRService {
    public static OCRService instance = new OCRService();

    ExecutorService executor = Executors.newFixedThreadPool(5);


    public void doOCR(CreativeIdentificationTask task) {

        Runnable worker = new OCRRunnable(task);
        executor.execute(worker);
    }

    public void doOCRThreaded(CreativeIdentificationTask task) {
        OCRAPIWrapper ocr = APIExecutor.getInstance().process(task.getImageUrl());


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
        System.out.println("Current Thread " + Thread.currentThread().getId() + "Image URL : " + ocr.getImageUrl() + "  Status : Unidentified" + "     KEYWORDS EXTRACTED : " + task.getKeywords());

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
