package classes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class HelperMethods {
    public static boolean isValidDate(String dateValue){
        try{
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            date.setLenient(false);
            date.parse(dateValue);
            Date dateAfterParsing = convertStringToDate(dateValue);
            Date now = date.parse(date.format(new Date()));
            if(dateAfterParsing.compareTo(now)!= -1)
                return true;
            else {
                System.out.println(ConsoleOptions.ANSI_RED + "The start date already passed" + ConsoleOptions.ANSI_RESET);
                return false;
            }
        }
        catch (ParseException e){
            System.out.println(ConsoleOptions.ANSI_RED +"invalid date format"+ConsoleOptions.ANSI_RESET);
            return false;
        }
    }

    public static boolean isValidEndDate(Date startDate, String dateString){
        if(isValidDate(dateString)){
            Date endDate = convertStringToDate(dateString);
            if(endDate.compareTo(startDate) != -1)
                return true;
            else{
                System.out.println(ConsoleOptions.ANSI_RED +"End date must be after start date."+ConsoleOptions.ANSI_RESET);
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
    
    public static String validateGetStringInput(String message){// used to make sure that user input(string) is not empty or not only just ' ' character
        Scanner data = new Scanner(System.in);
        // System.out.println("Hello, what is your name?");
        String userInput = data.nextLine();
        while(userInput.matches(" +")|| userInput.isEmpty()){
            System.out.println(ConsoleOptions.ANSI_RED + message + ConsoleOptions.ANSI_RESET);
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
            System.out.println(ConsoleOptions.ANSI_RED +message+ConsoleOptions.ANSI_RESET);
            userInput = data.nextLine();
        }
        return Integer.parseInt(userInput);
    }
}
