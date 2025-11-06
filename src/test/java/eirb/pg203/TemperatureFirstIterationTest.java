package eirb.pg203;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TemperatureFirstIterationTest {
    WeatherAPI weather = new WeatherAPI();

    /* Testing the return type of getTemperature function */
    @Test
    public void getTempratureTest1() throws IOException, ArgumentException{
        JSONObject aJsonObject = weather.getFullData("Paris");
        FirstIteration fIteration = new FirstIteration(aJsonObject);
        Object temperatures = fIteration.getTemperatures();

        Assertions.assertTrue(temperatures instanceof JSONArray, "L'objet n'est pas de type JSONArray");
    }

    /* Testing getTemperature function to make sure that all targeted
     * datas have been selected correctly 
    */
    @Test
    public void getTemperatureTest2() throws IOException, ArgumentException {
        JSONObject aJsonObject = weather.getFullData("Paris");
        FirstIteration fIteration = new FirstIteration(aJsonObject);
        JSONArray temperatures = fIteration.getTemperatures();

        Assertions.assertNotNull(temperatures, "Le tableau JSON retourné est null");
        Assertions.assertFalse(temperatures.isEmpty(), "Le tableau JSON retourné est vide");

        for (int i = 1; i < temperatures.length(); i++) {
            JSONObject element = temperatures.getJSONObject(i);

            Assertions.assertTrue(element.has("date"), "L'élément " + i + " ne contient pas la clé 'date'");
            Assertions.assertTrue(element.has("temperature"), "L'élément " + i + " ne contient pas la clé 'temperature'");

            String date = element.getString("date");
            Assertions.assertTrue(date.startsWith("J+"), "La date est incorrecte : " + date);

            String temperature = element.getString("temperature");
            Assertions.assertTrue(temperature.endsWith("°"), "La température n'a pas le bon format : " + temperature);
        }
}
}