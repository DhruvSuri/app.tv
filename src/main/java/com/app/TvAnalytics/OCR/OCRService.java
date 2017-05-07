package com.app.TvAnalytics.OCR;

import com.app.TvAnalytics.CreativeIdentificationTask;

import java.util.Arrays;

/**
 * Created by dhruv.suri on 12/04/17.
 */
public class OCRService {
    public static OCRService instance = new OCRService();

    public void doOCR(CreativeIdentificationTask task) {
        System.out.println("Processing : " + task.getImageUrl());
        OCRAPIWrapper ocr = APIExecutor.getInstance().process(task.getImageUrl());
        if (ocr.getErroredOnProcessing()) {
            task.setIdentified(false);
            return;
        }
        task.setIdentified(true);
        String parsedText = ocr.getParsedResults().get(0).getParsedText();
        parsedText = parsedText.replace("\n"," ");
        parsedText = parsedText.replace("\r"," ");

        task.setKeywords(Arrays.asList(parsedText.split(" ")));

        System.out.println("Keywords : " + task.getKeywords());
    }
}
