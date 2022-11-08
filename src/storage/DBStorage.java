package storage;

import connection.DBConnection;
import enums.Category;
import enums.Priority;
import model.TodoItem;
import model.User;
import utility.DateUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DBStorage implements Storage{
    Connection connection;
    Statement stmt;

    DBStorage(){
        connection = DBConnection.configureConnection();
        try {
            stmt = connection.createStatement();
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }

    private ResultSet getRawData(String username){
        ResultSet result = null;
        try {
            result = stmt.executeQuery("SELECT u.name, t.title, t.description, " +
                    "t.priority, t.category, t.startDate, t.endDate \n" +
                    "FROM todolist.user as u LEFT OUTER JOIN todolist.todoitem as t\n" +
                    "ON t.userId = u.iduser " +
                    "WHERE u.name = " + username);
        }
        catch (SQLException e){
            System.out.println(e);
        }

        return result;
    }

    private ResultSet getUserNames(){
        ResultSet result = null;
        try {
            result = stmt.executeQuery(" SELECT name \n" +
                    "FROM todolist.user");
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return result;
    }

    private User setUserData(String username) {
        ResultSet result = getRawData(username);
        DateUtils dateUtils = new DateUtils();
        User user = null;
        try {
            TodoItem todo;
            result.next();
            user = new User(result.getString("name"));
            do {
                String currentFormat = "dd-MM-yyyy";
                todo = new TodoItem();
                todo.setTitle(result.getString("title"));
                todo.setDescription(result.getString("description"));
                todo.setPriority(Priority.valueOf(result.getString("priority")));
                todo.setCategory(Category.valueOf(result.getString("category")));
                todo.setFavorite(result.getInt("isFavorite") == 1);
                todo.setStartDate(dateUtils.changeFormat(currentFormat, result.getDate("startDate")));
                todo.setEndDate(dateUtils.changeFormat(currentFormat, result.getDate("endDate")));
                user.addTodoItem(todo);
            }
            while (result.next());
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return user;
    }

    @Override
    public ArrayList<User> loadData() {
        ArrayList<User> users = new ArrayList<>();
        ResultSet userNames = getUserNames();
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
