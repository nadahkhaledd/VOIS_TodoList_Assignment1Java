package model;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public boolean addUser(String name) {
        return userRepository.createUser(name);
    }
}
