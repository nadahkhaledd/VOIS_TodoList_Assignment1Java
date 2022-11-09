package storage;

import enums.Category;
import enums.Priority;
import todoItems.TodoItem;
import model.User;
import todoItems.TodoItemsRepository;
import todoItems.TodoItemsService;
import utility.DateUtils;
import utility.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBStorage implements Storage{
    TodoItemsRepository repository;
    TodoItemsService itemsService;

    public DBStorage(){
        repository = new TodoItemsRepository();
        itemsService = new TodoItemsService();
    }


    private User setUserData(String username) {
        ResultSet result = repository.getUserTodos(username);
        User user = new User(username);
        user.setItems(itemsService.getTodosFromDB(result));
        return user;
    }

    @Override
    public ArrayList<User> loadData() {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<String> userNames = repository.getUserNames();
        System.out.println("usernames: " + userNames);
        for (String username: userNames){
            User user = setUserData(username);
            users.add(user);
        }
        for (User user: users) {
            System.out.println("users");
            System.out.println(user.getName() + " "+user.getItems());
        }
        return users;
    }

    @Override
    public void saveData(ArrayList<User> users) {

    }
}
