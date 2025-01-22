package TestModels;

import models.Category;

public class TestCategory {

        public static void main(String[] args) {
            Category category = new Category("1", "Work", "this is a work category");

            // Test getId
            if (!"1".equals(category.getId())) {
                System.out.println("testGetId failed ❌");
            } else {
                System.out.println("testGetId passed ✅");
            }

            // Test getName
            if (!"Work".equals(category.getName())) {
                System.out.println("testGetName failed ❌");
            } else {
                System.out.println("testGetName passed ✅");
            }

            // Test setId
            category.setId("2");
            if (!"2".equals(category.getId())) {
                System.out.println("testSetId failed ❌");
            } else {
                System.out.println("testSetId passed ✅");
            }

            // Test setName
            category.setName("Personal");
            if (!"Personal".equals(category.getName())) {
                System.out.println("testSetName failed ❌");
            } else {
                System.out.println("testSetName passed ✅");
            }

            // Test toString
            String expected = "Category{id='2', name='Personal', description='this is a work category'}";
            if (!expected.equals(category.toString())) {
                System.out.println("testToString failed ❌");
            } else {
                System.out.println("testToString passed ✅");
            }
        }

}
