package app;

import enums.Category;
import enums.Priority;
import enums.SearchKey;
import models.TodoItem;
import models.User;
import storage.FileStorage;
import ui.Font;
import ui.Text;
import utility.DateUtils;
import utility.Utils;

import java.util.Date;
import java.util.Scanner;

public class TodoList {
    private Scanner scanner = new Scanner(System.in);
    private User currentUser;
    private FileStorage fileStorage = new FileStorage();
    private Utils utils = new Utils();
    private DateUtils dateUtils = new DateUtils();
    private Font font = new Font();
    private Text text = new Text();

    public void start() {
        if(!isThereUser())
            setUserName();
        showMenu();
    }

    private boolean isThereUser() {
        currentUser = fileStorage.loadData();
        if(currentUser != null) {
            return true;
        }
        return false;
    }
    private void setUserName() {
        utils.print("Hello, what is your name?");
        String name = utils.getInput("write a valid name");
        currentUser = new User(name);
    }

    private void showMenu(){
        while(true)
        {
            utils.PrintColoredMessage(font.ANSI_YELLOW, "\nWelcome " + currentUser.getName());
            for(String option : text.menuOptions)
                System.out.println(option);

            int option = utils.getInput("Invalid input", 1, 10);
            switch (option){
                case 1:
                    TodoItem item = takeCreateItemFromUser();
                    if(item != null) {
                        currentUser.addTodoItem(item);
                        currentUser.showAllTodoItems();
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
                    addItemToFavoriteFromUser();
                    saveFile();
                    break;

                case 9:
                    currentUser.printFavorites();
                    break;
                case 10:
                    saveFile();
                    System.exit(0);
                    break;
            }
        }

    }

    private TodoItem takeCreateItemFromUser(){
        utils.print("Enter new data...");

        utils.print("Enter title:");
        String title = validateGetTitle("");//data.nextLine();
        if(title.equalsIgnoreCase("/back")) return null;

        utils.print("Enter description:");
        String description = utils.getInput("enter a valid description");//data.nextLine();
        if(description.equalsIgnoreCase("/back")) return null;

        utils.print(text.choosePriority);
        int userPriorityChoice = utils.getInput(
                "invalid choice.\n" + text.choosePriority, 1, 3
        );
        if(userPriorityChoice == -1) return null;
        Priority priority = (userPriorityChoice == 1)? Priority.Low :
                ((userPriorityChoice == 2)?Priority.Medium : Priority.High);

        utils.print(text.chooseCategory);
        int userCategoryChoice = utils.getInput("invalid input.\n" +
                text.chooseCategory, 1, 6);
        if(userCategoryChoice == -1) return null;
        Category category = text.categories.get(userCategoryChoice-1);

        String startDateString;
        do {
            utils.print(text.enterStartDate);
            startDateString = scanner.nextLine();
            if(startDateString.equalsIgnoreCase("/back")) return null;
        } while(!dateUtils.isValidDate(startDateString));
        Date startDate = dateUtils.convertStringToDate(startDateString);

        String endDateString;
        do {
            utils.print(text.enterEndDate);
            endDateString = scanner.nextLine();
            if(startDateString.equalsIgnoreCase("/back")) return null;
        } while(!dateUtils.isValidEndDate(startDate, endDateString));
        Date endDate = dateUtils.convertStringToDate(endDateString);

        return new TodoItem(title, description, priority, category, startDate, endDate);
    }

    private boolean updateIsConfirmed(String itemToBeUpdated){
        System.out.println("choose 1 if you want to update the "+itemToBeUpdated+" and 2 if you don't want to update it");
        String userInput=scanner.next();
        while(!userInput.equals("1") && !userInput.equals("2") ){
            System.out.println("invalid choice");
            userInput=scanner.next();
        }
        switch(userInput){
            case "1":return true;
            default: return false;
        }

    }

    private String validateGetTitle(String oldTitle){// used to make sure that user input(string) is not empty or not only just ' ' character
        String title = scanner.nextLine();
        if(title.equalsIgnoreCase("/back")) return title;
        boolean titleAlreadyExists=(currentUser.getItemByTitle(title.trim())!=-1 && !oldTitle.equalsIgnoreCase(title.trim()));

        while(title .matches(" +")|| title .isEmpty() || titleAlreadyExists){// used to make sure that user input(string) is not empty or not only just ' ' character and title doesn't exist
            if(titleAlreadyExists)
                utils.print("title already exists re-enter title");
            else if(title .matches(" +")|| title .isEmpty())
                utils.print("invalid title");
            title=scanner.nextLine();
            titleAlreadyExists=(currentUser.getItemByTitle(title.trim())!=-1 && !oldTitle.equalsIgnoreCase(title.trim()));
        }
        return title;
    }

    private String getExistingTitle(String messageSpecifier){
        String title = "";
        while(true) {
            System.out.println("Enter title of item to be added to "+messageSpecifier);
            title = utils.getInput("invalid title");
            if(title.equalsIgnoreCase("/back")) return title;
            if(currentUser.itemExists(title)) break;
            System.err.println("Item doesn't exist");
        }
        return title;
    }

    private String getOldTitleFromUser(){
        while(true){
            utils.print("Enter title of item to be updated:");
            String oldTitle = scanner.nextLine();
            if(oldTitle.equalsIgnoreCase("/back")) return oldTitle;
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

    private void takeUpdateItemFromUser() {
        String oldTile = getOldTitleFromUser();
        if(oldTile.equalsIgnoreCase("/back")) return;
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
            String startDateString = scanner.nextLine();
            while (!dateUtils.isValidDate(startDateString)) {
                utils.print(text.enterStartDate);
                startDateString = scanner.nextLine();
            }
            Date startDate = dateUtils.convertStringToDate(startDateString);
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
            String endDateString = scanner.nextLine();
            while (!dateUtils.isValidEndDate(item.getStartDate(), endDateString)) {
                utils.print(text.enterEndDate);
                endDateString = scanner.nextLine();
            }
            Date endDate = dateUtils.convertStringToDate(endDateString);
            item.setEndDate(endDate);


        }
        System.out.println("Item updated:\n" + item.toString());
    }

    private void deleteItemByUser(){
        if(currentUser.getItems().isEmpty())
            utils.PrintColoredMessage(font.ANSI_RED, "No items available");
        else {
            utils.print("Enter title of item to be deleted:");
            String title = utils.getInput("invalid title");
            if(title.equalsIgnoreCase("/back")) return;
            currentUser.deleteTodoItem(title);
        }
    }

    private void search(){
        boolean isSearchKeyValid = false;
        while (!isSearchKeyValid){
            utils.print(text.chooseSearchFilter);
            String searchOption = scanner.nextLine();
            if(searchOption.equalsIgnoreCase("/back")) return;

            switch (searchOption){
                case "1":
                    utils.print("Enter title of an item: ");
                    String searchTitle = utils.getInput("invalid title");
                    if(searchTitle.equalsIgnoreCase("/back")) return;
                    currentUser.searchShowItemsBySearchKey(SearchKey.Title, searchTitle);
                    isSearchKeyValid = true;
                    break;

                case "2":
                    String searchStartDate;
                    do {
                        utils.print(text.enterStartDate);
                        searchStartDate = scanner.next();
                        if(searchStartDate.equalsIgnoreCase("/back")) return;
                    } while(!dateUtils.isValidDate(searchStartDate));
                    currentUser.searchShowItemsBySearchKey(SearchKey.StartDate, searchStartDate);
                    isSearchKeyValid = true;
                    break;

                case "3":
                    String searchEndDate;
                    do {
                        utils.print(text.enterEndDate);
                        searchEndDate = scanner.next();
                        if(searchEndDate.equalsIgnoreCase("/back")) return;
                    } while(!dateUtils.isValidDate(searchEndDate));
                    currentUser.searchShowItemsBySearchKey(SearchKey.EndDate, searchEndDate);
                    isSearchKeyValid = true;
                    break;

                case "4":
                    utils.print(text.choosePriority);
                    int searchPriority = utils.getInput("Invalid option, try again."
                            + font.ANSI_RESET + "\n" + text.choosePriority, 1, 3);
                    if(searchPriority == -1) return;
                    String priorityValue = (searchPriority == 1) ? "Low" : ((searchPriority == 2) ? "Medium" : "High");
                    currentUser.searchShowItemsBySearchKey(SearchKey.Priority, priorityValue);
                    isSearchKeyValid = true;
                    break;

                default:
                    System.err.println("Invalid input.");
                    break;
            }
        }
        scanner.close();
    }

    private void addItemToCategoryFromUser(){
        String title = getExistingTitle("Category");
        utils.print(text.chooseCategory);
        int userCategoryChoice = utils.getInput("invalid input.\n" +
                text.chooseCategory, 1, 6);
        Category category = text.categories.get(userCategoryChoice-1);
        currentUser.addItemToCategory(title,category);
    }

    private void addItemToFavoriteFromUser() {
        String title = getExistingTitle("Favorites");
        if(title.equalsIgnoreCase("/back")) return;
        currentUser.addItemToFavorite(title);
    }

    private void saveFile() {
        fileStorage.saveData(currentUser);
    }
}
