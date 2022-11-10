import app.Simulator;
import connection.DBConnection;
import todoItems.TodoItemsRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.start();
    //    DBConnection.configureConnection();

    }
}
