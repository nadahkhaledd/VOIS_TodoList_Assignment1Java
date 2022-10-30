package storage;

import models.User;
public interface Storage {
    User loadData();
    void saveData(User user);
}
