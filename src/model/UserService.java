package model;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public boolean updateUsersName(String name, String newName){
        boolean updated = userRepository.updateUsersName(name,newName);
        if(updated){
            name = newName;
            System.out.println("YOUR NAME WAS UPDATED SUCCESSFULLY "+ newName.toUpperCase());
            return true;
        }
        return false;
    }
}
