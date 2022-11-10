package todoItems;

import enums.Category;
import enums.Priority;
import enums.SearchKey;
import ui.Font;
import utility.DateUtils;
import utility.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoItemsService {
    private final TodoItemsRepository todoItemsRepository;
    private Font font;


    public TodoItemsService(TodoItemsRepository todoItemsRepository) {
        this.todoItemsRepository = todoItemsRepository;
        this.font = new Font();

    }

    public ArrayList<TodoItem> getItemsByPriority(Priority priority,ArrayList<TodoItem> userItems) {
        ArrayList<TodoItem> result = new ArrayList<>();
        for(int i=0; i<userItems.size(); i++) {
            if(userItems.get(i).getPriority().equals(priority)){
                result.add(userItems.get(i));
            }
        }
        return result;
    }
    public ArrayList<TodoItem> getItemsByFavorite(ArrayList<TodoItem> userItems) {
        //repository.getFavorites(id);
        return null;
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
    public boolean updateTodoItem(String name,TodoItem item, String oldTitle,ArrayList<TodoItem> userTodoItems){
        //repo.update
        return todoItemsRepository.updateTodoItem(name,item,oldTitle);

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
    public boolean addItemToFavorite(String name,String title,ArrayList<TodoItem> userTodoItems){
        boolean updated = todoItemsRepository.addItemToFavorite(name,title);
        if(updated) {
            int itemIndex = getItemByTitle(title, userTodoItems);
            userTodoItems.get(itemIndex).setFavorite(true);
            System.out.println("ADDED TO FAVORITES SUCCESSFULLY");
        }
        return updated;
    }

    public void printFavorites(ArrayList<TodoItem> userTodoItems){
        searchShowItemsBySearchKey(SearchKey.Favorite, "true",userTodoItems);
    }

    public boolean addItemToCategory(String name,String title, Category category,ArrayList<TodoItem> userTodoItems){
        boolean updated = todoItemsRepository.addItemToCategory(name,title,category);
        if(updated) {
            int itemIndex = getItemByTitle(title,userTodoItems);
            userTodoItems.get(itemIndex).setCategory(category);
            System.out.println("ADDED TO CATEGORY SUCCESSFULLY");
        }
        return updated;
    }
    public ArrayList<TodoItem> getTodosFromDB(ResultSet result){
        ArrayList<TodoItem> todos = new ArrayList<>();



        DateUtils dateUtils = new DateUtils();
        Utils utils = new Utils();
        try {
            TodoItem todo;
            while (result.next()){
                if(result.getString(1) == null){
                    break;
                }
                String currentFormat = "dd-MM-yyyy";
                todo = new TodoItem();
                todo.setTitle(result.getString("title"));
                todo.setDescription(result.getString("description"));
                todo.setPriority(Priority.valueOf(utils.capitalizeFirstLetter(result.getString("priority"))));
                todo.setCategory(Category.valueOf(utils.capitalizeFirstLetter(result.getString("category"))));
                todo.setFavorite(result.getInt("isFavorite") == 1);
                todo.setStartDate(dateUtils.changeFormat(currentFormat, result.getDate("startDate")));
                todo.setEndDate(dateUtils.changeFormat(currentFormat, result.getDate("endDate")));
                todos.add(todo);
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return todos;
    }


}
