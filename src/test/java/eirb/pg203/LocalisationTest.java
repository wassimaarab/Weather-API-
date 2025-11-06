package eirb.pg203;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalisationTest {
    
    /* Testing getLocalisation function in its normal functioning */
    @Test
    public void getLocalisationTest1() throws ArgumentException {
        String[] loc = {"-l", "Bordeaux"};
        String localisation = Arguments.getLocalisation(loc);

        Assertions.assertEquals("Bordeaux", localisation);
    }

    /* Testing getLocalisation function for empty arguments */
    @Test
    public void getLocalisationTest2() throws ArgumentException {
        String[] loc = {};
        
        Assertions.assertThrows(ArgumentException.class, () -> {
            Arguments.getLocalisation(loc);
        });
    }

    /* Testing getLocalisation function for only one argument */
    @Test
    public void getLocalisationTest3() throws ArgumentException {
        String[] loc = {"-l"};
        
        Assertions.assertThrows(ArgumentException.class, () -> {
            Arguments.getLocalisation(loc);
        });
    }

    /* Testing getLocalisation function for three arguments */
    @Test
    public void getLocalisationTest4() throws ArgumentException {
        String[] loc = {"-l", "Bordeaux", "Paris"};
        
        Assertions.assertThrows(ArgumentException.class, () -> {
            Arguments.getLocalisation(loc);
        });
    }

    /* Testing getLocalisation function for two arguments but not in the correct format */
    @Test
    public void getLocalisationTest5() throws ArgumentException {
        String[] loc = {"Bordeaux", "-l"};
        
        Assertions.assertThrows(ArgumentException.class, () -> {
            Arguments.getLocalisation(loc);
        });
    }
}
