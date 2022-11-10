package todoItems;

import connection.DBConnection;
import enums.Category;
import enums.SearchKey;

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

    public boolean deleteTodoItem(String title) {
        System.out.println("title = " + title);
        try {
            String sqlQuery = "DELETE FROM todolist.todoitem " +
                    "WHERE title = '" + title + "'";
            stmt.executeUpdate(sqlQuery);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTodoItem(String name,TodoItem item, String oldTitle) {
        String idSubQuery = "(SELECT iduser FROM todolist.user where name= '"+name+"')";
        java.sql.Date sqlStartDate = new java.sql.Date(item.getStartDate().getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(item.getEndDate().getTime());
        String updateStatement = "UPDATE todolist.todoitem SET title = '" + item.getTitle() +
                "' ,description= '" + item.getDescription() +
                "' ,priority= '" + item.getPriority() +
                "' ,category= '" + item.getCategory() +
                "' ,startDate= '" + sqlStartDate +
                "' ,endDate= '" + sqlEndDate+
                "'\nWHERE userId= " + idSubQuery + " AND title= '" + oldTitle + "';";

        try {
            int result = stmt.executeUpdate(updateStatement);
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
    public boolean addItemToFavorite(String name,String title){
        String idSubQuery = "(SELECT iduser FROM todolist.user where name= '"+name+"')";
        String updateStatement = "UPDATE todolist.todoitem SET `isFavorite` = 1"+
                "\nWHERE userId= " + idSubQuery + " AND title= '" + title + "';";

        try {
            int result = stmt.executeUpdate(updateStatement);
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addItemToCategory(String name,String title, Category category){
        String idSubQuery = "(SELECT iduser FROM todolist.user where name= '"+name+"')";

        String updateStatement = "UPDATE todolist.todoitem SET category= '" + category+
                "'\nWHERE userId= " + idSubQuery + " AND title= '" + title + "';";

        try {
            int result = stmt.executeUpdate(updateStatement);
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


}

