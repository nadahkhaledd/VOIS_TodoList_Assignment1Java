package todoItems;

import connection.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TodoItemsRepository {
    Connection connection;
    Statement stmt;
    public TodoItemsRepository(){
        connection = DBConnection.configureConnection();
        try {
            stmt = connection.createStatement();
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }
}
