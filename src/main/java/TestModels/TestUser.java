package Test;

import models.User;

public class TestUser {

    public static void main(String[] args) {
        User user = new User("1", "John Doe", "john.doe@example.com" , "password");

        // Test getId
        if (!"1".equals(user.getId())) {
            System.out.println("testGetId failed ❌");
        } else {
            System.out.println("testGetId passed ✅");
        }

        // Test getName
        if (!"John Doe".equals(user.getUsername())) {
            System.out.println("testGetName failed ❌");
        } else {
            System.out.println("testGetName passed ✅");
        }

        // Test getEmail
        if (!"john.doe@example.com".equals(user.getEmail())) {
            System.out.println("testGetEmail failed ❌");
        } else {
            System.out.println("testGetEmail passed ✅");
        }

        // Test setId
        user.setId("2");
        if (!"2".equals(user.getId())) {
            System.out.println("testSetId failed ❌");
        } else {
            System.out.println("testSetId passed ✅");
        }

        // Test setName
        user.setUsername("Jane Doe");
        if (!"Jane Doe".equals(user.getUsername())) {
            System.out.println("testSetName failed ❌");
        } else {
            System.out.println("testSetName passed ✅");
        }

        // Test setEmail
        user.setEmail("jane.doe@example.com");
        if (!"jane.doe@example.com".equals(user.getEmail())) {
            System.out.println("testSetEmail failed ❌");
        } else {
            System.out.println("testSetEmail passed ✅");
        }

        // Test toString
        String expected = "User{id='2', username='Jane Doe', email='jane.doe@example.com', password='********'}";
        if (!expected.equals(user.toString())) {
            System.out.println("testToString failed ❌");
        } else {
            System.out.println("testToString passed ✅");
        }
    }
}