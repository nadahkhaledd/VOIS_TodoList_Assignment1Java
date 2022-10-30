package storage;

import models.User;

import java.io.*;

public class FileStorage implements Storage {
    @Override
    public User loadData() {
        User user;
        try (FileInputStream fis = new FileInputStream("data.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)){
                user = (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return user;
    }

    @Override
    public void saveData(User user) {
        try (FileOutputStream fos = new FileOutputStream("data.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(user);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
