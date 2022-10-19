import classes.HelperMethods;
import classes.TodoItem;
import classes.User;
import enums.Category;
import enums.Priority;
import enums.SearchKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Main {
    static User currentUser;
    static ArrayList<String> menuOptions = new ArrayList<>(
            Arrays.asList("1- Add item ", "2- Update item ", "3- Delete item", "4- Show All items",
                    "5- Show top 5 nearest by date", "6- Search by title, date (start & End), or priority",
                    "7- Add item to a category", "8- Add item to a favorite", "9- show favorites","10- Exit"));
    static ArrayList<Category> categories = new ArrayList<>(Arrays.asList(Category.Work, Category.Chores,
            Category.People, Category.Learning, Category.Other, Category.None));

    public static void main(String[] args) {
        showMenu();
    }

    public static void showMenu(){
        Scanner input = new Scanner(System.in);

        System.out.println("Hello, what is your name?");
        String name = input.nextLine();
        currentUser = new User(name);
        while(true)
        {
            System.out.println("\nWelcome " + name);
            for(String option : menuOptions)
                System.out.println(option);
            int option = input.nextInt();
            switch (option){
                case 1:
                    TodoItem item = takeItemFromUser();
                    currentUser.addTodoItem(item);
                    currentUser.showAllTodoItems();
                    break;

                case 2:
                    updateItemFromUser();
                    break;

                case 3:
                    deleteItemByUser();
                    break;

                case 4:
                    // print to be modified
                    currentUser.showAllTodoItems();
                    break;

                case 5:
                    currentUser.showTop5ItemsByDate();
                    break;

                case 6:
                    System.out.println("choose filter for search (1.title, 2.start date, 3.end date, 4.priority)");
                    int searchOption = input.nextInt();
                    switch (searchOption){
                        case 1:
                            System.out.print("Enter title of an item:");
                            String searchTitle = input.next();
                            currentUser.searchShowItemsBySearchKey(SearchKey.Title, searchTitle);
                            break;

                        case 2:
                            System.out.print("Enter start date of an item:");
                            String searchStartDate = input.next();
                            while(!HelperMethods.isValidDate(searchStartDate)){
                                System.out.print("Enter start date of an item:");
                                searchStartDate = input.next();
                            }
                            currentUser.searchShowItemsBySearchKey(SearchKey.StartDate, searchStartDate);
                            break;

                        case 3:
                            System.out.print("Enter end date of an item:");
                            String searchEndDate = input.next();
                            while(!HelperMethods.isValidDate(searchEndDate)){
                                System.out.print("Enter end date of an item:");
                                searchEndDate = input.next();
                            }
                            currentUser.searchShowItemsBySearchKey(SearchKey.EndDate, searchEndDate);
                            break;


                    }

                case 8:
                    input.nextLine();
                    System.out.println("Enter title of item to be added to favorite:");
                    String title = input.nextLine();
                    currentUser.addItemToFavorite(title);
                    break;

                case 9:
                    currentUser.printFavorites();
                    break;
                case 10:
                default:
                    System.exit(0);
                    break;
            }
        }

    }

    private static TodoItem takeItemFromUser(){
        System.out.println("Enter new data...");
        Scanner data = new Scanner(System.in);
        System.out.println("Enter title:");
        String title = data.nextLine();

        System.out.println("Enter description:");
        String description = data.nextLine();

        System.out.println("Choose priority for the item (1.Low, 2.Medium, 3.High):");
        int userPriorityChoice = data.nextInt();
        while (userPriorityChoice != 1 && userPriorityChoice != 2 && userPriorityChoice != 3){
            System.out.println("invalid input.\nChoose priority for the item (1.Low, 2.Medium, 3.High):");
            userPriorityChoice = data.nextInt();
        }
        Priority priority = (userPriorityChoice == 1)? Priority.Low :
                ((userPriorityChoice == 2)?Priority.Medium : Priority.High);

        System.out.println("Choose category for the item " +
                "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)");
        int userCategoryChoice = data.nextInt();
        while (userCategoryChoice<1 || userCategoryChoice>6){
            System.out.println("invalid input.\nChoose category for the item " +
                    "(1.work, 2.chores, 3.People, 4.Learning, 5.Other, 6.No category)");
            userCategoryChoice = data.nextInt();
        }
        Category category = categories.get(userCategoryChoice-1);

        data.nextLine();
        System.out.println("Enter start date of the item (e.g. dd-mm-yyyy)");
        String startDateString = data.nextLine();
        while(!HelperMethods.isValidDate(startDateString)){
            System.out.println("Enter start date of the item (e.g. dd-mm-yyyy)");
            startDateString = data.nextLine();
        }
        Date startDate = HelperMethods.convertStringToDate(startDateString);

        System.out.println("Enter end date of the item (e.g. dd-mm-yyyy)");
        String endDateString = data.nextLine();
        while(!HelperMethods.isValidEndDate(startDate, endDateString)){
            System.out.println("Enter end date of the item (e.g. dd-mm-yyyy)");
            endDateString = data.nextLine();
        }
        Date endDate = HelperMethods.convertStringToDate(endDateString);

        TodoItem item = new TodoItem(title, description, priority, category, startDate, endDate);
        return item;
    }

    private static void updateItemFromUser(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter title of item to be updated:");
        String oldTitle = input.nextLine();
        TodoItem newItem = takeItemFromUser();
        while (!currentUser.updateTodoItem(newItem, oldTitle)){
            System.out.println("Enter title of item to be updated:");
            oldTitle = input.nextLine();
            newItem = takeItemFromUser();
        }
    }

    private static void deleteItemByUser(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter title of item to be deleted:");
        String title = input.nextLine();
        currentUser.deleteTodoItem(title);
    }
    

    

}
