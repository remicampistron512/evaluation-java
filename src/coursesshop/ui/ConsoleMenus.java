package coursesshop.ui;

import coursesshop.business.CourseBuyingService;
import coursesshop.model.Category;
import coursesshop.model.Course;
import coursesshop.model.enums.AttendanceMode;

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

    private void printHeader(String title) {
        System.out.println("============================================================");
        System.out.println(title);
        System.out.println("============================================================");
    }

    private void printCourses(String title, List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        printHeader(title);

        System.out.printf("%-10s %-35s %-90s %-12s %10s %-12s%n",
                "Id", "Name", "Description", "Duration", "Price", "Mode");

        for (Course course : courses) {
            System.out.printf("%-10d %-35s %-90s %-12s %10s %-12s%n",
                    course.getId(),
                    course.getName(),
                    course.getDescription(),
                    course.getDurationDays() + " days",
                    course.getPrice(),
                    course.getMode());
        }
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1) Display courses");
            System.out.println("2) Display courses by category");
            System.out.println("3) Display courses by keyword");
            System.out.println("4) Display courses by attendance");
            System.out.println("0) Exit");

            int choice = readInt(CHOICE_TEXT, 0, 5);
            switch (choice) {
                case 1 -> listCourses();
                case 2 -> chooseCategory();
                case 3 -> chooseKeyword();
                case 4 -> chooseAttendance();
                case 0 -> { return; } // exit application
                default -> { return; }
            }
        }
    }

    private void chooseKeyword() {
        System.out.println("\n--- Enter a keyword ? ---");
        String keyword = in.nextLine();
        listCoursesByKeyword(keyword);
    }

    private void chooseAttendance() {
        List<AttendanceMode> attendanceModes = listAttendanceModes();
        System.out.println("\n--- Enter the attendance id ? ---");
        int attendanceModeId = readInt(CHOICE_TEXT, 0, attendanceModes.size());
        AttendanceMode attendanceMode = attendanceModes.get(attendanceModeId - 1); // index -> enum
        listCoursesByAttendance(attendanceMode);
    }

    private void listCoursesByAttendance(AttendanceMode attendanceMode) {
        printCourses("Courses \"" + attendanceMode + "\"", service.listCoursesByAttendance(attendanceMode));

    }


    private List<AttendanceMode>  listAttendanceModes() {
        printHeader("Attendance modes");

        System.out.printf(
                "%-10s %-35s%n",
                "Id", "Name"
        );
        List<AttendanceMode> attendanceModes = service.listAttendanceModes();

        int i = 0;
        for (AttendanceMode attendanceMode : attendanceModes){
            i++;
            System.out.printf(
                    "%-10d  %-35s%n",

                    i,
                    attendanceMode

            );
        }
        return attendanceModes;
    }

    private void listCoursesByKeyword(String keyword) {
        printCourses("Courses \"" + keyword + "\"", service.listCoursesByKeyword(keyword));
    }

    private void chooseCategory(){
        listCourseCategory();
        List<Category> categoriesList = service.listCategories();
        System.out.println("\n--- Display courses from which Category ? ---");
        int choice = readInt(CHOICE_TEXT, 0, categoriesList.size());
        listCoursesByCategory(choice);
    }


    private void listCoursesByCategory(int categoryId){

        printCourses("Courses", service.listCoursesByCategory(categoryId));


    }

    private void listCourseCategory() {
        List<Category> categoryList = service.listCategories();

        if (categoryList == null || categoryList.isEmpty()) {
            System.out.println("No categories  found.");
            return;
        }
        printHeader("Choose a category");

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

        printCourses("Courses", service.listCourses());

    }

}
