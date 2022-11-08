package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{
    public static Connection configureConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist", "root", "P@ssw0rd");
        }
        catch (ClassNotFoundException | SQLException c){
            System.out.println(c);
        }

        return connection;
    }
}
