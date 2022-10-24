import console.ConsoleOptions;
import classes.HelperMethods;
import classes.TodoItem;
import classes.User;
import console.OutputMessages;
import enums.Category;
import enums.Priority;
import enums.SearchKey;
import storage.FileStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Main {
    static User currentUser;
    static FileStorage fileStorage = new FileStorage();

    public static void main(String[] args) {
        if(!doesUserExist())
            getUserName();
        showMenu();
    }

    public static boolean doesUserExist() {
        currentUser = fileStorage.loadData();
        if(currentUser != null) {
            return true;
        }
        return false;
    }

    public static void getUserName() {
        Scanner input = new Scanner(System.in);

        System.out.println("Hello, what is your name?");

        String name = HelperMethods.validateGetStringInput("write a valid name");
        currentUser = new User(name);
    }

    public static void showMenu(){
        Scanner input = new Scanner(System.in);
        while(true)
        {
            System.out.println(ConsoleOptions.ANSI_YELLOW + "\nWelcome " + currentUser.getName() + ConsoleOptions.ANSI_RESET);
            for(String option : OutputMessages.menuOptions)
                System.out.println(option);

            int option = HelperMethods.validateGetIntegerInput("Invalid input", 1, 10);
            switch (option){
                case 1:
                    TodoItem item = takeCreateItemFromUser();
                    currentUser.addTodoItem(item);
                    currentUser.showAllTodoItems();
                    saveFile();
                    break;

                case 2:
                   // updateItemFromUser();
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
        System.out.println("Enter new data...");
        Scanner data = new Scanner(System.in);
        System.out.println("Enter title:");
        String title = validateGetTitle("");//data.nextLine();

        System.out.println("Enter description:");
        String description = HelperMethods.validateGetStringInput("enter a valid description");//data.nextLine();

        System.out.println("Choose priority for the item (1.Low, 2.Medium, 3.High):");
        int userPriorityChoice = HelperMethods.validateGetIntegerInput(
                "invalid choice.\nChoose priority for the item (1.Low, 2.Medium, 3.High):", 1, 3
        );
        Priority priority = (userPriorityChoice == 1)? Priority.Low :
                ((userPriorityChoice == 2)?Priority.Medium : Priority.High);

        System.out.println("Choose category for the item " +
                "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)");
        int userCategoryChoice = HelperMethods.validateGetIntegerInput("invalid input.\nChoose category for the item " +
                "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)", 1, 6);
        Category category = OutputMessages.categories.get(userCategoryChoice-1);

        System.out.println("Enter start date of the item (e.g. dd-MM-yyyy)");
        String startDateString = data.nextLine();
        while(!HelperMethods.isValidDate(startDateString)){
            System.out.println("Enter start date of the item (e.g. dd-mm-yyyy)");
            startDateString = data.nextLine();
        }
        Date startDate = HelperMethods.convertStringToDate(startDateString);

        System.out.println("Enter end date of the item (e.g. dd-MM-yyyy)");
        String endDateString = data.nextLine();
        while(!HelperMethods.isValidEndDate(startDate, endDateString)){
            System.out.println("Enter end date of the item (e.g. dd-mm-yyyy)");
            endDateString = data.nextLine();
        }
        Date endDate = HelperMethods.convertStringToDate(endDateString);

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

    public static String validateGetTitle(String oldTitle){// used to make sure that user input(string) is not empty or not only just ' ' character
        Scanner data = new Scanner(System.in);
        String title = data.nextLine();
        boolean titleAlreadyExists=(currentUser.getItemByTitle(title.trim())!=-1 && !oldTitle.equalsIgnoreCase(title.trim()));
        while(title .matches(" +")|| title .isEmpty() || titleAlreadyExists){// used to make sure that user input(string) is not empty or not only just ' ' character and title doesn't exist
            if(titleAlreadyExists)
                System.out.println(" title already exists re-enter title");
            else if(title .matches(" +")|| title .isEmpty())
                System.out.println("invalid title");
            title=data.nextLine();
            titleAlreadyExists=(currentUser.getItemByTitle(title.trim())!=-1 && !oldTitle.equalsIgnoreCase(title.trim()));
        }
        return title;
    }

    private static String getExistingTitle(String messageSpecifier){
        String title = "";
        while(true) {
            System.out.println("Enter title of item to be added to "+messageSpecifier);
            title = HelperMethods.validateGetStringInput("invalid title");
            if(currentUser.itemExists(title)) break;
            System.out.println("Item doesn't exist");
        }
        return title;
    }

    private static String getOldTitleFromUser(){
        Scanner data = new Scanner(System.in);
        while(true){
            System.out.println("Enter title of item to be updated:");
            String oldTitle = data.nextLine();
            if(currentUser.itemExists(oldTitle)){
                return oldTitle;
            }
            if(oldTitle .matches(" +")|| oldTitle .isEmpty()){
                System.out.println("Please enter a valid title");
            }
            else {
                System.out.println("Title entered doesn't exist");
            }
        }
    }

    private static void takeUpdateItemFromUser() {
        System.out.println("Enter new data...");
        Scanner data = new Scanner(System.in);
        String oldTile = getOldTitleFromUser();
        int itemIndex = currentUser.getItemByTitle(oldTile);
        TodoItem item = currentUser.getItems().get(itemIndex);

        System.out.println("Enter new data...");


        if (updateIsConfirmed("title")) {
            System.out.println("Enter title:");
            String title = validateGetTitle(oldTile);//data.nextLine();
            item.setTitle(title);
        }
        if (updateIsConfirmed("description")) {
            System.out.println("Enter description:");
            String description = HelperMethods.validateGetStringInput("enter a valid description");//data.nextLine();
            item.setDescription(description);
        }

        if (updateIsConfirmed("priority")) {
            System.out.println("Choose priority for the item (1.Low, 2.Medium, 3.High):");
            int userPriorityChoice = HelperMethods.validateGetIntegerInput(
                    "invalid choice.\nChoose priority for the item (1.Low, 2.Medium, 3.High):", 1, 3
            );
            Priority priority = (userPriorityChoice == 1) ? Priority.Low :
                    ((userPriorityChoice == 2) ? Priority.Medium : Priority.High);
            item.setPriority(priority);
        }

        if (updateIsConfirmed("category")) {
            System.out.println("Choose category for the item " +
                    "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)");
            int userCategoryChoice = HelperMethods.validateGetIntegerInput("invalid input.\nChoose category for the item " +
                    "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)", 1, 6);
            Category category = OutputMessages.categories.get(userCategoryChoice - 1);
            item.setCategory(category);
        }
        boolean startDatePassedEndDate = false;
        if (updateIsConfirmed("start date")) {
            System.out.println("Enter start date of the item (e.g. dd-MM-yyyy)");
            String startDateString = data.nextLine();
            while (!HelperMethods.isValidDate(startDateString)) {
                System.out.println("Enter start date of the item (e.g. dd-mm-yyyy)");
                startDateString = data.nextLine();
            }
            Date startDate = HelperMethods.convertStringToDate(startDateString);
            //item.setStartDate(startDate);

            startDatePassedEndDate = startDate.compareTo(item.getEndDate())==1;
            if(startDatePassedEndDate) {
                System.out.println("The start date entered passes the end date," +
                        " are you sure you want to change it?  (1-Yes , 2-No)");
                int choice = HelperMethods.validateGetIntegerInput("Enter a valid choice",1,2);
                if(choice==1){
                    item.setStartDate(startDate);
                }
                else{
                    startDatePassedEndDate = false;
                }
            }
        }
        if (startDatePassedEndDate || updateIsConfirmed("end date")) {
            System.out.println("Enter end date of the item (e.g. dd-MM-yyyy)");
            String endDateString = data.nextLine();
            while (!HelperMethods.isValidEndDate(item.getStartDate(), endDateString)) {
                System.out.println("Enter end date of the item (e.g. dd-mm-yyyy)");
                endDateString = data.nextLine();
            }
            Date endDate = HelperMethods.convertStringToDate(endDateString);
            item.setEndDate(endDate);


        }
        System.out.println("Item updated:\n" + item.toString());

    }

    private static void deleteItemByUser(){
        System.out.println("Enter title of item to be deleted:");
        String title = HelperMethods.validateGetStringInput("invalid title");
        currentUser.deleteTodoItem(title);
    }

    private static void search(){
        Scanner input = new Scanner(System.in);
        boolean isSearchKeyValid = false;
        while (!isSearchKeyValid){
            System.out.println("choose filter for search (1.title, 2.start date, 3.end date, 4.priority)");
            String searchOption = input.nextLine();
            switch (searchOption){
                case "1":
                    System.out.print("Enter title of an item: ");
                    String searchTitle = HelperMethods.validateGetStringInput("invalid title");
                    currentUser.searchShowItemsBySearchKey(SearchKey.Title, searchTitle);
                    isSearchKeyValid = true;
                    break;

                case "2":
                    System.out.print("Enter start date of an item: ");
                    String searchStartDate = input.next();
                    while(!HelperMethods.isValidDate(searchStartDate)){
                        System.out.print("Enter start date of an item: ");
                        searchStartDate = input.next();
                    }
                    currentUser.searchShowItemsBySearchKey(SearchKey.StartDate, searchStartDate);
                    isSearchKeyValid = true;
                    break;

                case "3":
                    System.out.print("Enter end date of an item: ");
                    String searchEndDate = input.next();
                    while(!HelperMethods.isValidDate(searchEndDate)){
                        System.out.print("Enter end date of an item: ");
                        searchEndDate = input.next();
                    }
                    currentUser.searchShowItemsBySearchKey(SearchKey.EndDate, searchEndDate);
                    isSearchKeyValid = true;
                    break;

                case "4":
                    System.out.print("Choose priority of an item (1.Low, 2.Medium, 3.High): ");
                    int searchPriority = HelperMethods.validateGetIntegerInput("Invalid option, try again."
                            +ConsoleOptions.ANSI_RESET + "\nChoose priority of an item (1.Low, 2.Medium, 3.High): ", 1, 3);
                    String priorityValue = (searchPriority == 1) ? "Low" : ((searchPriority == 2) ? "Medium" : "High");
                    currentUser.searchShowItemsBySearchKey(SearchKey.Priority, priorityValue);
                    isSearchKeyValid = true;
                    break;

                default:
                    System.out.println("Invalid input.");
                    break;
            }
        }
    }

    private static void addItemToCategoryFromUser(){
        String title = getExistingTitle("Category");

        System.out.println("Choose category for the item " +
                "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)");
        int userCategoryChoice = HelperMethods.validateGetIntegerInput("invalid input.\nChoose category for the item " +
                "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)", 1, 6);
        Category category = OutputMessages.categories.get(userCategoryChoice-1);

        currentUser.addItemToCategory(title,category);
    }

    private static void saveFile() {
        fileStorage.saveData(currentUser);
    }
}
