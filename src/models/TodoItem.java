package models;

import ui.ConsoleOptions;
import enums.Category;
import enums.Priority;

import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.util.Date;

public class TodoItem implements Serializable {
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    private Date startDate;
    private Date endDate;
    private boolean isFavorite;

    public TodoItem(String title, String description, Priority priority,
                    Category category, Date startDate, Date endDate) {
        this.title = title.trim();
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    @Override
    public String toString()
    {
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        String result =
        ConsoleOptions.ANSI_BLUE + "--------------------------------------------------------------------------\n" + ConsoleOptions.ANSI_RESET;
        if(isFavorite)
            result += ConsoleOptions.SET_BOLD_TEXT +ConsoleOptions.ANSI_YELLOW+"                                                         Favorite" +"\uD83E\uDD29"+ ConsoleOptions.SET_PLAIN_TEXT+"\n";
            result+=    ConsoleOptions.SET_BOLD_TEXT +ConsoleOptions.ANSI_BLUE + title + ConsoleOptions.SET_PLAIN_TEXT +  ConsoleOptions.ANSI_RESET +
                ConsoleOptions.SET_BOLD_TEXT +"\t\tPriority: " + ConsoleOptions.SET_PLAIN_TEXT + priority+
                ConsoleOptions.SET_BOLD_TEXT + "\t\tCategory: " + ConsoleOptions.SET_PLAIN_TEXT + category+
                ConsoleOptions.SET_BOLD_TEXT +" \n Start Date: " + ConsoleOptions.SET_PLAIN_TEXT + formatter.format(startDate) +
                ConsoleOptions.SET_BOLD_TEXT +"\t\t\t End Date: "+ ConsoleOptions.SET_PLAIN_TEXT + formatter.format(endDate) +
                ConsoleOptions.SET_BOLD_TEXT +"\n Description: "+ ConsoleOptions.SET_PLAIN_TEXT + description;


                result += ConsoleOptions.ANSI_BLUE + "\n-------------------------------------------------------------------------\n" + ConsoleOptions.ANSI_RESET;
                return result;
    }
}
//🤩