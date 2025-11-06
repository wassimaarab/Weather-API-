package eirb.pg203;

import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DecimalFormat;

/*
 * Data is an abstracrt class that extract the needed data in a given JSONObject 
 */
public abstract class Data {
    public String apiName ;

    /**
     * The given location 
     */
    public String Location ;
    /**
     * Retuned from an API connection
     */
    public JSONObject aJsonObject;
    
    /**
     * To store the targeted data
     */
    public JSONArray targetData ;

    /**
     * Put the data into "targetData" from the cache if exist , stores them in the cach otherwise
     * @param api : String
     * @param city : String
     */
    public void arrayCheckCache(String api ,String city ){
        Cache cache = new Cache();
        JSONObject cachedData = cache.getCachedData(city, api);
        if (cachedData != null) {
            System.out.println("API name :"+api+"\nThe data are taken from the cach");
           targetData = cachedData.getJSONArray("value");
        } else {
            System.out.println("API name :"+api+"\n No data in cache");
            aJsonObject=getFullData(city);
            getTargetedData();

            // Sauvegarder dans le cache
            cache.saveToCache(city, api, targetData);
        }
        targetData= cache.getCachedData(city, api).getJSONArray("value");
    }
    /**
     * Get the full data for a given location 
     * Handle API connection and argument exceptions
     * @param localisation : the given location
     * @return JSONObject with data inside  
     */
    public abstract JSONObject getFullData(String localisation);

    /**
     * Extract the targeted data (Temperature , wind Speed , weather ...) from our JSONObject   
     * And put it in the JSONArray targetData
     */
    public abstract void getTargetedData();
    
    /**
     * This function is used to write all the data in the forme "xx.xx" if positif, "-xx.x" otherwise
     * @params value : the value to formats
     * @return String containing the value in a specific format 
     */
    public String normalizeformat(double value){
        DecimalFormat nf = new DecimalFormat("00.0");
        DecimalFormat df = new DecimalFormat("00.00");
        String valueformatee;
        if(value >=0){
            valueformatee= df.format(value);
        }
        else{
            valueformatee= nf.format(value);
        }
        return valueformatee;
    }
}
