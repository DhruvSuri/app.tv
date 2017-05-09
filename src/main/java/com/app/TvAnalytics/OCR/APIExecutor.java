package com.app.TvAnalytics.OCR;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import twitter4j.JSONObject;
import com.app.utils.AzazteUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by dhruv.suri on 09/04/17.
 */
public class APIExecutor {
    private static final String baseUrl = "https://apipro3.ocr.space/parse/image";
    private static final String AWSBaseURl = "https://s3.ap-south-1.amazonaws.com/tv-image/";
    private static final String baseUrlSec = "https://apipro1.ocr.space/parse/image";

    private static APIExecutor instance = new APIExecutor();

    public static APIExecutor getInstance() {
        return instance;
    }

    public OCRAPIWrapper process(String imageUrl) {
//        imageUrl = aws(imageUrl);
        try {
            URL obj = new URL(baseUrl); // OCR API Endpoints
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("POST");

            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");


            JSONObject postDataParams = new JSONObject();

            postDataParams.put("apikey", "PKMXB3765888A");
            postDataParams.put("isOverlayRequired", false);
            postDataParams.put("url", "Image/" + imageUrl);


            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(getPostDataString(postDataParams));
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return AzazteUtils.fromJson(response.toString(), OCRAPIWrapper.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public static String aws(String imageUrl) {
        final long startTime = System.currentTimeMillis();
        AWSCredentials credentials = new BasicAWSCredentials("AKIAIQRNVAGHVSCD6EYA", "/lbj25SjFvvwFBbj9vsDRvXZu6fTZj3qtssDmApM");
        AmazonS3 s3client = new AmazonS3Client(credentials);
        s3client.putObject(new PutObjectRequest("tv-image", imageUrl,
                new File("image/" + imageUrl)).withCannedAcl(CannedAccessControlList.PublicRead));
        final long duration = System.currentTimeMillis() - startTime;
        System.out.println("AWS : (Seconds)" + duration / 1000);
        return AWSBaseURl + imageUrl;

    }

}
