package com.app.TvAnalytics;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhruv.suri on 18/02/17.
 */
public class AdService {

    private static final String indexFolderName = "index/";
    private int indexFolderCounter = 0;
    private static AdService instance = new AdService();
    Map<String, String> map = new HashMap<>();

    public static AdService getInstance() {
        return instance;
    }

    public void fetchAdFromURL(String url){}

    public void convertAdVideoToImage(String videoPath,String imagePath){}

    public void preprocessAdsForIndexing(final String adFolder) {
        indexFolderCounter = 1;
        try {
            Path path = Paths.get(adFolder + "Ads/");

            Path indexFolderPath = Paths.get(adFolder + indexFolderName);

            Files.createDirectory(indexFolderPath);

            Files.walkFileTree(path, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileName = file.getFileName().toString();

                    if (fileName.contains("png") || fileName.contains("jpg") || fileName.contains("jpeg")) {
                        int count = getImageCounter();
                        String parentName = file.getParent().getFileName().toString();
                        Path target = Paths.get(adFolder + indexFolderName + "/" + count + ".png");
                        Files.copy(file, target);
                        map.put(target.getFileName().toString(), parentName);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });


            //serialize map
//            try {
////                FileOutputStream fos =
////                        new FileOutputStream(DefaultPaths.mapPath);
////                ObjectOutputStream oos = new ObjectOutputStream(fos);
////                oos.writeObject(map);
////                oos.close();
////                fos.close();
//                System.out.printf("Serialized HashMap data is saved in hashmap.ser");
//            } catch (IOException ioe) {
//                ioe.printStackTrace();
//            }


        } catch (FileAlreadyExistsException | NoSuchFileException e) {
            System.out.println(e.toString());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private int getImageCounter() {
        return indexFolderCounter++;
    }

}
