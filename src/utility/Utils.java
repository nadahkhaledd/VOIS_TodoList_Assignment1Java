package utility;

import ui.Font;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Utils {
    Font font = new Font();

    public void PrintColoredMessage(String color, String message){
        System.out.println(color + message + font.ANSI_RESET);
    }

    public void print(String message){
        System.out.println(message);
    }

    public boolean isValidDate(String dateValue){
        try{
            long dashesCount=dateValue.chars().filter(ch -> ch =='-').count();
            //System.out.println("in valid date "+dashesCount);
            if(dashesCount>=3){
                throw new ParseException("invalid date format",15);
            }
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            date.setLenient(false);
            date.parse(dateValue);
            Date dateAfterParsing = convertStringToDate(dateValue);
            Date startDateLimit = convertStringToDate("1-1-2000");
            if(dateAfterParsing.compareTo(startDateLimit)!= -1)
                return true;
            else {
                System.out.println(font.ANSI_RED + "Enter date starting from 1/1/2000" + font.ANSI_RESET);
                return false;
            }
        }
        catch (ParseException e){
            System.out.println(font.ANSI_RED +"invalid date format"+ font.ANSI_RESET);
            return false;
        }
    }

    public boolean isValidEndDate(Date startDate, String dateString){
        if(isValidDate(dateString)){
            Date endDate = convertStringToDate(dateString);
            if(endDate.compareTo(startDate) != -1)
                return true;
            else{
                System.out.println(font.ANSI_RED +"End date must be after start date."+ font.ANSI_RESET);
                return false;
            }
        }
        return false;
    }

    public Date convertStringToDate(String dateString){
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getInput(String message){// used to make sure that user input(string) is not empty or not only just ' ' character
        Scanner data = new Scanner(System.in);
        // System.out.println("Hello, what is your name?");
        String userInput = data.nextLine();
        userInput=userInput.trim();
       // System.out.println("in validation :: "+userInput);
        while(userInput.matches(" +")|| userInput.isEmpty()){
            System.out.println(font.ANSI_RED + message + font.ANSI_RESET);
            userInput=data.nextLine();
        }
        return userInput;
    }

    public int getInput(String message, int startLimit, int endLimit) {
        Scanner data = new Scanner(System.in);
        String userInput = data.nextLine();
        userInput=userInput.trim();
        while(!userInput.matches("\\d+")
                || Integer.parseInt(userInput) < startLimit
                || Integer.parseInt(userInput) > endLimit) {
            System.out.println(font.ANSI_RED +message+ font.ANSI_RESET);
            userInput = data.nextLine();
        }
        return Integer.parseInt(userInput);
    }
}
