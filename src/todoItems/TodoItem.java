package todoItems;

import ui.Font;
import enums.Category;
import enums.Priority;

import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.util.Date;

public class TodoItem implements Serializable,Cloneable {
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    private Date startDate;
    private Date endDate;
    private boolean isFavorite;

    public TodoItem(){}

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
        StringBuilder result = new StringBuilder();
        result.append(font.ANSI_BLUE + "--------------------------------------------------------------------------\n" + font.ANSI_RESET);
        if(isFavorite)
            result.append(font.SET_BOLD_TEXT + font.ANSI_YELLOW+"\t\t\t\t\t\t\t\tFavorite" +"\uD83E\uDD29"+ font.SET_PLAIN_TEXT+"\n");
        result.append(font.SET_BOLD_TEXT + font.ANSI_BLUE + title + font.SET_PLAIN_TEXT)
                .append(font.ANSI_RESET).append(font.SET_BOLD_TEXT).append("\t\tPriority: ")
                .append(font.SET_PLAIN_TEXT).append(priority).append(font.SET_BOLD_TEXT)
                .append("\t\tCategory: ").append(font.SET_PLAIN_TEXT).append(category).append(font.SET_BOLD_TEXT)
                .append(" \nStart Date: ").append(font.SET_PLAIN_TEXT).append(formatter.format(startDate)).append(font.SET_BOLD_TEXT)
                .append("\t\t\t End Date: ").append(font.SET_PLAIN_TEXT).append(formatter.format(endDate)).append(font.SET_BOLD_TEXT)
                .append("\n\t" + font.SET_PLAIN_TEXT).append(description)
                .append(font.ANSI_BLUE + "\n-------------------------------------------------------------------------\n" + font.ANSI_RESET);
        return result.toString();
    }
    public TodoItem clone(){
        try {
            return (TodoItem) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }

    }
    public void updateNewItem(TodoItem newItem){

        this.title = newItem.getTitle();
        this.description = newItem.getDescription();
        this.priority = newItem.getPriority();
        this.category = newItem.getCategory();
        this.startDate = newItem.getStartDate();
        this.endDate = newItem.getEndDate();
    }
}
//🤩