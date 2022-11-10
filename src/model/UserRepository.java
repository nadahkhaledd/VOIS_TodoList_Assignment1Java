package model;

import connection.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepository {
    Connection connection;
    Statement stmt;

    public UserRepository() {
        connection = DBConnection.configureConnection();
        try {
            stmt = connection.createStatement();
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }

    public boolean createUser(String name) {
        String insertQuery = "INSERT INTO `todolist`.`user` (`name`) VALUES ('"+name+"');";
        try {
            int result = stmt.executeUpdate(insertQuery);
            return result > 0;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean updateUsersName(String name, String newName){
        String updateStatement = "UPDATE todolist.user SET name= '" + newName+
                "'\nWHERE name= '" + name + "';";

        try {
            int result = stmt.executeUpdate(updateStatement);
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
