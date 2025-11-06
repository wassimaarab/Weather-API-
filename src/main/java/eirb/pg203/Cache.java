package eirb.pg203;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;


public class Cache {
    private static final String cacheDirectory = "./cache/";
    private static final long cacheLife  = 86400; 

    /**
     * The constructor creats the directory cache in the project root
     */
    public Cache() {
        createCacheDirectory();
    }

    /**
     * Creats a cach directory 
     */
    private void createCacheDirectory() {
        try {
            Files.createDirectories(Paths.get(cacheDirectory));
        } catch (IOException e) {
            System.err.println("Error occured while creating the file : " + e.getMessage());
        }
    }

    /**
     * Return a string containing the cache file stored in the cach directory
     * @param city : String  
     * @param api  : String
     * @return String
     */ 
    private String getCacheFileName(String city, String api) {
        return cacheDirectory + city + api + ".json";
    }

    /**
     * Extracts the datas in the cach directory 
     * @param city : String
     * @param api : String
     * @return JSONObject 
     */
    public JSONObject getCachedData(String city, String api) {
        String cacheFile = getCacheFileName(city, api);
        File file = new File(cacheFile);

        if (!file.exists()) {
            return null;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(cacheFile)));
            JSONObject jsonData = new JSONObject(content);
            long timestamp = jsonData.getLong("timestamp");
            long now = Instant.now().getEpochSecond();

            if (now - timestamp < cacheLife) {
                return jsonData;
            } else {
                file.delete(); 
            }
        } catch (IOException e) {
            System.err.println("Error occured while reding the file : " + e.getMessage());
        }

        return null;
    }

    /**
     * Save the data of a given city and API into the cache directory 
     * @param city : String
     * @param api : String
     * @param value : JSONObject
     */
    public void saveToCache(String city, String api, JSONArray value) {
        String cacheFile = getCacheFileName(city, api);
        JSONObject jsonData = new JSONObject();

        try {
            jsonData.put("city", city);
            jsonData.put("api", api);
            jsonData.put("timestamp", Instant.now().getEpochSecond());
            jsonData.put("value", value);

            try (FileWriter fileWriter = new FileWriter(cacheFile)) {
                fileWriter.write(jsonData.toString(4)); 
            }
        } catch (IOException e) {
            System.err.println("Error occured while reading the cache : " + e.getMessage());
        }
    }
}
