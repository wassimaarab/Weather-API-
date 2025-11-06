package eirb.pg203;

import java.util.Arrays;



public class Main {
    static String[] argment;
    
    public static void main(String[] args){
        argment =args;
        System.out.println("Args: " + Arrays.toString(args));
        SecondThirdIteration Iteration2 = new SecondThirdIteration(args);
        Iteration2.displayTable();
    }

    
    // https://api.openweathermap.org/data/3.0/onecall?lat=33.44&lon=-94.04&appid=2dd9a2480e01663af907d26c26181fe3
    // https://api.opencagedata.com/geocode/v1/json?q=Bordeaux&key=482503fa8b38414c97c4b1866ae52a35
}