package todoItems;

import connection.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public ResultSet getUserTodos(String username){
        ResultSet result = null;
        try {
            result = stmt.executeQuery("SELECT u.name, t.title, t.description, " +
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

    public ResultSet getUserNames(){
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
}
