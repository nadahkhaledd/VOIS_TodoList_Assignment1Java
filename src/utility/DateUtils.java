package utility;

import ui.Font;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    Font font = new Font();

    public boolean isValidDate(String dateValue){
        try{
            long dashesCount=dateValue.chars().filter(ch -> ch =='-').count();
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

    public Date changeFormat(String format, Date date){
        SimpleDateFormat DateFormat = new SimpleDateFormat(format);
        Date formattedDate = convertStringToDate(DateFormat.format(date));
        return formattedDate;
    }
}
