package Test;

import models.Notification;

import java.time.LocalDateTime;

public class TestNotification {

    public static void main(String[] args) {
        Notification notification1 = new Notification("1", "Task due soon", LocalDateTime.now(), 101);
        Notification notification2 = new Notification("1", "Task due soon", LocalDateTime.now(), 101);
        Notification notification3 = new Notification("2", "Task completed", LocalDateTime.now(), 102);

        // Test toString
        System.out.println(notification1.toString());

        // Test getId
        if (!"1".equals(notification1.getId())) {
            System.out.println("testGetId failed ❌");
        } else {
            System.out.println("testGetId passed ✅");
        }

        // Test getMessage
        if (!"Task due soon".equals(notification1.getMessage())) {
            System.out.println("testGetMessage failed ❌");
        } else {
            System.out.println("testGetMessage passed ✅");
        }

        // Test getTime
        if (notification1.getTime() == null) {
            System.out.println("testGetTime failed ❌");
        } else {
            System.out.println("testGetTime passed ✅");
        }

        // Test getTaskId
        if (notification1.getTaskId() != 101) {
            System.out.println("testGetTaskId failed ❌");
        } else {
            System.out.println("testGetTaskId passed ✅");
        }

        // Test setId
        notification1.setId("3");
        if (!"3".equals(notification1.getId())) {
            System.out.println("testSetId failed ❌");
        } else {
            System.out.println("testSetId passed ✅");
        }

        // Test setMessage
        notification1.setMessage("New message");
        if (!"New message".equals(notification1.getMessage())) {
            System.out.println("testSetMessage failed ❌");
        } else {
            System.out.println("testSetMessage passed ✅");
        }

        // Test setTime
        LocalDateTime newTime = LocalDateTime.now().plusDays(1);
        notification1.setTime(newTime);
        if (!newTime.equals(notification1.getTime())) {
            System.out.println("testSetTime failed ❌");
        } else {
            System.out.println("testSetTime passed ✅");
        }

        // Test setTaskId
        notification1.setTaskId(103);
        if (notification1.getTaskId() != 103) {
            System.out.println("testSetTaskId failed ❌");
        } else {
            System.out.println("testSetTaskId passed ✅");
        }

        // Test equality
        if (notification1.equals(notification2)) {
            System.out.println("testEquality failed ❌");
        } else {
            System.out.println("testEquality passed ✅");
        }

        if (!notification1.equals(notification3)) {
            System.out.println("testInequality passed ✅");
        } else {
            System.out.println("testInequality failed ❌");
        }
    }
}