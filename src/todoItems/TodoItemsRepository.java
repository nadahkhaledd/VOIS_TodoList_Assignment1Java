package todoItems;

import connection.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TodoItemsRepository {
    Connection connection;
    Statement stmt;

    public TodoItemsRepository() {
        connection = DBConnection.configureConnection();
        try {
            stmt = connection.createStatement();
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }
    public boolean createUserTodo(String name, TodoItem item){
        java.sql.Date convertedStartDate = new java.sql.Date(item.getStartDate().getTime());
        java.sql.Date convertedEndDate = new java.sql.Date(item.getEndDate().getTime());
        String insertQuery = "INSERT INTO `todolist`.`todoitem` (`title`, `description`, `priority`, `category`," +
                "`startDate`, `endDate`, `isFavorite`, `userId`)" +
                "VALUES ('"+item.getTitle()+"'," +
                "'"+item.getDescription()+"'," +
                "'"+item.getPriority()+"'," +
                "'"+item.getCategory()+"'," +
                "'"+convertedStartDate+"'," +
                "'"+convertedEndDate+"'," +
                "0, "+
                "(SELECT iduser FROM `todolist`.`user` WHERE name = '"+name+"'));";
        try {
            int result = stmt.executeUpdate(insertQuery);
            return result > 0;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public ResultSet getUserTodos(String username){
        ResultSet result = null;
        try {
            result = stmt.executeQuery("SELECT t.title, t.description, " +
                    "t.priority, t.category, t.startDate, t.endDate, t.isFavorite \n" +
                    "FROM todolist.user as u LEFT OUTER JOIN todolist.todoitem as t\n" +
                    "ON t.userId = u.iduser \n " +
                    "WHERE u.name = '" + username + "'");

        }
        catch (SQLException e){
            System.out.println(e);
        }
        return result;
    }

    public ArrayList<String> getUserNames(){
        ResultSet result = null;
        ArrayList<String> usernames = new ArrayList<>();
        try {
            result = stmt.executeQuery(" SELECT name \n" +
                    "FROM todolist.user");

            while (result.next())
                usernames.add(result.getString(1));
        }
        catch (SQLException e){
            System.out.println(e);
        }

        return usernames;
    }

//    public void closeConnection(){
//        try {
//            connection.close();
//        }
//        catch (SQLException e){
//            System.out.println(e);
//        }
//    }
}
