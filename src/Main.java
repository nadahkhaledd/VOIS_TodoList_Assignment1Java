import ui.Font;
import utility.Utils;
import models.TodoItem;
import models.User;
import ui.Text;
import enums.Category;
import enums.Priority;
import enums.SearchKey;
import storage.FileStorage;

import java.util.Date;
import java.util.Scanner;

public class Main {
    static User currentUser;
    static FileStorage fileStorage = new FileStorage();
    static Utils utils = new Utils();
    static Font font = new Font();
    static Text text = new Text();

    public static void main(String[] args) {
        if(!isThereUser())
            getUserName();
        showMenu();
    }

    private static boolean isThereUser() {
        currentUser = fileStorage.loadData();
        if(currentUser != null) {
            return true;
        }
        return false;
    }

    private static void getUserName() {
        Scanner input = new Scanner(System.in);

        utils.print("Hello, what is your name?");

        String name = utils.getInput("write a valid name");
        currentUser = new User(name);
    }

    private static void showMenu(){
        Scanner input = new Scanner(System.in);
        while(true)
        {
            utils.PrintColoredMessage(font.ANSI_YELLOW, "\nWelcome " + currentUser.getName());
            for(String option : text.menuOptions)
                System.out.println(option);

            int option = utils.getInput("Invalid input", 1, 10);
            switch (option){
                case 1:
                    TodoItem item = takeCreateItemFromUser();
                    currentUser.addTodoItem(item);
                    currentUser.showAllTodoItems();
                    saveFile();
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
                    currentUser.showAllTodoItems();
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
                    String title = getExistingTitle("Favorites");
                    currentUser.addItemToFavorite(title);
                    saveFile();
                    break;

                case 9:
                    currentUser.printFavorites();
                    break;

                case 10:
                default:
                    saveFile();
                    System.exit(0);
                    break;
            }
        }

    }

    private static TodoItem takeCreateItemFromUser(){
        utils.print("Enter new data...");
        Scanner data = new Scanner(System.in);
        utils.print("Enter title:");
        String title = validateGetTitle("");//data.nextLine();

        utils.print("Enter description:");
        String description = utils.getInput("enter a valid description");//data.nextLine();

        utils.print(text.choosePriority);
        int userPriorityChoice = utils.getInput(
                "invalid choice.\n" + text.choosePriority, 1, 3
        );
        Priority priority = (userPriorityChoice == 1)? Priority.Low :
                ((userPriorityChoice == 2)?Priority.Medium : Priority.High);

        utils.print(text.chooseCategory);
        int userCategoryChoice = utils.getInput("invalid input.\n" +
                text.chooseCategory, 1, 6);
        Category category = text.categories.get(userCategoryChoice-1);

        utils.print(text.enterStartDate);
        String startDateString = data.nextLine();
        while(!utils.isValidDate(startDateString)){
            utils.print(text.enterStartDate);
            startDateString = data.nextLine();
        }
        Date startDate = utils.convertStringToDate(startDateString);

        utils.print(text.enterEndDate);
        String endDateString = data.nextLine();
        while(!utils.isValidEndDate(startDate, endDateString)){
            utils.print(text.enterEndDate);
            endDateString = data.nextLine();
        }
        Date endDate = utils.convertStringToDate(endDateString);

        return new TodoItem(title, description, priority, category, startDate, endDate);
    }

    private static boolean updateIsConfirmed(String itemToBeUpdated){
        Scanner data = new Scanner(System.in);
        System.out.println("choose 1 if you want to update the "+itemToBeUpdated+" and 2 if you don't want to update it");
        String userInput=data.next();
        while(!userInput.equals("1") && !userInput.equals("2") ){
            System.out.println("invalid choice");
            userInput=data.next();
        }
        switch(userInput){
            case "1":return true;
            default: return false;
        }

    }

    private static String validateGetTitle(String oldTitle){// used to make sure that user input(string) is not empty or not only just ' ' character
        Scanner data = new Scanner(System.in);
        String title = data.nextLine();
        boolean titleAlreadyExists=(currentUser.getItemByTitle(title.trim())!=-1 && !oldTitle.equalsIgnoreCase(title.trim()));
        while(title .matches(" +")|| title .isEmpty() || titleAlreadyExists){// used to make sure that user input(string) is not empty or not only just ' ' character and title doesn't exist
            if(titleAlreadyExists)
                utils.print("title already exists re-enter title");
            else if(title .matches(" +")|| title .isEmpty())
                utils.print("invalid title");
            title=data.nextLine();
            titleAlreadyExists=(currentUser.getItemByTitle(title.trim())!=-1 && !oldTitle.equalsIgnoreCase(title.trim()));
        }
        return title;
    }

    private static String getExistingTitle(String messageSpecifier){
        String title = "";
        while(true) {
            System.out.println("Enter title of item to be added to "+messageSpecifier);
            title = utils.getInput("invalid title");
            if(currentUser.itemExists(title)) break;
            System.err.println("Item doesn't exist");
        }
        return title;
    }

    private static String getOldTitleFromUser(){
        Scanner data = new Scanner(System.in);
        while(true){
            utils.print("Enter title of item to be updated:");
            String oldTitle = data.nextLine();
            if(currentUser.itemExists(oldTitle)){
                return oldTitle;
            }
            if(oldTitle .matches(" +")|| oldTitle .isEmpty()){
                System.err.println("Please enter a valid title");
            }
            else {
                System.err.println("Title entered doesn't exist");
            }
        }
    }

    private static void takeUpdateItemFromUser() {
        Scanner data = new Scanner(System.in);
        String oldTile = getOldTitleFromUser();
        int itemIndex = currentUser.getItemByTitle(oldTile);
        TodoItem item = currentUser.getItems().get(itemIndex);

        System.out.println("Enter new data...");
        if (updateIsConfirmed("title")) {
            utils.print("Enter title:");
            String title = validateGetTitle(oldTile);//data.nextLine();
            item.setTitle(title);
        }
        if (updateIsConfirmed("description")) {
            utils.print("Enter description:");
            String description = utils.getInput("enter a valid description");//data.nextLine();
            item.setDescription(description);
        }

        if (updateIsConfirmed("priority")) {
            utils.print(text.choosePriority);
            int userPriorityChoice = utils.getInput(
                    "invalid choice.\n"+ text.choosePriority, 1, 3
            );
            Priority priority = (userPriorityChoice == 1) ? Priority.Low :
                    ((userPriorityChoice == 2) ? Priority.Medium : Priority.High);
            item.setPriority(priority);
        }

        if (updateIsConfirmed("category")) {
            utils.print(text.chooseCategory);
            int userCategoryChoice = utils.getInput("invalid input.\n" +
                    text.chooseCategory, 1, 6);
            Category category = text.categories.get(userCategoryChoice - 1);
            item.setCategory(category);
        }
        boolean startDatePassedEndDate = false;
        if (updateIsConfirmed("start date")) {
            utils.print(text.enterStartDate);
            String startDateString = data.nextLine();
            while (!utils.isValidDate(startDateString)) {
                utils.print(text.enterStartDate);
                startDateString = data.nextLine();
            }
            Date startDate = utils.convertStringToDate(startDateString);
            //item.setStartDate(startDate);

            startDatePassedEndDate = startDate.compareTo(item.getEndDate())==1;
            if(startDatePassedEndDate) {
                System.out.println("The start date entered passes the end date," +
                        " are you sure you want to change it?  (1-Yes , 2-No)");
                int choice = utils.getInput("Enter a valid choice",1,2);
                if(choice==1){
                    item.setStartDate(startDate);
                }
                else{
                    startDatePassedEndDate = false;
                }
            }
        }
        if (startDatePassedEndDate || updateIsConfirmed("end date")) {
            utils.print(text.enterEndDate);
            String endDateString = data.nextLine();
            while (!utils.isValidEndDate(item.getStartDate(), endDateString)) {
                utils.print(text.enterEndDate);
                endDateString = data.nextLine();
            }
            Date endDate = utils.convertStringToDate(endDateString);
            item.setEndDate(endDate);


        }
        System.out.println("Item updated:\n" + item.toString());

    }

    private static void deleteItemByUser(){
        utils.print("Enter title of item to be deleted:");
        String title = utils.getInput("invalid title");
        currentUser.deleteTodoItem(title);
    }

    private static void search(){
        Scanner input = new Scanner(System.in);
        boolean isSearchKeyValid = false;
        while (!isSearchKeyValid){
            utils.print(text.chooseSearchFilter);
            String searchOption = input.nextLine();
            switch (searchOption){
                case "1":
                    utils.print("Enter title of an item: ");
                    String searchTitle = utils.getInput("invalid title");
                    currentUser.searchShowItemsBySearchKey(SearchKey.Title, searchTitle);
                    isSearchKeyValid = true;
                    break;

                case "2":
                    utils.print(text.enterStartDate);
                    String searchStartDate = input.next();
                    while(!utils.isValidDate(searchStartDate)){
                        utils.print(text.enterStartDate);
                        searchStartDate = input.next();
                    }
                    currentUser.searchShowItemsBySearchKey(SearchKey.StartDate, searchStartDate);
                    isSearchKeyValid = true;
                    break;

                case "3":
                    utils.print(text.enterEndDate);
                    String searchEndDate = input.next();
                    while(!utils.isValidDate(searchEndDate)){
                        utils.print(text.enterEndDate);
                        searchEndDate = input.next();
                    }
                    currentUser.searchShowItemsBySearchKey(SearchKey.EndDate, searchEndDate);
                    isSearchKeyValid = true;
                    break;

                case "4":
                    utils.print(text.choosePriority);
                    int searchPriority = utils.getInput("Invalid option, try again."
                            + font.ANSI_RESET + "\n" + text.choosePriority, 1, 3);
                    String priorityValue = (searchPriority == 1) ? "Low" : ((searchPriority == 2) ? "Medium" : "High");
                    currentUser.searchShowItemsBySearchKey(SearchKey.Priority, priorityValue);
                    isSearchKeyValid = true;
                    break;

                default:
                    System.err.println("Invalid input.");
                    break;
            }
        }
    }

    private static void addItemToCategoryFromUser(){
        String title = getExistingTitle("Category");

        utils.print(text.chooseCategory);
        int userCategoryChoice = utils.getInput("invalid input.\n" +
                text.chooseCategory, 1, 6);
        Category category = text.categories.get(userCategoryChoice-1);

        currentUser.addItemToCategory(title,category);
    }

    private static void saveFile() {
        fileStorage.saveData(currentUser);
    }
}
