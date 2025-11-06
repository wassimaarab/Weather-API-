package eirb.pg203;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class OpenMeteo extends Data{
    /**
     * Contains all the weather codes WCO with the description associated to it 
     */
    JSONObject weatherDescriptions=new JSONObject();

    /**
     * fill in the weatherDesptions  
     */
    OpenMeteo(){
        super();
        this.apiName ="OpenMeteo";
        weatherDescriptions.put("0", "clear");
        weatherDescriptions.put("1", "clear");
        weatherDescriptions.put("2", "partly cloudy");
        weatherDescriptions.put("3", "overcast");
        weatherDescriptions.put("45", "fog");
        weatherDescriptions.put("48", "cold");
        weatherDescriptions.put("51", "rain");
        weatherDescriptions.put("53", "rain");
        weatherDescriptions.put("55", "rain");
        weatherDescriptions.put("61", "rain");
        weatherDescriptions.put("63", "rain");
        weatherDescriptions.put("65", "rain");
        weatherDescriptions.put("80", "showers");
        weatherDescriptions.put("81", "showers");
        weatherDescriptions.put("82", "showers");
        weatherDescriptions.put("95", "Thunderstorm");
        weatherDescriptions.put("96", "Thunderstorm");
        weatherDescriptions.put("99", "Thunderstorm");
    }

    /**
     * Get the weather description associated to a weather code 
     * @param weatherCodes a AJSONARRAY containing the weather codes
     * @return String describing the weather 
     */
    String getWeatherDescription(JSONArray weatherCodes) {
        StringBuilder description = new StringBuilder();
        for (int i = 0; i < weatherCodes.length(); i++) {
            String code = weatherCodes.optString(i, "0"); // Utiliser "0" comme valeur par défaut si le code est invalide
            String weather = weatherDescriptions.optString(code, "unknown");
            description.append(" ").append(weather);
        }
        return description.toString().trim();
    }
    /**
     * Given a city name it returns the longitude and latitude associated
     * @param cityName
     * @return double[2]={latitude , longitude}
     * 
     */
    static double[] getCoordinates(String cityName) {
        double[] coord = new double[2];
        coord[0]=0;
        coord[1]=0;
        if (cityName == null || cityName.trim().isEmpty()) {
            System.err.println("No city name mentionned");
            return coord ;
        }
    
        StringBuilder cityData = new StringBuilder();
        try{
            URL url = URI.create("https://api.opencagedata.com/geocode/v1/json?q="+cityName+"&key=482503fa8b38414c97c4b1866ae52a35").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    cityData.append(line);
                }
            }
        }
        catch(IOException ioe){
            System.out.println("API name : opencagetdata\n API connection problem occured \\n" +//
                                " Please make verify your Internet connection");
            return coord ;
        }
        
        try{
            JSONObject jsonCityData = new JSONObject(cityData.toString());
            double lat = jsonCityData.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getDouble("lat");
            double lng = jsonCityData.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getDouble("lng");
            coord[0]=lat;
            coord[1]=lng;
            return coord;
        }
        catch(JSONException jo){
            System.out.println("Please enter a Valid city name");
            return coord;
        }
    }

    @Override
    public JSONObject getFullData(String localisation) {
        double[] coords = getCoordinates(localisation);
        if(coords[1]== 0 && coords[0]==1){
            return new JSONObject();
        }
        try{
            return WeatherFetchingException.getData("https://api.open-meteo.com/v1/forecast?latitude="+coords[0]+"&longitude="+coords[1]+"&hourly=temperature_2m,wind_speed_10m&forecast_days=3&daily=weather_code");
        }
        catch(WeatherFetchingException weatherFetchingException){
            System.out.println("API name :" +apiName +"\n  API connection problem occured \n Please verify your Internet connection");
            return new JSONObject();
        }
    }

    @Override
    public void getTargetedData() {
        targetData = new JSONArray();
        if(aJsonObject == null || aJsonObject.length() == 0){
            return;
        }
        JSONObject name = new JSONObject();
        name.put("name",this.apiName);
        targetData.put(name);
        for (int i=0 ; i<3 ; i++){

            JSONArray hourlyTemp = aJsonObject.getJSONObject("hourly").getJSONArray("temperature_2m");
            JSONArray hourlyWind = aJsonObject.getJSONObject("hourly").getJSONArray("wind_speed_10m");
            JSONArray weatherCode = aJsonObject.getJSONObject("daily").getJSONArray("weather_code");

            double tempC=0;
            double wind_kph = 0;
            for(int j = 0 ; j < hourlyTemp.length() ; j++ ){
                tempC += hourlyTemp.getDouble(j);
            }
            for(int j = 0 ; j < hourlyWind.length(); j++){
                wind_kph+=hourlyWind.getDouble(j);
            }
            tempC/=hourlyTemp.length();
            wind_kph/=hourlyWind.length();

            String weatherCondition = getWeatherDescription(weatherCode);

            JSONObject dateTemp = new JSONObject();
            dateTemp.put("date",("J+"+i).toString() );
            dateTemp.put("temperature", (normalizeformat(tempC)+"°").toString()); 
            dateTemp.put("wind", (normalizeformat(wind_kph)+"km/h").toString());
            dateTemp.put("aWeather",weatherCondition);
            targetData.put(dateTemp);
        }
    }
}
