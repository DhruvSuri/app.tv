package com.app.TvAnalytics.OCR;

import java.util.List;

/**
 * Created by dhruv.suri on 12/04/17.
 */
public class OCRAPIWrapper {
    private int OCRExitCode;
    private Boolean IsErroredOnProcessing;
    private String ErrorMessage;
    private String ProcessingTimeInMilliseconds;
    private List<ParsedResult> ParsedResults;

    public List<ParsedResult> getParsedResults() {
        return ParsedResults;
    }

    public void setParsedResults(List<ParsedResult> parsedResults) {
        ParsedResults = parsedResults;
    }

    public int getOCRExitCode() {
        return OCRExitCode;
    }

    public void setOCRExitCode(int OCRExitCode) {
        this.OCRExitCode = OCRExitCode;
    }

    public Boolean getErroredOnProcessing() {
        return IsErroredOnProcessing;
    }

    public void setErroredOnProcessing(Boolean erroredOnProcessing) {
        IsErroredOnProcessing = erroredOnProcessing;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getProcessingTimeInMilliseconds() {
        return ProcessingTimeInMilliseconds;
    }

    public void setProcessingTimeInMilliseconds(String processingTimeInMilliseconds) {
        ProcessingTimeInMilliseconds = processingTimeInMilliseconds;
    }
}


class ParsedResult {
    private String ParsedText;
    private String ErrorMessage;

    public String getParsedText() {
        return ParsedText;
    }

    public void setParsedText(String parsedText) {
        this.ParsedText = parsedText;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}