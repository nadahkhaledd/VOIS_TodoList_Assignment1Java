package storage;

import classes.User;

import java.io.*;

public class FileStorage implements Storage{
    @Override
    public User loadData() {
        User user;
        try (FileInputStream fis = new FileInputStream("data.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)){
                user = (User) ois.readObject();
                ois.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public void saveData(User user) {
        try (FileOutputStream fos = new FileOutputStream("data.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(user);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
