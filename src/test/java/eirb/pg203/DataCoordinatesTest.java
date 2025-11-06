import org.junit.jupiter.api.Test ;
import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import eirb.pg203.Data;
import eirb.pg203.Cache;

class DataTest {

    // Concrete implementation of the abstract Data class for testing purposes
    class TestData extends Data {
        @Override
        public JSONObject getFullData(String localisation) {
            // A mock response for testing
            JSONObject mockData = new JSONObject();
            mockData.put("temperature", 22.5);
            return mockData;
        }

        @Override
        public void getTargetedData() {
            // Populating the targetData with some mock values
            targetData = new JSONArray();
            targetData.put(new JSONObject().put("temperature", 22.5));
        }
    }
 
    @Test
    void testArrayCheckCacheWithCacheData() {
        TestData testData = new TestData();
        testData.Location = "Bordeaux";
        testData.apiName = "WeatherAPI";
        Cache cache = new Cache();
        JSONObject cachedData = new JSONObject();
        cachedData.put("value", new JSONArray().put(new JSONObject().put("temperature", 22)));
        JSONArray targetdata = new JSONArray();
        targetdata.put(cachedData);

        cache.saveToCache(testData.Location, testData.apiName, targetdata);
        testData.arrayCheckCache(testData.apiName, testData.Location);
        assertNotNull(testData.targetData);
    }

    @Test
    void testArrayCheckCacheWithoutCacheData() {
        TestData testData = new TestData();
        testData.Location = "Paris";
        testData.apiName = "WeatherAPI";
        Cache cache = new Cache();
        JSONObject noCachedData = null;
        testData.arrayCheckCache(testData.apiName, testData.Location);
        assertNotNull(testData.targetData);
    }

    @Test
    void testNormalizeFormatPositiveValue() {
        TestData testData = new TestData();
        String formattedValue = testData.normalizeformat(25.6);
        assertEquals("25.60", formattedValue);
    }

    @Test
    void testNormalizeFormatNegativeValue() {
        TestData testData = new TestData();
        String formattedValue = testData.normalizeformat(-5.4);
        assertEquals("-05.4", formattedValue);
    }

    @Test
    void testNormalizeFormatZeroValue() {
        TestData testData = new TestData();
        String formattedValue = testData.normalizeformat(0.0);
        assertEquals("00.00", formattedValue);
    }
}
