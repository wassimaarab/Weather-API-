package eirb.pg203;


/**
 * This class handles the argument mentionned in the command line
 */
public class Arguments{

    /**
 * This function return the CityName mentionnned in the args
 * @param args : a Sting with the form "-l CityName" 
 * @return CityName
 * @throws ArgumentException : if there is no city mentionned OR No -l option 
 * 
 */
    static String getLocalisation(String[] args) throws ArgumentException{
        if(args.length!=2){
            throw new ArgumentException("ERROR Localisation: No city mentionned. Please include \"-l CityName\" in your command lignee");
        }

        if(args[0].equals("-l")){
            return args[1];
        }
        else{
            throw new ArgumentException("ERROR Localisation: No city mentionned. Please include \"-l CityName\" in your command lignee");
        }
    }
}