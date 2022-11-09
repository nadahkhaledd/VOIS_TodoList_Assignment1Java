package todoItems;

import enums.Category;
import enums.Priority;
import enums.SearchKey;
import ui.Font;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class TodoItemsService {
    private final TodoItemsRepository todoItemsRepository;
    private Font font;


    public TodoItemsService(TodoItemsRepository todoItemsRepository) {
        this.todoItemsRepository = todoItemsRepository;
        this.font = new Font();

    }

    public ArrayList<TodoItem> getItemsByPriority(Priority priority,ArrayList<TodoItem> userItems) {
        ArrayList<TodoItem> result = (ArrayList<TodoItem>) userItems.stream().filter(item -> item.getPriority() == priority).collect(Collectors.toList());
        return result;
    }
    public ArrayList<TodoItem> getItemsByFavorite(ArrayList<TodoItem> userItems) {
        ArrayList<TodoItem> favorites = (ArrayList<TodoItem>) userItems.stream().filter(TodoItem::isFavorite).collect(Collectors.toList());
        return favorites;
    }
    public ArrayList<TodoItem> getItemsByStartDate(Date startDate,ArrayList<TodoItem> userItems) {

        return null;
    }
    public ArrayList<TodoItem> getItemsByEndDate(Date endDate,ArrayList<TodoItem> userItems){

        return null;
    }
    public boolean addTodoItem(String id,TodoItem item,ArrayList<TodoItem> userTodoItems){
        return false;
    }
    public boolean updateTodoItem(String id,TodoItem item, String oldTitle,ArrayList<TodoItem> userTodoItems){
        //repo.update
        return false;
    }
    public boolean deleteTodoItem(String id,String title,ArrayList<TodoItem> userTodoItems){
        //repo.delete
        return false;
    }
    public void showAllTodoItems(ArrayList<TodoItem> userTodoItems){
        printListItems(userTodoItems.size(),userTodoItems);
    }
    public void showTop5ItemsByDate(){
     //repo.showw
    }
    private void printListItems(int lastIndex,ArrayList<TodoItem> userTodoItems){
        for(int i=0; i<lastIndex; i++){
            System.out.println(userTodoItems.get(i).toString());
        }
    }
    private int getItemByTitle(String title,ArrayList<TodoItem> userTodoItems){
        for(int i=0; i<userTodoItems.size(); i++) {
            if(userTodoItems.get(i).getTitle().equalsIgnoreCase(title)){
                return i;
            }
        }
        return -1;
    }
    public void searchShowItemsBySearchKey(SearchKey searchKey, String searchValue,ArrayList<TodoItem> userTodoItems){
        ArrayList<TodoItem> returnedItems = new ArrayList<>();
        switch (searchKey){
            case Title:
                int returnedIndex = getItemByTitle(searchValue,userTodoItems);
                if(returnedIndex != -1)
                    returnedItems.add(userTodoItems.get(returnedIndex));
                break;

            case Priority:
                returnedItems = getItemsByPriority(Priority.valueOf(searchValue),userTodoItems);
                break;

            case StartDate:
                try{
                    Date startDate=new SimpleDateFormat("dd-MM-yyyy").parse(searchValue);
                    returnedItems = getItemsByStartDate(startDate,userTodoItems);
                }
                catch (ParseException e){
                    System.out.println(font.ANSI_RED + "invalid date format" + font.ANSI_RESET);
                }
                break;

            case EndDate:
                try{
                    Date endDate=new SimpleDateFormat("dd-MM-yyyy").parse(searchValue);
                    returnedItems = getItemsByEndDate(endDate,userTodoItems);
                }
                catch (ParseException e){
                    System.out.println(font.ANSI_RED + "invalid date format" + font.ANSI_RESET);
                }
                break;

            case Favorite:
                returnedItems = getItemsByFavorite(userTodoItems);
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
    public void addItemToFavorite(String id,String title){
        //repo.add
    }

    public void printFavorites(ArrayList<TodoItem> userTodoItems){
        searchShowItemsBySearchKey(SearchKey.Favorite, "true",userTodoItems);
    }

    public void addItemToCategory(String id,String title, Category category){
        //repo.add


    }


}
