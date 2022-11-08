package storage;

import model.User;

import java.util.ArrayList;

public interface Storage {
    ArrayList<User> loadData();
    void saveData(ArrayList<User> users);
}
