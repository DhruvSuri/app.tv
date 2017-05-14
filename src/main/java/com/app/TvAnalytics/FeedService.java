package com.app.TvAnalytics;

import com.app.Redis.RedisFactory;
import com.app.hello.BaseController;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created by dhruv.suri on 21/02/17.
 */
public class FeedService {
    private static FeedService instance = new FeedService();

    private String baseUrl = "http://dittotv.live-s.cdn.bitgravity.com/cdn-live/_definst_/dittotv/secure/sony_ent_Web.smil/";
    private Set<String> alreadyFetchedVideos = new HashSet<>();

    public static FeedService getInstance() {
        return instance;
    }

    public void fetchVideoFeeds(String chunkUrl) {
        try {
            String videoUrl = DefaultPaths.defaultVideoPath + new Date() + ".ts";


            while (BaseController.isPollingEnabled) {

                Thread.sleep(2000);
                List<String> videoListFromChunkList = getVideoListFromChunkList(chunkUrl);
                if (videoListFromChunkList == null || videoListFromChunkList.size() == 0) {
                    System.out.println("Video Poll waiting");
                    continue;
                }


                for (String url : videoListFromChunkList) {
                    int indexOf = url.indexOf(".ts");
                    alreadyFetchedVideos.add(url.substring(0, indexOf));

                    RedisFactory.video(videoUrl);
                    videoUrl = DefaultPaths.defaultVideoPath + new Date() + ".ts";
                    url = baseUrl + url;
                    FileUtils.copyURLToFile(new URL(url), new File(videoUrl));
                }

            }

        } catch (Exception ignored) {
            System.out.println(ignored.toString());
        }
        System.out.println("Polling stopped");
    }


    private List<String> getVideoListFromChunkList(String url) {

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(url);
            HttpResponse response = httpClient.execute(getRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Chunklist get call broken" + response.getStatusLine().getReasonPhrase());
                return null;
            }
            return parseFileResponse(response.getEntity().getContent());
            //return parseFileResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> parseFileResponse(InputStream response) {

        List<String> videosToFetch = new ArrayList<>();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(response));
        String url;
        try {
            while ((url = br.readLine()) != null) {
                if (url.startsWith("media_")) {
                    url = url + br.readLine();
                    int i = url.indexOf(".ts");
                    if (!alreadyFetchedVideos.contains(url.substring(0, i))) {
                        videosToFetch.add(url);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return videosToFetch;
    }


}
