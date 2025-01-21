package Test;

import models.Board;
import models.Task;
import models.Status;
import models.Priority;

import java.util.Set;

public class TestBoard {

    public static void main(String[] args) {
        // Create a board
        Board board = new Board("1");

        // Create some tasks
        Task task1 = new Task("1", "Task 1", "Description 1", null, Priority.HIGH, Status.PENDING, "Category1");
        Task task2 = new Task("2", "Task 2", "Description 2", null, Priority.MEDIUM, Status.IN_PROGRESS, "Category1");
        Task task3 = new Task("3", "Task 3", "Description 3", null, Priority.LOW, Status.COMPLETE, "Category2");

        // Add tasks to the board
        board.addTask(task1);
        board.addTask(task2);
        board.addTask(task3);

        // Test addTask
        if (board.getTasksByTitle("Task 1").size() != 1) {
            System.out.println("testAddTask failed ❌");
        } else {
            System.out.println("testAddTask passed ✅");
        }

        // Test removeTask
        board.removeTask(task1);
        if (board.getTasksByTitle("Task 1").size() != 0) {
            System.out.println("testRemoveTask failed ❌");
        } else {
            System.out.println("testRemoveTask passed ✅");
        }

        // Test getTasksByCategory
        Set<Task> category1Tasks = board.getTasksByCategory("Category1");
        if (category1Tasks.size() != 1 || !category1Tasks.contains(task2)) {
            System.out.println("testGetTasksByCategory failed ❌");
        } else {
            System.out.println("testGetTasksByCategory passed ✅");
        }

        // Test getTasksByTitle
        Set<Task> tasksWithTitle2 = board.getTasksByTitle("Task 2");
        if (tasksWithTitle2.size() != 1 || !tasksWithTitle2.contains(task2)) {
            System.out.println("testGetTasksByTitle failed ❌");
        } else {
            System.out.println("testGetTasksByTitle passed ✅");
        }

        // Test getPendingTasks
        Set<Task> pendingTasks = board.getPendingTasks();
        if (pendingTasks.size() != 0) { // task1 was removed, so no pending tasks
            System.out.println("testGetPendingTasks failed ❌");
        } else {
            System.out.println("testGetPendingTasks passed ✅");
        }

        // Test getInProgressTasks
        Set<Task> inProgressTasks = board.getIinProgressTasks();
        if (inProgressTasks.size() != 1 || !inProgressTasks.contains(task2)) {
            System.out.println("testGetInProgressTasks failed ❌");
        } else {
            System.out.println("testGetInProgressTasks passed ✅");
        }

        // Test getCompleteTasks
        Set<Task> completeTasks = board.getCompleteTasks();
        if (completeTasks.size() != 1 || !completeTasks.contains(task3)) {
            System.out.println("testGetCompleteTasks failed ❌");
        } else {
            System.out.println("testGetCompleteTasks passed ✅");
        }

        // Test getHighPriorityTasks
        Set<Task> highPriorityTasks = board.getHighPriorityTasks();
        if (highPriorityTasks.size() != 0) { // task1 was removed, so no high-priority tasks
            System.out.println("testGetHighPriorityTasks failed ❌");
        } else {
            System.out.println("testGetHighPriorityTasks passed ✅");
        }

        // Test getMediumPriorityTasks
        Set<Task> mediumPriorityTasks = board.getMediumPriorityTasks();
        if (mediumPriorityTasks.size() != 1 || !mediumPriorityTasks.contains(task2)) {
            System.out.println("testGetMediumPriorityTasks failed ❌");
        } else {
            System.out.println("testGetMediumPriorityTasks passed ✅");
        }

        // Test getLowPriorityTasks
        Set<Task> lowPriorityTasks = board.getLowPriorityTasks();
        if (lowPriorityTasks.size() != 1 || !lowPriorityTasks.contains(task3)) {
            System.out.println("testGetLowPriorityTasks failed ❌");
        } else {
            System.out.println("testGetLowPriorityTasks passed ✅");
        }

        System.out.println(board);
    }
}