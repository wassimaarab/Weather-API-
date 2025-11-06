package eirb.pg203;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.json.JSONObject;

public class WeatherFetchingException extends Exception{
    JSONObject aJsonObject;
    WeatherFetchingException(String erroString){
        super(erroString);
    }

    
    static public JSONObject getData(String ApiLink) throws WeatherFetchingException{
        StringBuilder result = new StringBuilder();
        try{
        URL url = URI.create(ApiLink).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            return new JSONObject(result.toString());
        }
        catch(IOException ioException){
            throw new WeatherFetchingException("Error occured with API connection");
        }
    }
}
