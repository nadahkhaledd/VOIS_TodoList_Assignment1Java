import classes.TodoItem;
import classes.User;
import enums.Category;
import enums.Priority;
import storage.FileStorage;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        if(!isUserExist()) getUserName();
        showMenu();
    }

    public static void getUserName() {
        Scanner input = new Scanner(System.in);
        System.out.println("Hello, what is your name?");
        String name = input.nextLine();
        currentUser = new User(name);
    }

    public static boolean isUserExist() {
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
                    // print to be modified
                    currentUser.showAllTodoItems();
                    break;

                case 5:
                    currentUser.showTop5ItemsByDate();
                    break;
                case 7:
                    saveFile();
                    break;
                case 8:
                    input.nextLine();
                    System.out.println("Enter title of item to be added to favorite:");
                    String title = input.nextLine();
                    currentUser.addItemToFavorite(title);
                    saveFile();
                    break;

                case 9:
                    currentUser.printFavorites();
                    saveFile();
                    break;
                case 10:
                    saveFile();
                    System.exit(0);
                default:
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
        while(!isValidDate(startDateString)){
            System.out.println("Enter start date of the item (e.g. dd-mm-yyyy)");
            startDateString = data.nextLine();
        }
        Date startDate = convertStringToDate(startDateString);

        System.out.println("Enter end date of the item (e.g. dd-mm-yyyy)");
        String endDateString = data.nextLine();
        while(!isValidEndDate(startDate, endDateString)){
            System.out.println("Enter end date of the item (e.g. dd-mm-yyyy)");
            endDateString = data.nextLine();
        }
        Date endDate = convertStringToDate(endDateString);

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
    
    private static boolean isValidDate(String dateValue){
        try{
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            date.setLenient(false);
            date.parse(dateValue);
            return true;
        }
        catch (ParseException e){
            System.out.println("invalid date format");
            return false;
        }
    }

    private static boolean isValidEndDate(Date startDate, String dateString){
        if(isValidDate(dateString)){
            Date endDate = convertStringToDate(dateString);
            if(endDate.compareTo(startDate) != -1)
                return true;
            else{
                System.out.println("end date is incorrect.");
                return false;
            }
        }
        return false;
    }
    
    private static Date convertStringToDate(String dateString){
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
            return date;
        } catch (ParseException e) {
            saveFile();
            throw new RuntimeException(e);
        }
    }

    private static void saveFile() {
        fileStorage.saveData(currentUser);
    }

}
