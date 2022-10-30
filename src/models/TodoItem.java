package models;

import ui.Font;
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
        Font font = new Font();
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        String result =
        font.ANSI_BLUE + "--------------------------------------------------------------------------\n" + font.ANSI_RESET;
        if(isFavorite)
            result += font.SET_BOLD_TEXT + font.ANSI_YELLOW+"                                                         Favorite" +"\uD83E\uDD29"+ font.SET_PLAIN_TEXT+"\n";
            result+=    font.SET_BOLD_TEXT + font.ANSI_BLUE + title + font.SET_PLAIN_TEXT +  font.ANSI_RESET +
                font.SET_BOLD_TEXT +"\t\tPriority: " + font.SET_PLAIN_TEXT + priority+
                font.SET_BOLD_TEXT + "\t\tCategory: " + font.SET_PLAIN_TEXT + category+
                font.SET_BOLD_TEXT +" \n Start Date: " + font.SET_PLAIN_TEXT + formatter.format(startDate) +
                font.SET_BOLD_TEXT +"\t\t\t End Date: "+ font.SET_PLAIN_TEXT + formatter.format(endDate) +
                font.SET_BOLD_TEXT +"\n Description: "+ font.SET_PLAIN_TEXT + description;


                result += font.ANSI_BLUE + "\n-------------------------------------------------------------------------\n" + font.ANSI_RESET;
                return result;
    }
}
//🤩