package app;

import enums.Category;
import enums.Priority;
import enums.SearchKey;
import model.UserRepository;
import model.UserService;
import todoItems.TodoItem;
import model.User;
import storage.DBStorage;
import storage.Storage;
import todoItems.TodoItemsRepository;
import todoItems.TodoItemsService;
import ui.Font;
import ui.Text;
import utility.DateUtils;
import utility.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Simulator {
    private Scanner scanner = new Scanner(System.in);

    TodoItemsRepository repository;
    TodoItemsService itemsService;

    private ArrayList<User> users = new ArrayList<>();
    private User currentUser = null;
    private Storage storage;
    private Utils utils = new Utils();
    private DateUtils dateUtils = new DateUtils();
    private Font font = new Font();
    private Text text = new Text();
    private UserService userService= new UserService(new UserRepository());

    public Simulator(){
        storage = new DBStorage();
        repository = new TodoItemsRepository();
        itemsService = new TodoItemsService(this.repository);
    }

    public void start() {
        signInUser();
        showMenu();
    }

    private void signInUser() {
        if (isThereUser()) {
            User user = null;

            while (user == null) {
                System.out.println("Welcome!\n1- Sign up as a new User\n2- Sign in as an already existing user");
                int inputChoice = utils.getInput("Please enter either 1 or 2", 1, 2);

                if (inputChoice == 1) {
                    user = addNewUser(false);
                } else {
                    user = authenticateUser();
                }
                if (user == null) {
                    clearScreen();
                }
            }
            currentUser = user;
        } else {
            System.out.println("Welcome to our To-do List app, what is your name?");
            currentUser = addNewUser(true);
        }

    }

    private User authenticateUser() {

        System.out.println("Enter you name to sign in. (Press 0 to return to main page)");
        String usersName = "";

        User user = null;
        while (user == null) {
            usersName = utils.getInput("Please enter a valid name");
            if (usersName.trim().equals("0"))
                return null;
            user = getUserByUsername(usersName);
            if (user == null) {
                System.err.println("----------------------------ACCESS DENIED---------------------------\nThis name doesn't exist, try again. (Press 0 to return to main page)");
            }

        }
        return user;

    }

    private User addNewUser(boolean isFirstTime) {
        if (!isFirstTime)
            System.out.println("Enter name of new user. (Press 0 to return to main page)");
        boolean uniqueUserNameEntered = false;

        //check username exists
        //if not display message
        //else add user
        String usersName = "";
        while (!uniqueUserNameEntered) {
            usersName = utils.getInput("Please enter a valid name");
            if (usersName.trim().equals("0")) {
                return null;
            }
            if (getUserByUsername(usersName) == null)
                uniqueUserNameEntered = true;
            else {
                System.err.println("The name Entered already exists, please enter a new name. (Press 0 to return to main page)");
            }
        }
        User newUser = new User(usersName);
        users.add(newUser);
        return newUser;
        //ask youssef if break functionality must be added here

    }

    private User getUserByUsername(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    private boolean isThereUser() {
        ArrayList<User> data = storage.loadData();
        if (data.isEmpty())
            return false;
        else {
            users = data;
            return true;
        }
        //return false;
    }

    private void setUserName() {
        utils.print("Hello, what is your name?");
        String name = utils.getInput("write a valid name");
        currentUser = new User(name);
    }

    private void showMenu() {

        while (true) {
            if (currentUser == null) {
                clearScreen();
                signInUser();
            }
            utils.PrintColoredMessage(font.ANSI_YELLOW, "\nWelcome " + currentUser.getName());
            text.menuOptions.forEach(System.out::println);

            int option = utils.getInput("Invalid input", 1, 13);
            switch (option) {
                case 1:
                    TodoItem item = takeCreateItemFromUser();
                    if (item != null) {
                        currentUser.addTodoItem(item);
                        itemsService.showAllTodoItems(currentUser.getItems());
                        saveFile();
                    }
                    break;

                case 2:
                    takeUpdateItemFromUser();
                    saveFile();
                    break;

                case 3:
                    deleteItemByUser();
                    saveFile();
                    break;

                case 4:
                    itemsService.showAllTodoItems(currentUser.getItems());
                    break;

                case 5:
                    currentUser.showTop5ItemsByDate();
                    break;

                case 6:
                    search();
                    break;

                case 7:
                    addItemToCategoryFromUser();
                    saveFile();
                    break;

                case 8:
                    addItemToFavoriteFromUser();
                    saveFile();
                    break;

                case 9:
                    itemsService.printFavorites(currentUser.getItems());
                    break;

                case 10:
                    clearScreen();
                    break;
                case 11:
                    updateName();
                    break;
                case 12:
                    currentUser = null;
                    break;
                case 13:
                    saveFile();
                    System.exit(0);
                    break;
            }
        }

    }

    private TodoItem takeCreateItemFromUser() {
        utils.print("Enter new data...");

        utils.print("Enter title:");
        String title = validateGetTitle("");//data.nextLine();
        if (title.equalsIgnoreCase("/back")) return null;

        utils.print("Enter description:");
        String description = utils.getInput("enter a valid description");//data.nextLine();
        if (description.equalsIgnoreCase("/back")) return null;

        utils.print(text.choosePriority);
        int userPriorityChoice = utils.getInput(
                "invalid choice.\n" + text.choosePriority, 1, 3
        );
        if (userPriorityChoice == -1) return null;
        Priority priority = (userPriorityChoice == 1) ? Priority.Low :
                ((userPriorityChoice == 2) ? Priority.Medium : Priority.High);

        utils.print(text.chooseCategory);
        int userCategoryChoice = utils.getInput("invalid input.\n" +
                text.chooseCategory, 1, 6);
        if (userCategoryChoice == -1) return null;
        Category category = text.categories.get(userCategoryChoice - 1);

        String startDateString;
        do {
            utils.print(text.enterStartDate);
            startDateString = scanner.nextLine();
            if (startDateString.equalsIgnoreCase("/back")) return null;
        } while (!dateUtils.isValidDate(startDateString));
        Date startDate = dateUtils.convertStringToDate(startDateString);

        String endDateString;
        do {
            utils.print(text.enterEndDate);
            endDateString = scanner.nextLine();
            if (startDateString.equalsIgnoreCase("/back")) return null;
        } while (!dateUtils.isValidEndDate(startDate, endDateString));
        Date endDate = dateUtils.convertStringToDate(endDateString);

        return new TodoItem(title, description, priority, category, startDate, endDate);
    }

    private int updateIsConfirmed(String itemToBeUpdated) {
        System.out.println("choose 1 if you want to update the " + itemToBeUpdated + " and 2 if you don't want to update it");
        String userInput = scanner.next();
        while (!userInput.equals("1") && !userInput.equals("2") && !userInput.equalsIgnoreCase("/back")) {
            System.out.println("invalid choice");
            userInput = scanner.next();
        }
        switch (userInput) {
            case "1":
                return 1;
            case "2":
                return 2;
            case "/back":
                return -1;
            default:
                return 0;
        }
    }

    private String validateGetTitle(String oldTitle) {// used to make sure that user input(string) is not empty or not only just ' ' character
        String title = scanner.nextLine();
        if (title.equalsIgnoreCase("/back")) return title;
        boolean titleAlreadyExists = (itemsService.getItemByTitle(title.trim(), currentUser.getItems()) != -1 && !oldTitle.equalsIgnoreCase(title.trim()));

        while (title.matches(" +") || title.isEmpty() || titleAlreadyExists) {// used to make sure that user input(string) is not empty or not only just ' ' character and title doesn't exist
            if (titleAlreadyExists)
                utils.print("title already exists re-enter title");
            else if (title.matches(" +") || title.isEmpty())
                utils.print("invalid title");
            title = scanner.nextLine();
            titleAlreadyExists = (itemsService.getItemByTitle(title.trim(), currentUser.getItems()) != -1 && !oldTitle.equalsIgnoreCase(title.trim()));
        }
        return title;
    }

    private String getExistingTitle(String messageSpecifier) {
        String title = "";
        while (true) {
            System.out.println("Enter title of item to be added to " + messageSpecifier);
            title = utils.getInput("invalid title");
            if (title.equalsIgnoreCase("/back")) return title;
            if (currentUser.itemExists(title)) break;
            System.err.println("Item doesn't exist");
        }
        return title;
    }

    private String getOldTitleFromUser() {
        while (true) {
            utils.print("Enter title of item to be updated:");
            String oldTitle = scanner.nextLine();
            if (oldTitle.equalsIgnoreCase("/back")) return oldTitle;
            if (currentUser.itemExists(oldTitle)) {
                return oldTitle;
            }
            if (oldTitle.matches(" +") || oldTitle.isEmpty()) {
                System.err.println("Please enter a valid title");
            } else {
                System.err.println("Title entered doesn't exist");
            }
        }
    }

    private void takeUpdateItemFromUser() {
        String oldTitle = getOldTitleFromUser();
        if (oldTitle.equalsIgnoreCase("/back")) return;
        int itemIndex = itemsService.getItemByTitle(oldTitle, currentUser.getItems());
        TodoItem item = currentUser.getItems().get(itemIndex).clone();

        System.out.println("Enter new data...");
        int confirmUpdate;

        confirmUpdate = updateIsConfirmed("title");
        if (confirmUpdate == 1) {
            utils.print("Enter title:");
            String title = validateGetTitle(oldTitle);//data.nextLine();
            item.setTitle(title);
        } else if (confirmUpdate == -1) return;

        confirmUpdate = updateIsConfirmed("description");
        if (confirmUpdate == 1) {
            utils.print("Enter description:");
            String description = utils.getInput("enter a valid description");//data.nextLine();
            item.setDescription(description);
        } else if (confirmUpdate == -1) return;

        confirmUpdate = updateIsConfirmed("priority");
        if (confirmUpdate == 1) {
            utils.print(text.choosePriority);
            int userPriorityChoice = utils.getInput(
                    "invalid choice.\n" + text.choosePriority, 1, 3
            );
            Priority priority = (userPriorityChoice == 1) ? Priority.Low :
                    ((userPriorityChoice == 2) ? Priority.Medium : Priority.High);
            item.setPriority(priority);
        } else if (confirmUpdate == -1) return;

        confirmUpdate = updateIsConfirmed("category");
        if (confirmUpdate == 1) {
            utils.print(text.chooseCategory);
            int userCategoryChoice = utils.getInput("invalid input.\n" +
                    text.chooseCategory, 1, 6);
            Category category = text.categories.get(userCategoryChoice - 1);
            item.setCategory(category);
        } else if (confirmUpdate == -1) return;

        boolean startDatePassedEndDate = false;
        confirmUpdate = updateIsConfirmed("start date");
        if (confirmUpdate == 1) {
            utils.print(text.enterStartDate);
            String startDateString = scanner.nextLine();
            while (!dateUtils.isValidDate(startDateString)) {
                utils.print(text.enterStartDate);
                startDateString = scanner.nextLine();
            }
            Date startDate = dateUtils.convertStringToDate(startDateString);
            //item.setStartDate(startDate);

            startDatePassedEndDate = startDate.compareTo(item.getEndDate()) == 1;
            if (startDatePassedEndDate) {
                System.out.println("The start date entered passes the end date," +
                        " are you sure you want to change it?  (1-Yes , 2-No)");
                int choice = utils.getInput("Enter a valid choice", 1, 2);
                if (choice == 1) {
                    item.setStartDate(startDate);
                } else {
                    startDatePassedEndDate = false;
                }
            }
        } else if (confirmUpdate == -1) return;

        confirmUpdate = updateIsConfirmed("end date");
        if (startDatePassedEndDate || confirmUpdate == 1) {
            utils.print(text.enterEndDate);
            String endDateString = scanner.nextLine();
            while (!dateUtils.isValidEndDate(item.getStartDate(), endDateString)) {
                utils.print(text.enterEndDate);
                endDateString = scanner.nextLine();
            }
            Date endDate = dateUtils.convertStringToDate(endDateString);
            item.setEndDate(endDate);


        } else if (confirmUpdate == -1) return;

        boolean updated = itemsService.updateTodoItem(currentUser.getName(), item, oldTitle, currentUser.getItems());
        if (updated) {
            currentUser.getItems().get(itemIndex).updateNewItem(item);
            System.out.println("Item updated:\n" + item.toString());
        }
    }

    private void deleteItemByUser() {
        if (currentUser.getItems().isEmpty())
            utils.PrintColoredMessage(font.ANSI_RED, "No items available");
        else {
            utils.print("Enter title of item to be deleted:");
            String title = utils.getInput("invalid title");
            if (title.equalsIgnoreCase("/back")) return;
            itemsService.deleteTodoItem(title, currentUser.getItems());
            //  currentUser.deleteTodoItem(title);

        }
    }

    private void search() {
        boolean isSearchKeyValid = false;
        while (!isSearchKeyValid) {
            utils.print(text.chooseSearchFilter);
            String searchOption = scanner.nextLine();

            switch (searchOption) {
                case "1":
                    utils.print("Enter title of an item: ");
                    String searchTitle = utils.getInput("invalid title");
                    if (searchTitle.equalsIgnoreCase("/back")) return;
                    itemsService.searchShowItemsBySearchKey(SearchKey.Title, searchTitle, currentUser.getItems());
                    isSearchKeyValid = true;
                    break;

                case "2":
                    String searchStartDate;
                    do {
                        utils.print(text.enterStartDate);
                        searchStartDate = scanner.next();
                        if (searchStartDate.equalsIgnoreCase("/back")) return;
                    } while (!dateUtils.isValidDate(searchStartDate));
                    itemsService.searchShowItemsBySearchKey(SearchKey.StartDate, searchStartDate, currentUser.getItems());
                    isSearchKeyValid = true;
                    break;

                case "3":
                    String searchEndDate;
                    do {
                        utils.print(text.enterEndDate);
                        searchEndDate = scanner.next();
                        if (searchEndDate.equalsIgnoreCase("/back")) return;
                    } while (!dateUtils.isValidDate(searchEndDate));
                    itemsService.searchShowItemsBySearchKey(SearchKey.EndDate, searchEndDate, currentUser.getItems());
                    isSearchKeyValid = true;
                    break;

                case "4":
                    utils.print(text.choosePriority);
                    int searchPriority = utils.getInput("Invalid option, try again."
                            + font.ANSI_RESET + "\n" + text.choosePriority, 1, 3);
                    if (searchPriority == -1) return;
                    String priorityValue = (searchPriority == 1) ? "Low" : ((searchPriority == 2) ? "Medium" : "High");
                    itemsService.searchShowItemsBySearchKey(SearchKey.Priority, priorityValue, currentUser.getItems());
                    isSearchKeyValid = true;
                    break;
                case "/back":
                    return;

                default:
                    System.err.println("Invalid input.");
                    break;
            }
        }
    }

    private void addItemToCategoryFromUser() {
        String title = getExistingTitle("Category");
        utils.print(text.chooseCategory);
        int userCategoryChoice = utils.getInput("invalid input.\n" +
                text.chooseCategory, 1, 6);
        Category category = text.categories.get(userCategoryChoice-1);
        //currentUser.addItemToCategory(title,category);
        itemsService.addItemToCategory(currentUser.getName(),title,category,currentUser.getItems());
    }

    private void addItemToFavoriteFromUser() {
        String title = getExistingTitle("Favorites");
        if(title.equalsIgnoreCase("/back")) return;
        //currentUser.addItemToFavorite(title);
        itemsService.addItemToFavorite(currentUser.getName(),title,currentUser.getItems());
    }

    private void updateName() {
        System.out.println("Please type in your new name");

        String name = "";
        boolean uniqueNameEntered = false;
        while (!uniqueNameEntered) {
            name = utils.getInput("Please enter a valid name");
            if (name.equalsIgnoreCase("/back"))
                return;
            else if (getUserByUsername(name) == null)
                uniqueNameEntered = true;
            else {
                System.err.println("The name entered already exists, please try again");
            }
        }
        userService.updateUsersName(currentUser.getName(),name);
        //currentUser.setName(name);

    }

    private void clearScreen() {
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    private void saveFile() {
        storage.saveData(users);
    }

}
