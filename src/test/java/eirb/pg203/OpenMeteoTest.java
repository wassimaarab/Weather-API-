package eirb.pg203;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class OpenMeteoTest {

    OpenMeteo weather= new OpenMeteo();
    
    /* Testing getFullData function for OpenMeteo for an argument that is not a valid city */
    @Test
    public void getFullDataOpenMeteoTest1() {
            weather.getFullData("regergfe");    
            assertNull(weather.aJsonObject);
    }

    /* Testing getFullData function for OpenMeteo for an empty argument  */
    @Test
    public void getFullDataOpenMeteoTest2() {
        weather.getFullData("");
        assertNull(weather.aJsonObject);
    }

    /* Testing getFullData function for OpenMeteo to make sure of its return type */
    @Test
    public void getFullDataOpenMeteoTest3() throws IOException, ArgumentException {

        Object targetedData = weather.getFullData("Paris");

        Assertions.assertTrue(targetedData instanceof JSONObject, "L'objet n'est pas de type JSONObject");

    }

    /* ------------------------------------ */

    /* Testing getTargetedData function for OpenMeteo to make sure of its return type */
    @Test
    public void getTargetedDataOpenMeteoTest1() throws IOException, ArgumentException{
        weather.aJsonObject = weather.getFullData("Paris");
        weather.getTargetedData();

        Assertions.assertTrue(weather.targetData instanceof JSONArray, "L'objet n'est pas de type JSONArray");
    }

    /* Testing getTargetedData function for OpenMeteo to make sure that all targeted
     * datas have been selected correctly
     */
    @Test
    public void getTargetedDataOpenMeteoTest2() throws IOException, ArgumentException {
        weather.aJsonObject = weather.getFullData("Paris");
        weather.getTargetedData();

        Assertions.assertNotNull(weather.targetData, "Le tableau JSON retourné est null");
        Assertions.assertFalse(weather.targetData.isEmpty(), "Le tableau JSON retourné est vide");

        JSONObject nameObject = weather.targetData.getJSONObject(0);
        Assertions.assertTrue(nameObject.has("name"), "Le premier élément ne contient pas la clé 'name'");
        Assertions.assertEquals(weather.apiName, nameObject.getString("name"), "Le nom de l'API est incorrect");

        for (int i = 1; i < weather.targetData.length(); i++) {
            JSONObject element = weather.targetData.getJSONObject(i);

            Assertions.assertTrue(element.has("date"), "L'élément " + i + " ne contient pas la clé 'date'");
            Assertions.assertTrue(element.has("temperature"), "L'élément " + i + " ne contient pas la clé 'temperature'");
            Assertions.assertTrue(element.has("wind"), "L'élément " + i + " ne contient pas la clé 'wind'");


            String date = element.getString("date");
            Assertions.assertTrue(date.startsWith("J+"), "La date est incorrecte : " + date);

            String temperature = element.getString("temperature");
            Assertions.assertTrue(temperature.endsWith("°"), "La température n'a pas le bon format : " + temperature);

            String wind = element.getString("wind");
            Assertions.assertTrue(wind.endsWith("km/h"), "La vitesse du vent n'a pas le bon format : " + wind);
        }
}

}