package Test;

import models.Task;

import models.Status;
import models.Priority;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestTask {

    public static void main(String[] args) {
        Map<String, Date> dateTime = new HashMap<>();
        dateTime.put("startDate", new Date());
        dateTime.put("startTime", new Date());
        dateTime.put("endDate", new Date());
        dateTime.put("endTime", new Date());

        Task task1 = new Task("1", "Test Task", "This is a test task", dateTime, Priority.MEDIUM, Status.PENDING, "1");
        Task task2 = new Task("1", "Test Task", "This is a test task", dateTime, Priority.MEDIUM, Status.PENDING, "1");
        Task task3 = new Task("2", "Another Task", "This is another test task", dateTime, Priority.HIGH, Status.IN_PROGRESS, "2");

        System.out.println(task1.toString());

        // Test setting invalid priority
        try {
            task1.setPriority(Priority.valueOf("INVALID_PRIORITY"));
            System.out.println("testSetInvalidPriority failed ❌");
        } catch (IllegalArgumentException e) {
            System.out.println("testSetInvalidPriority passed ✅");
        }

        // Test setting invalid status
        try {
            task1.setStatus(Status.valueOf("INVALID_STATUS"));
            System.out.println("testSetInvalidStatus failed ❌");
        } catch (IllegalArgumentException e) {
            System.out.println("testSetInvalidStatus passed ✅");
        }

        // Test isEqual
        if (task1.isEqual(task2)) {
            System.out.println("testIsEqual passed ✅");
        } else {
            System.out.println("testIsEqual failed ❌");
        }

        if (!task1.isEqual(task3)) {
            System.out.println("testIsNotEqual passed ✅");
        } else {
            System.out.println("testIsNotEqual failed ❌");
        }
    }
}