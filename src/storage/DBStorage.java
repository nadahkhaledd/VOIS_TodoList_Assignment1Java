package storage;

import enums.Category;
import enums.Priority;
import todoItems.TodoItem;
import model.User;
import todoItems.TodoItemsRepository;
import utility.DateUtils;
import utility.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBStorage implements Storage{
    TodoItemsRepository repository;

    public DBStorage(){
        repository = new TodoItemsRepository();
    }


    private User setUserData(String username) {
        ResultSet result = repository.getUserTodos(username);
        DateUtils dateUtils = new DateUtils();
        Utils utils = new Utils();
        User user = null;
        try {
            TodoItem todo;
            user = new User(username);
            while (result.next()){
                String currentFormat = "dd-MM-yyyy";
                todo = new TodoItem();
                todo.setTitle(result.getString("title"));
                todo.setDescription(result.getString("description"));
                todo.setPriority(Priority.valueOf(utils.capitalizeFirstLetter(result.getString("priority"))));
                todo.setCategory(Category.valueOf(utils.capitalizeFirstLetter(result.getString("category"))));
                todo.setFavorite(result.getInt("isFavorite") == 1);
                todo.setStartDate(dateUtils.changeFormat(currentFormat, result.getDate("startDate")));
                todo.setEndDate(dateUtils.changeFormat(currentFormat, result.getDate("endDate")));
                user.addTodoItem(todo);
                //System.out.println(todo);
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return user;
    }

    @Override
    public ArrayList<User> loadData() {
        ArrayList<User> users = new ArrayList<>();
        ResultSet userNames = repository.getUserNames();
        try {
            while (userNames.next()){
                User user = setUserData(userNames.getString("name"));
                users.add(user);
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return users;
    }

    @Override
    public void saveData(ArrayList<User> users) {

    }
}
