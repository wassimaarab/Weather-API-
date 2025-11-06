package eirb.pg203;


import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherAPI extends Data{
    WeatherAPI(){
        super();
        this.apiName="WeatherAPI";
    }

    @Override
    public JSONObject getFullData(String localisation) {
        try{
            return WeatherFetchingException.getData("http://api.weatherapi.com/v1/forecast.json?key=3a23ba266347468786d194705241412&q="+localisation+"&days=3");
        }
        catch(WeatherFetchingException weatherFetchingException){
            System.out.println("API name :" +apiName +"\n  API connection problem occured \n Please verify your Internet connection");
            return new JSONObject();
        }
    }

    @Override
    public void getTargetedData() {
        if(aJsonObject == null || aJsonObject.length() == 0){
            targetData = new JSONArray();
            return ; 
        }
        JSONArray resultArray = new JSONArray();
        JSONArray forecastDays = aJsonObject.getJSONObject("forecast").getJSONArray("forecastday");
        JSONObject name = new JSONObject();
        name.put("name",this.apiName);
        resultArray.put(name);
        for (int i = 0; i < forecastDays.length(); i++) {
            JSONObject day = forecastDays.getJSONObject(i);
            JSONArray hourlyData = day.getJSONArray("hour");
            double tempC=0 ;
            for (int j = 0 ; j < hourlyData.length(); j++ ){
                tempC+=hourlyData.getJSONObject(j).getDouble("temp_c");
            }
            JSONObject firstHour = hourlyData.getJSONObject(0);
            tempC /= hourlyData.length();
            double wind_kph = firstHour.getDouble("wind_kph");
            String aWeather= firstHour.getJSONObject("condition").getString("text");
            JSONObject dateTemp = new JSONObject();
            dateTemp.put("date",("J+" +i).toString() );
            dateTemp.put("temperature", (normalizeformat(tempC)+"°").toString()); 
            dateTemp.put("aWeather",aWeather);
            dateTemp.put("wind", (normalizeformat(wind_kph)+"km/h").toString());
            resultArray.put(dateTemp);
        }
        targetData= resultArray;
    }
}
