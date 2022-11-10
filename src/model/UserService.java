package model;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public String updateUsersName(String name, String newName){
        boolean updated = userRepository.updateUsersName(name,newName);
        if(updated){
            System.out.println("YOUR NAME WAS UPDATED SUCCESSFULLY "+ newName.toUpperCase());
            return newName;
        }
        return null;
    }
}
