package eirb.pg203;

import org.json.JSONArray;
import org.json.JSONObject;

class OpenWeatherMap extends Data {
    OpenWeatherMap(){
        super();
        this.apiName ="OpenWeatherMap";
    }

    @Override
    public JSONObject getFullData(String localisation) {

        try{
            return WeatherFetchingException.getData("http://pro.openweathermap.org/data/2.5/forecast/climate?q="+localisation+"&units=metric&cnt=3&APPID=c23b6ccfecb0828d7c6f6508b06d2e3d");
        }
        catch(WeatherFetchingException weatherFetchingException){
            System.out.println("API name :" +apiName +"\n  API connection problem occured \n Please verify your Internet connection");
            return new JSONObject();
        }
    }

    @Override
    public void getTargetedData() {
        if(aJsonObject == null || aJsonObject.length() == 0){
            return;
        }
        JSONArray resultArray = new JSONArray();
        JSONObject name = new JSONObject();
        name.put("name",this.apiName);

        JSONArray listJsonArray = aJsonObject.getJSONArray("list");
        resultArray.put(name);
        for (int i=0 ; i< listJsonArray.length() ; i++){

            JSONObject diffTemps = listJsonArray.getJSONObject(i);
            double tempC = diffTemps.getJSONObject("temp").getDouble("day");
            double wind_kph = diffTemps.getDouble("speed");
            String weather= diffTemps.getJSONArray("weather").getJSONObject(0).getString("main");
            JSONObject dateTemp = new JSONObject();

            dateTemp.put("date",("J+"+i).toString() );
            dateTemp.put("temperature", (normalizeformat(tempC)+"°").toString()); 
            dateTemp.put("aWeather",weather.toString());
            dateTemp.put("wind", (normalizeformat(wind_kph)+"km/h").toString());

            resultArray.put(dateTemp);
        }
        targetData = resultArray;
    }
}

