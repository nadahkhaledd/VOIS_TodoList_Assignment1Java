package storage;

import models.User;

import java.io.*;
import java.util.ArrayList;

public class FileStorage implements Storage {
    @Override
    public ArrayList<User> loadData() {
        ArrayList<User> users;
        try (FileInputStream fis = new FileInputStream("data.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)){
                users = (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return users;
    }

    @Override
    public void saveData(ArrayList<User> users) {
        try (FileOutputStream fos = new FileOutputStream("data.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(users);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
