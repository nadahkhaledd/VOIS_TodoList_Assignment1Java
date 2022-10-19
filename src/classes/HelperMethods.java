package classes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperMethods {

    public static Date convertStringToDate(String dateString){
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
            return date;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidDate(String dateValue){
        try{
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            date.setLenient(false);
            date.parse(dateValue);
            return true;
        }
        catch (ParseException e){
            System.out.println("invalid date format");
            return false;
        }
    }

    public static boolean isValidEndDate(Date startDate, String dateString){
        if(isValidDate(dateString)){
            Date endDate = HelperMethods.convertStringToDate(dateString);
            if(endDate.compareTo(startDate) != -1)
                return true;
            else{
                System.out.println("end date is incorrect.");
                return false;
            }
        }
        return false;
    }
}
