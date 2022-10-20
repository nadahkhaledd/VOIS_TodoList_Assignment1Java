import classes.HelperMethods;
import classes.TodoItem;
import classes.User;
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
    static ArrayList<String> menuOptions = new ArrayList<>(
            Arrays.asList("1- Add item ", "2- Update item ", "3- Delete item", "4- Show All items",
                    "5- Show top 5 nearest by date", "6- Search by title, date (start & End), or priority",
                    "7- Add item to a category", "8- Add item to a favorite", "9- show favorites","10- Exit"));
    static ArrayList<Category> categories = new ArrayList<>(Arrays.asList(Category.Work, Category.Chores,
            Category.People, Category.Learning, Category.Other, Category.None));

    public static void main(String[] args) {
        if(!doesUserExist())
            getUserName();
        showMenu();
    }

    public static void getUserName() {
        Scanner input = new Scanner(System.in);

        System.out.println("Hello, what is your name?");

        String name = HelperMethods.validateGetStringInput("write a valid name");
        /*input.nextLine();
        while(name.matches(" +")|| name.isEmpty()){
            System.out.println("write a valid name");
            name=input.nextLine();
        }*/
        currentUser = new User(name);
    }

    public static boolean doesUserExist() {
        currentUser = fileStorage.loadData();
        if(currentUser != null) {
            return true;
        }
        return false;
    }

    public static void showMenu(){
        Scanner input = new Scanner(System.in);
        while(true)
        {
            System.out.println("\nWelcome " + currentUser.getName());
            for(String option : menuOptions)
                System.out.println(option);
            int option = input.nextInt();
            switch (option){
                case 1:
                    TodoItem item = takeItemFromUser();
                    currentUser.addTodoItem(item);
                    currentUser.showAllTodoItems();
                    saveFile();
                    break;

                case 2:
                    updateItemFromUser();
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
                    boolean isSearchKeyValid = false;
                    while (!isSearchKeyValid){
                        System.out.println("choose filter for search (1.title, 2.start date, 3.end date, 4.priority)");
                        int searchOption = input.nextInt();
                        switch (searchOption){
                            case 1:
                                System.out.print("Enter title of an item: ");
                                String searchTitle = input.next();
                                currentUser.searchShowItemsBySearchKey(SearchKey.Title, searchTitle);
                                isSearchKeyValid = true;
                                break;

                            case 2:
                                System.out.print("Enter start date of an item: ");
                                String searchStartDate = input.next();
                                while(!HelperMethods.isValidDate(searchStartDate)){
                                    System.out.print("Enter start date of an item: ");
                                    searchStartDate = input.next();
                                }
                                currentUser.searchShowItemsBySearchKey(SearchKey.StartDate, searchStartDate);
                                isSearchKeyValid = true;
                                break;

                            case 3:
                                System.out.print("Enter end date of an item: ");
                                String searchEndDate = input.next();
                                while(!HelperMethods.isValidDate(searchEndDate)){
                                    System.out.print("Enter end date of an item: ");
                                    searchEndDate = input.next();
                                }
                                currentUser.searchShowItemsBySearchKey(SearchKey.EndDate, searchEndDate);
                                isSearchKeyValid = true;
                                break;

                            case 4:
                                System.out.print("Choose priority of an item (1.Low, 2.Medium, 3.High): ");
                                int searchPriority = input.nextInt();
                                if(searchPriority < 1 || searchPriority > 3){
                                    System.out.println("Invalid option, try again.");
                                    System.out.print("Choose priority of an item (1.Low, 2.Medium, 3.High): ");
                                    searchPriority = input.nextInt();
                                }
                                String priorityValue = (searchPriority == 1) ? "Low" : ((searchPriority == 2) ? "Medium" : "High");
                                currentUser.searchShowItemsBySearchKey(SearchKey.Priority, priorityValue);
                                isSearchKeyValid = true;
                                break;

                            default:
                                System.out.println("Invalid input.");
                                break;
                        }
                    }
                    break;

                case 7:
                    addItemToCategoryFromUser();
                    saveFile();
                    break;

                case 8:
                    //input.nextLine();
                    System.out.println("Enter title of item to be added to favorite:");
                    String title = HelperMethods.validateGetStringInput("Enter a valid title");
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

    private static TodoItem takeItemFromUser(){
        System.out.println("Enter new data...");
        Scanner data = new Scanner(System.in);
        System.out.println("Enter title:");
        String title = HelperMethods.validateGetStringInput("enter a valid title");//data.nextLine();

        System.out.println("Enter description:");
        String description = HelperMethods.validateGetStringInput("enter a valid description");//data.nextLine();

        System.out.println("Choose priority for the item (1.Low, 2.Medium, 3.High):");
        int userPriorityChoice = HelperMethods.validateGetIntegerInput(
                "invalid choice.\nChoose priority for the item (1.Low, 2.Medium, 3.High):", 1, 3
        );
        /*while (userPriorityChoice < 1 || userPriorityChoice > 3){
            System.out.println("invalid choice.\nChoose priority for the item (1.Low, 2.Medium, 3.High):");
            userPriorityChoice = data.nextInt();
        }*/
        Priority priority = (userPriorityChoice == 1)? Priority.Low :
                ((userPriorityChoice == 2)?Priority.Medium : Priority.High);

        System.out.println("Choose category for the item " +
                "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)");
        int userCategoryChoice = HelperMethods.validateGetIntegerInput("invalid input.\nChoose category for the item " +
        "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)", 1, 6);
        /*while (userCategoryChoice<1 || userCategoryChoice>6){
            System.out.println("invalid input.\nChoose category for the item " +
                    "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)");
            userCategoryChoice = data.nextInt();
        }*/
        Category category = categories.get(userCategoryChoice-1);

        //data.nextLine();
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

    private static void updateItemFromUser(){
        System.out.println("Enter title of item to be updated:");
        String oldTitle = HelperMethods.validateGetStringInput("Enter a valid title");
        TodoItem newItem = takeItemFromUser();
        while (!currentUser.updateTodoItem(newItem, oldTitle)){
            System.out.println("Enter title of item to be updated:");
            oldTitle = HelperMethods.validateGetStringInput("Enter a valid title");
            newItem = takeItemFromUser();
        }
    }

    private static void deleteItemByUser(){
        System.out.println("Enter title of item to be deleted:");
        String title = HelperMethods.validateGetStringInput("Enter a valid title");
        currentUser.deleteTodoItem(title);
    }

    private static void addItemToCategoryFromUser(){
        //Scanner input = new Scanner(System.in);

        System.out.println("Enter title of item to be added to Category");
        String title = HelperMethods.validateGetStringInput("Enter a valid title");

        System.out.println("Choose category for the item " +
                "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)");
        int userCategoryChoice = HelperMethods.validateGetIntegerInput("invalid input.\nChoose category for the item " +
                "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)", 1, 6);
        /*while (userCategoryChoice<1 || userCategoryChoice>6){
            System.out.println("invalid input.\nChoose category for the item " +
                    "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)");
            userCategoryChoice =input.nextInt();
        }
        input.nextLine();*/
        Category category = categories.get(userCategoryChoice-1);

        currentUser.addItemToCategory(title,category);
    }

    private static void saveFile() {
        fileStorage.saveData(currentUser);
    }

}
