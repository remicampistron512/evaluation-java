package coursesshop.ui;

import coursesshop.business.CourseBuyingService;
import coursesshop.model.Category;
import coursesshop.model.Course;
import coursesshop.model.enums.AttendanceMode;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for browsing courses.
 *
 * <p>This class is responsible for:
 * </p>
 * <ul>
 *   <li>Displaying the main menu and reading user choices</li>
 *   <li>Printing lists of courses, categories, and attendance modes</li>
 *   <li>Delegating data retrieval to {@link CourseBuyingService}</li>
 * </ul>
 *
 */
public class ConsoleMenus {

  /**
   * Default prompt text for integer choices.
   */
  private static final String CHOICE_TEXT = "Choose: ";
  /**
   * Shared scanner used to read user input from standard input.
   */
  private final Scanner in = new Scanner(System.in);
  /**
   * Business service used to retrieve courses and reference data.
   */
  private final CourseBuyingService service;

  /**
   * Builds a new console menu controller.
   *
   * @param service the business service used by the UI (must not be {@code null})
   */
  public ConsoleMenus(CourseBuyingService service) {
    this.service = service;
  }

  /**
   * Starts the console UI.
   *
   * <p>This method blocks until the user exits from the main menu.
   * </p>
   */
  public void run() {
    mainMenu(); // blocks until user exits
    System.out.println("Goodbye.");
  }

  /**
   * Prints a consistent visual header for console sections.
   *
   * @param title the title to display
   */
  private void printHeader(String title) {
    System.out.println("============================================================");
    System.out.println(title);
    System.out.println("============================================================");
  }

  /**
   * Prints a list of courses in a formatted table.
   *
   * @param title   section title to display above the table
   * @param courses list of courses to print
   */
  private void printCourses(String title, List<Course> courses) {
    // Guard clause: handle empty results.
    if (courses == null || courses.isEmpty()) {
      System.out.println("No courses found.");
      return;
    }

    // Section header.
    printHeader(title);

    // Table header.
    System.out.printf("%-10s %-35s %-90s %-12s %10s %-12s%n",
        "Id", "Name", "Description", "Duration", "Price", "Mode");

    // Table rows.
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

  /**
   * Main menu loop.
   *
   * <p>Displays the available user actions and dispatches to the corresponding handlers.
   * </p>
   */
  private void mainMenu() {
    while (true) {
      System.out.println("\n=== MAIN MENU ===");
      System.out.println("1) Display courses");
      System.out.println("2) Display courses by category");
      System.out.println("3) Display courses by keyword");
      System.out.println("4) Display courses by attendance");
      System.out.println("0) Exit");

      int choice = readInt(CHOICE_TEXT, 0, 4);

      switch (choice) {
        case 1 -> listCourses();
        case 2 -> chooseCategory();
        case 3 -> chooseKeyword();
        case 4 -> chooseAttendance();
        case 0 -> {
          return;
        } // Exit application
        default -> {
          return;
        } // Defensive: unexpected choice
      }
    }
  }

  /**
   * Prompts the user for a keyword and displays matching courses.
   */
  private void chooseKeyword() {
    System.out.println("\n--- Enter a keyword ? ---");
    String keyword = in.nextLine();

    // Retrieve and print matching courses.
    printCourses("Courses \"" + keyword + "\"", service.listCoursesByKeyword(keyword));
  }

  /**
   * Displays available attendance modes, prompts the user to choose one, then prints courses
   * matching that attendance mode.
   */
  private void chooseAttendance() {
    // Display available attendance modes and keep the list so we can map index -> enum.
    List<AttendanceMode> attendanceModes = listAttendanceModes();

    System.out.println("\n--- Enter the attendance id ? ---");

    // Read a 1-based index and convert it to 0-based list index.
    int attendanceModeId = readInt("Choose attendance id", 1, attendanceModes.size());
    AttendanceMode attendanceMode = attendanceModes.get(attendanceModeId - 1);

    // Retrieve and print matching courses.
    printCourses("Courses \"" + attendanceMode + "\"",
        service.listCoursesByAttendance(attendanceMode));
  }

  /**
   * Prints all attendance modes as a numbered list (1.n) and returns the list so the caller can
   * map the selected index to an {@link AttendanceMode}.
   *
   * @return the attendance modes in display order
   */
  private List<AttendanceMode> listAttendanceModes() {
    printHeader("Attendance modes");

    // Table header.
    System.out.printf("%-10s %-35s%n", "Id", "Name");

    // Load modes from service/DAO.
    List<AttendanceMode> attendanceModes = service.listAttendanceModes();

    // Print each mode with a 1-based index.
    int i = 0;
    for (AttendanceMode attendanceMode : attendanceModes) {
      i++;
      System.out.printf("%-10d %-35s%n", i, attendanceMode);
    }

    return attendanceModes;
  }

  /**
   * Displays categories, prompts the user to choose one, then prints courses for that category.
   *
   */
  private void chooseCategory() {
    listCourseCategory();
    List<Category> categoriesList = service.listCategories();

    System.out.println("\n--- Display courses from which Category ? ---");


    int choice = readInt(CHOICE_TEXT, 0, categoriesList.size());

    printCourses("Courses", service.listCoursesByCategory(choice));
  }

  /**
   * Prints all categories with their database identifier.
   */
  private void listCourseCategory() {
    List<Category> categoryList = service.listCategories();

    // Guard clause: no categories to display.
    if (categoryList == null || categoryList.isEmpty()) {
      System.out.println("No categories found.");
      return;
    }

    printHeader("Choose a category");

    // Table header.
    System.out.printf("%-10s %-35s%n", "Id", "Name");
    System.out.println("---------------------------------------------------------------------");

    // Print each category (uses DB id).
    for (Category category : categoryList) {
      System.out.printf("%-10d %-35s%n", category.getId(), category.getName());
    }
  }

  /**
   * Reads an integer from the console within a specified inclusive range.
   *
   * <p>The method loops until a valid integer is provided.
   * </p>
   *
   * @param prompt prompt displayed to the user
   * @param min    minimum accepted value (inclusive)
   * @param max    maximum accepted value (inclusive)
   * @return a validated integer within [min.max]
   */
  private int readInt(String prompt, int min, int max) {
    while (true) {
      System.out.print(prompt);
      String s = in.nextLine().trim();

      try {
        int v = Integer.parseInt(s);

        // Range validation.
        if (v < min || v > max) {
          System.out.printf("Enter %d..%d%n", min, max);
          continue;
        }

        return v;

      } catch (NumberFormatException _) {
        // Intentionally ignore the exception details and re-prompt the user.
        System.out.println("Invalid number.");
      }
    }
  }

  /**
   * Displays all courses.
   */
  private void listCourses() {
    printCourses("Courses", service.listCourses());
  }
}
