package classes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class HelperMethods {
    public static boolean isValidDate(String dateValue){
        try{
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            date.setLenient(false);
            date.parse(dateValue);
            Date dateAfterParsing = convertStringToDate(dateValue);
            Date startDateLimit = convertStringToDate("1-1-2000");
            if(dateAfterParsing.compareTo(startDateLimit)!= -1)
                return true;
            else {
                System.out.println("Enter date starting from 1/1/2000");
                return false;
            }
        }
        catch (ParseException e){
            System.out.println("invalid date format");
            return false;
        }
    }


    private static Date getPreviousDate(int days) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }

    public static boolean isValidEndDate(Date startDate, String dateString){
        if(isValidDate(dateString)){
            Date endDate = convertStringToDate(dateString);
            if(endDate.compareTo(startDate) != -1)
                return true;
            else{
                System.out.println("End date must be after start date.");
                return false;
            }
        }
        return false;
    }
    
    public static Date convertStringToDate(String dateString){
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    //while(currentUser.getItemByTitle()){}

    public static String validateGetStringInput(String message){// used to make sure that user input(string) is not empty or not only just ' ' character
        Scanner data = new Scanner(System.in);
        // System.out.println("Hello, what is your name?");
        String userInput = data.nextLine();
        while(userInput.matches(" +")|| userInput.isEmpty()){
            System.out.println(message);
            userInput=data.nextLine();
        }
        return userInput;
    }

    public static int validateGetIntegerInput(String message, int startLimit, int endLimit) {
        Scanner data = new Scanner(System.in);
        String userInput = data.nextLine();
        while(!userInput.matches("\\d+")
                || Integer.parseInt(userInput) < startLimit
                || Integer.parseInt(userInput) > endLimit) {
            System.out.println(message);
            userInput = data.nextLine();
        }
        return Integer.parseInt(userInput);
    }
}
