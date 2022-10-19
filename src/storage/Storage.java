package storage;

import classes.User;
public interface Storage {
    User loadData();
    void saveData(User user);
}
