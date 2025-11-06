package eirb.pg203;

import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONObject;

public class SecondThirdIteration extends Arguments{
    WeatherAPI f1 = new WeatherAPI();
    OpenMeteo f2 = new OpenMeteo();
    OpenWeatherMap f3 = new OpenWeatherMap();
    /**
     * Stores all the data returned by different API's 
     */
    JSONArray[] aJsonArraysList;

    /**
     * {key = weather condition : value = icon}
     */
    JSONObject icons = new JSONObject();
    
    /**
     * Fill in the icons JSONOBject 
     * @param aJsonArraysList
     */
    SecondThirdIteration(String[] args){
        this.aJsonArraysList=Weather(args);
        icons.put("sun", " ☀️ ");
        icons.put("clear", " ☀️ ");
        icons.put("partly cloud", " 🌤 ");
        icons.put("cloud", " ☁️ ");
        icons.put("overcast", " 🌥 ");
        icons.put("thunderstorm", " ⛈ ");
        icons.put("snow", " 🌨 ");
        icons.put("fog", " 🌫 ");
        icons.put("tornado", " 🌪 ");
        icons.put("cold", " ❄️ ");
        icons.put("rain", " 🌧️ ");
        icons.put("storm", " 🌩️ ");
        icons.put("wind", " 🌬️ ");
        icons.put("showers"," 🌦️ ");
        icons.put("default", " 🌕 ");
    }

    /**
     * Display a given string "aString" a "number" of times
     * @param aString 
     * @param number 
     */
    static void displayNtimes(String aString, int number){
        for(int i =0 ; i < number ; i++){
            System.out.print(aString);
        }
    }
    
    /**
     * Given a weather condition "weather" returns the associated icon
     * @param weather
     * @return String containing the icon 
     */
    String getWeatherIcon(String weather) {
        String weatherLC = weather.toLowerCase();
        for (String key : icons.keySet()) {       
        if (weatherLC.contains(key)) {       
            return icons.getString(key);      
        }
    }
        return icons.getString("default");
    }

    public <E extends Data> void countNonEmptyJSONArray(E api, String location, AtomicInteger count) {
        api.arrayCheckCache(api.apiName, location);
        if (api.targetData == null || api.targetData.length() != 0) {
            count.incrementAndGet();
        }
    }

    public <E extends Data> void storeNonEmptyJSONArray(E api, JSONArray[] jsonArrayTable ,AtomicInteger count) {
        if(api.targetData.length()!= 0){
            jsonArrayTable[count.get() -1] = api.targetData;
            count.decrementAndGet();
        }
    }

    public JSONArray[] Weather(String[] args)  {
        AtomicInteger count = new AtomicInteger(0);
        try{
        String localisation =Arguments.getLocalisation(args);

        
        countNonEmptyJSONArray(f1,localisation,count);
        countNonEmptyJSONArray(f2,localisation,count);
        countNonEmptyJSONArray(f3,localisation,count);

        JSONArray[] jsonArrayTable = new JSONArray[count.get()];
        
        storeNonEmptyJSONArray(f1, jsonArrayTable, count);
        storeNonEmptyJSONArray(f2, jsonArrayTable, count);
        storeNonEmptyJSONArray(f3, jsonArrayTable, count);

        return jsonArrayTable;
        }
        catch(ArgumentException arge){
            System.err.println("ERROR Localisation: No city mentionned. Please include \\\"-l CityName\\\" in your command lignee");
            return null;
        }
    }

    /**
     * display the table with all the API and the data associated 
     */
    void displayTable(){
        if (aJsonArraysList == null) {
            return ;
        }
        int length =aJsonArraysList.length;
        displayNtimes(" ", 19);
        displayNtimes("+---------------------",3);
        System.out.println();
        displayNtimes(" ", 19);
        for (int i = 0 ; i < 3 ;i++) {
            System.out.printf("| %-19s ", aJsonArraysList[length-1].getJSONObject(i+1).getString("date"));
        }
        System.out.println("|");
        System.out.print("+------------------");
        displayNtimes("+---------------------",3);
        for (int i = 0; i < length ;i++) {
            
            System.out.println();
            System.out.printf("| %-16s ",aJsonArraysList[i].getJSONObject(0).getString("name")) ;
            for(int j =1 ; j< aJsonArraysList[i].length() ;j++){
                String temp =aJsonArraysList[i].getJSONObject(j).getString("temperature");
                System.out.printf("| %-3s", temp);
                String icon = getWeatherIcon(aJsonArraysList[i].getJSONObject(j).getString("aWeather"));
                if(icon.equals(" ☀️ ") || icon.equals(" ☁️ ")){
                    System.out.printf("%-5s", icon);
                }
                else{
                    System.out.printf("%-6s", icon);
                }
                String wind =aJsonArraysList[i].getJSONObject(j).getString("wind");
                System.out.printf("%-6s",wind); 
            }
            System.out.println("|");
            System.out.print("+------------------");
            displayNtimes("+---------------------",3);
            System.out.println();
        }
        
    }

}
