package coursesshop.ui;

import coursesshop.business.CourseBuyingService;
import coursesshop.model.Category;
import coursesshop.model.Course;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenus {
    private final Scanner in = new Scanner(System.in);
    private static final String CHOICE_TEXT = "Choose: ";
    private final CourseBuyingService service;
    public ConsoleMenus(CourseBuyingService service) {
        this.service = service;
    }

    public void run() {
        mainMenu();   // blocks until user exits
        System.out.println("Goodbye.");
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1) Display courses");
            System.out.println("2) Display courses by category");
            System.out.println("3) Display courses by keyword");
            System.out.println("4) Display courses by attendance");
            System.out.println("5) Log in/Sign in");
            System.out.println("0) Exit");

            int choice = readInt(CHOICE_TEXT, 0, 5);
            switch (choice) {
                case 1 -> listCourses();
                case 2 -> chooseCategory();
                case 3 -> { return; }
                case 4 -> { return; }
                case 5 -> { return; }
                case 0 -> { return; } // exit application
            }
        }
    }
    private void chooseCategory(){
        listCourseCategory();
        List<Category> categoriesList = service.listCategories();
        System.out.println("\n--- Display courses from which Category ? ---");
        int choice = readInt(CHOICE_TEXT, 0, categoriesList.size());

    }
    private void listCourseCategory() {
        List<Category> categoryList = service.listCategories();

        if (categoryList == null || categoryList.isEmpty()) {
            System.out.println("No categories  found.");
            return;
        }
        System.out.println("============================== Choose a category ==============================");

        System.out.printf(
                "%-10s %-35s%n",
                "Id", "Name"
        );

        System.out.println("---------------------------------------------------------------------");

        for (Category category : categoryList) {


            System.out.printf(
                    "%-10d  %-35s%n",

                    category.getId(),
                    category.getName()

            );
        }

    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) {
                    System.out.printf("Enter %d..%d%n", min, max);
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    private void listCourses(){
        List<Course> coursesList = service.listCourses();

        if (coursesList == null || coursesList.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        System.out.println("============================== Available courses ==============================");

        System.out.printf(
                "%-10s %-35s %-90s %-12s %10s %-12s%n",
                "Id", "Name", "Description", "Duration", "Price", "Mode"
        );

        System.out.println("---------------------------------------------------------------------");

        for (Course course : coursesList) {
            String duration = course.getDurationDays() + " days";

            System.out.printf(
                    "%-10d  %-35s %-90s %-12s %10.2f %-12s%n",

                    course.getId(),
                    course.getName(),
                    course.getDescription(),
                    duration,
                    course.getPrice(),
                    course.getMode()
            );
        }

    }

}
