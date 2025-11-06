package eirb.pg203;

import org.json.JSONArray;
import org.json.JSONObject;

public class FirstIteration extends Arguments{
    JSONObject aJsonObject;
    FirstIteration(JSONObject aJsonObject){
        this.aJsonObject=aJsonObject;
    }
    
    public JSONArray getTemperatures() {
        JSONArray resultArray = new JSONArray();
        JSONArray forecastDays = aJsonObject.getJSONObject("forecast").getJSONArray("forecastday");

        for (int i = 0; i < forecastDays.length(); i++) {
            JSONObject day = forecastDays.getJSONObject(i);
            JSONArray hourlyData = day.getJSONArray("hour");
            JSONObject firstHour = hourlyData.getJSONObject(0);
            double tempC = firstHour.getDouble("temp_c");
            JSONObject dateTemp = new JSONObject();
            dateTemp.put("date",("J+" +i).toString() );
            dateTemp.put("temperature", (tempC+"°").toString()); 
            resultArray.put(dateTemp);
        }
        return resultArray;
    }

    static void afficheNtimes(String aString, int number){
        for(int i =0 ; i < number ; i++){
            System.out.print(aString);
        }
    }
    static void afficherTable1(JSONArray aJsonArray){
        int length =aJsonArray.length();
        afficheNtimes(" ", 6);
        afficheNtimes("+-----",length);
        System.out.println();
        afficheNtimes(" ", 6);
        for (int i = 0 ; i < length ;i++) {
            System.out.printf("| %-3s ", aJsonArray.getJSONObject(i).getString("date"));
        }
        System.out.println("|");
        afficheNtimes("+-----",length+1);
        System.out.println();
        System.out.print("|     "); 
        for (int i = 0 ; i < length;i++) {
            System.out.printf("| %-3s ", aJsonArray.getJSONObject(i).getDouble("temperature")); 
        }
        System.out.println("|");
        afficheNtimes("+-----",length+1);
        System.out.println();
    }
}