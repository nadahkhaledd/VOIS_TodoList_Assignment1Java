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
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public ArrayList<String> getUserNames() {
        ResultSet result = null;
        ArrayList<String> usernames = new ArrayList<>();
        try {
            result = stmt.executeQuery(" SELECT name \n" +
                    "FROM todolist.user");

            while (result.next())
                usernames.add(result.getString(1));
        } catch (SQLException e) {
            System.out.println(e);
        }

        return usernames;
    }

    public ResultSet getUserTodos(String username) {
        ResultSet result = null;
        try {
            result = stmt.executeQuery("SELECT t.title, t.description, " +
                    "t.priority, t.category, t.startDate, t.endDate, t.isFavorite \n" +
                    "FROM todolist.user as u LEFT OUTER JOIN todolist.todoitem as t\n" +
                    "ON t.userId = u.iduser \n " +
                    "WHERE u.name = '" + username + "'");

        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

    public ResultSet getUserLatestTodos(String username) {
        ResultSet result = null;
        try {
            result = stmt.executeQuery("SELECT t.title, t.description, t.priority, t.category, t.startDate, t.endDate, t.isFavorite\n" +
                    "FROM todolist.user as u LEFT OUTER JOIN todolist.todoitem as t\n" +
                    "ON t.userId = u.iduser\n" +
                    "WHERE u.name = '" + username + "'\n" +
                    "ORDER BY t.endDate\n" +
                    "LIMIT 5");

        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;

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
