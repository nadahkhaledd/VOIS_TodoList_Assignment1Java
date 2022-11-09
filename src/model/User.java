package model;

import todoItems.TodoItem;
import todoItems.TodoItemsRepository;
import todoItems.TodoItemsService;
import ui.Font;
import enums.Category;
import enums.Priority;
import enums.SearchKey;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class User implements Serializable {
    private String name;
    ArrayList<TodoItem> items;

    TodoItemsRepository repository;
    TodoItemsService itemsService;
    private Font font;

    public User(String name) {
        this.name = name;
        this.font = new Font();
        this.items = new ArrayList<>();
        this.repository = new TodoItemsRepository();
        this.itemsService = new TodoItemsService(this.repository);
    }

    public ArrayList<TodoItem> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemByTitle(String title){
        for(int i=0; i<this.items.size(); i++) {
            if(this.items.get(i).getTitle().equalsIgnoreCase(title)){
                return i;
            }
        }
        return -1;
    }
    public boolean itemExists(String title){
        return getItemByTitle(title)!=-1;
    }

//    private ArrayList<TodoItem> getItemsByPriority(Priority priority){
//        ArrayList<TodoItem> result = new ArrayList<>();
//        for(int i=0; i<items.size(); i++) {
//            if(items.get(i).getPriority().equals(priority)){
//                result.add(items.get(i));
//            }
//        }
//        return result;
//    }

//    private ArrayList<TodoItem> getItemsByFavorite(){
//        ArrayList<TodoItem> result = new ArrayList<>();
//        for(int i=0; i<items.size(); i++) {
//            if(items.get(i).isFavorite()){
//                result.add(items.get(i));
//            }
//        }
//        return result;
//    }


//    private ArrayList<TodoItem> getItemsByStartDate(Date startDate){
//        ArrayList<TodoItem> result = new ArrayList<>();
//        for(int i=0; i<items.size(); i++) {
//            if(items.get(i).getStartDate().equals(startDate)){
//                result.add(items.get(i));
//            }
//        }
//        return result;
//    }

//    private ArrayList<TodoItem> getItemsByEndDate(Date endDate){
//        ArrayList<TodoItem> result = new ArrayList<>();
//        for(int i=0; i<items.size(); i++) {
//            if(items.get(i).getEndDate().equals(endDate)){
//                result.add(items.get(i));
//            }
//        }
//        return result;
//    }

    public boolean addTodoItem(TodoItem item){
        int itemIndex = getItemByTitle(item.getTitle());
        if(itemIndex == -1){
            this.items.add(item);
            System.out.println("Item added successfully.");
            return true;
        }
        else {
            System.out.println(font.ANSI_RED + "items with this title already exists." + font.ANSI_RESET);
            return false;
        }
    }

    public void updateTodoItem(TodoItem item, String oldTitle){
        int oldItemIndex = getItemByTitle(oldTitle);
        this.items.set(oldItemIndex, item);
        System.out.println("item updated successfully");

    }

    public boolean deleteTodoItem(String title){
        int foundItemIndex = getItemByTitle(title);
        if(foundItemIndex!=-1){
            items.remove(foundItemIndex);
            System.out.println("Item deleted successfully.");
            return true;
        }
        System.out.println(font.ANSI_RED + "Item couldn't be deleted" + font.ANSI_RESET);
        return false;
    }

    public void showAllTodoItems(){
        printListItems(items.size());
    }

    private void sortTodoItemsByDate(){
        System.out.println("sorting.....");
        for(int i=0; i<items.size(); i++){
            for(int j=i+1; j<items.size(); j++){
                if(items.get(j).getEndDate().compareTo(items.get(i).getEndDate()) == 1){
                    TodoItem temp = items.get(i);
                    items.set(i, items.get(j));
                    items.set(j, temp);
                }
            }
        }
    }

    private void printListItems(int lastIndex){
        for(int i=0; i<lastIndex; i++){
            System.out.println(items.get(i).toString());
        }
    }

    public void showTop5ItemsByDate(){
        if(items.isEmpty())
            System.out.println( font.ANSI_RED + "sorry, no items available." + font.ANSI_RESET);
        else{
            int lastIndex = (items.size() >= 5) ? 5: items.size();
            sortTodoItemsByDate();
            printListItems(lastIndex);
        }
    }

    public void searchShowItemsBySearchKey(SearchKey searchKey, String searchValue){
        ArrayList<TodoItem> returnedItems = new ArrayList<>();
        switch (searchKey){
            case Title:
                int returnedIndex = getItemByTitle(searchValue);
                if(returnedIndex != -1)
                    returnedItems.add(items.get(returnedIndex));
                break;

            case Priority:
                returnedItems = itemsService.getItemsByPriority(Priority.valueOf(searchValue), this.getItems());
                break;

            case StartDate:
                try{
                    Date startDate=new SimpleDateFormat("dd-MM-yyyy").parse(searchValue);
                    returnedItems = itemsService.getItemsByStartDate(startDate, this.getItems());
                }
                catch (ParseException e){
                    System.out.println(font.ANSI_RED + "invalid date format" + font.ANSI_RESET);
                }
                break;

            case EndDate:
                try{
                    Date endDate=new SimpleDateFormat("dd-MM-yyyy").parse(searchValue);
                    returnedItems = itemsService.getItemsByEndDate(endDate, this.getItems());
                }
                catch (ParseException e){
                    System.out.println(font.ANSI_RED + "invalid date format" + font.ANSI_RESET);
                }
                break;

            case Favorite:
                returnedItems = this.itemsService.getItemsByFavorite(this.getItems());
                break;
        }

        if (returnedItems.isEmpty()) {
            System.out.println(font.ANSI_RED + "No results found." + font.ANSI_RESET);
        }
        else {
            for(TodoItem item: returnedItems){
                System.out.println(item.toString());
            }
        }
    }

    public void addItemToFavorite(String title){
        int itemIndex = getItemByTitle(title);
            items.get(itemIndex).setFavorite(true);
    }

    public void printFavorites(){
        searchShowItemsBySearchKey(SearchKey.Favorite, "true");
    }

    public void addItemToCategory(String title, Category category){
        int itemIndex = getItemByTitle(title);
            items.get(itemIndex).setCategory(category);
            System.out.println("Item added to category " + category);

    }
}
