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
