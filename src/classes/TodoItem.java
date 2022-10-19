package classes;

import enums.Category;
import enums.Priority;

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
        this.title = title;
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
        this.title = title;
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
    public String toString() {
        return "TodoItem{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", category=" + category +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
