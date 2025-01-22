package TestDatabase;


import utils.Database;
import utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;


import models.User;
import models.Task;
import models.Board;
import models.Status;
import models.Priority;
import models.Notification;
import models.TaskManager;

public class TestDatabase {

    public static Map<String, Date> createDateTime(int year, int month, int day, int startHour, int startMinute, int endHour, int endMinute) {
        Map<String, Date> dateTime = new HashMap<>();
        Calendar calendar = Calendar.getInstance();

        // Set start date and time
        calendar.set(year, month - 1, day, startHour, startMinute); // month is 0-based in Calendar
        Date startDate = calendar.getTime();
        Date startTime = calendar.getTime();

        // Set end date and time
        calendar.set(year, month - 1, day, endHour, endMinute); // month is 0-based in Calendar
        Date endDate = calendar.getTime();
        Date endTime = calendar.getTime();

        dateTime.put("startDate", startDate);
        dateTime.put("startTime", startTime);
        dateTime.put("endDate", endDate);
        dateTime.put("endTime", endTime);

        return dateTime;
    }


    public static void main(String[] args) {

        // Test Connection to the database
        try{
            if(DatabaseConnection.getConnection().isValid(1)){
                System.out.println("Connected to the database ✅");
            }
        }
        catch(Exception e) {
            System.out.println("Connection to the database failed ❌");
            System.out.println(e.getMessage());
        }

        System.out.println("-------------------------------------------------------------");
        // Test loadUsers
        try{
            Set<User> users = Database.loadUsers();
            if(users.size() > 0){
                System.out.println("testLoadUsers passed ✅");
                users.stream().forEach(user -> System.out.println(user.toString()));
            }
            else{
                System.out.println("testLoadUsers passed ✅ , But there are no users found");
            }
        }
        catch(Exception e){
            System.out.println("testLoadUsers failed ❌");
            System.out.println(e.getMessage());
        }

        System.out.println("-------------------------------------------------------------");
        // Test loadTasks
        try{
            Set<Task> tasks = Database.loadTasks();
            if(tasks.size() > 0){
                System.out.println("testloadTasks passed ✅");
                tasks.stream().forEach(task -> System.out.println(task.toString()));
            }
            else{
                System.out.println("testloadTasks passed ✅ , But there are no tasks found");
            }
        }
        catch(Exception e){
            System.out.println("testloadTasks failed ❌");
            System.out.println(e.getMessage());
        }

        System.out.println("-------------------------------------------------------------");
        // Test loadBoards
        try{
            Set<Board> boards = Database.loadBoards();
            if(boards.size() > 0){
                System.out.println("testloadBoards passed ✅");
                boards.stream().forEach(board -> System.out.println(board.toString()));
            }
            else{
                System.out.println("testloadBoards passed ✅ , But there are no boards found");
            }
        }
        catch(Exception e){
            System.out.println("testloadBoards failed ❌");
            System.out.println(e.getMessage());
        }

        System.out.println("-------------------------------------------------------------");
        // Test saveUser
        try {
            User user = new User("6", "ahmed mensouri", "ahmed.mensouro@example.com", "password123");
            boolean userSaved = Database.saveUser(user);
            if (userSaved) {
                System.out.println("testSaveUser passed ✅");
            } else {
                System.out.println("testSaveUser failed ❌");
            }
        } catch (Exception e) {
            System.out.println("testSaveUser failed ❌");
            System.out.println(e.getMessage());
        }

        System.out.println("-------------------------------------------------------------");
        // Test saveBoard


        Set<Task> tasks = new HashSet<>();
            // Initialize tasks with unique date and time values
            tasks.add(new Task("1", "Complete Project Proposal", "Write and submit the project proposal by Friday.",
                    createDateTime(2023, 10, 1, 9, 0, 10, 0), Priority.MEDIUM, Status.NOT_STARTED, "1" , "1"));
            tasks.add(new Task("2", "Buy Groceries", "Milk, Eggs, Bread, and Vegetables.",
                    createDateTime(2023, 10, 1, 12, 30, 13, 30), Priority.HIGH, Status.IN_PROGRESS, "1" ,"1"));
            tasks.add(new Task("3", "Schedule Team Meeting", "Coordinate with the team to schedule a meeting for next week.",
                    createDateTime(2023, 10, 2, 14, 0, 15, 0), Priority.LOW, Status.COMPLETE, "1", "1"));
            tasks.add(new Task("4", "Prepare Presentation Slides", "Create the slides for the upcoming client presentation.",
                    createDateTime(2023, 10, 3, 10, 0, 11, 0), Priority.HIGH, Status.NOT_STARTED, "1", "1"));
            tasks.add(new Task("5", "Update Website", "Make necessary updates to the company website based on feedback.",
                    createDateTime(2023, 10, 4, 16, 45, 17, 45), Priority.MEDIUM, Status.IN_PROGRESS, "1", "1"));
            tasks.add(new Task("6", "Write Blog Post", "Write a blog post on the latest project updates.",
                    createDateTime(2023, 10, 5, 8, 15, 9, 15), Priority.LOW, Status.NOT_STARTED, "1", "1"));
            tasks.add(new Task("7", "Respond to Emails", "Reply to client emails and organize inbox.",
                    createDateTime(2023, 10, 6, 11, 0, 12, 0), Priority.HIGH, Status.NOT_STARTED, "1", "1"));
            tasks.add(new Task("8", "Refactor Code", "Improve code quality for the main application module.",
                    createDateTime(2023, 10, 7, 13, 30, 14, 30), Priority.MEDIUM, Status.IN_PROGRESS, "1", "1"));
            tasks.add(new Task("9", "Team Building Activity", "Organize an informal team-building event for the team next month.",
                    createDateTime(2023, 10, 8, 18, 0, 19, 0), Priority.LOW, Status.NOT_STARTED, "1", "1"));
            tasks.add(new Task("10", "Review Budget", "Review the project's budget and allocate resources for the next phase.",
                    createDateTime(2023, 10, 9, 15, 0, 16, 0), Priority.HIGH, Status.NOT_STARTED, "1", "1"));



        try {
            Board board = new Board("5");
            board.setTasks(tasks);
            board.setUserId("1");
            boolean boardSaved = Database.saveBoard(board);
            if (boardSaved) {
                System.out.println("testSaveBoard passed ✅");
            } else {
                System.out.println("testSaveBoard failed ❌");
            }
        } catch (Exception e) {
            System.out.println("testSaveBoard failed ❌");
            System.out.println(e.getMessage());
        }
    }
}
