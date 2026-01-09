package coursesshop.business;

import coursesshop.dao.AttendanceModeDao;
import coursesshop.dao.CartDao;
import coursesshop.dao.CategoryDao;
import coursesshop.dao.CourseDao;
import coursesshop.dao.UserDao;
import coursesshop.model.Cart;
import coursesshop.model.Category;
import coursesshop.model.Course;
import coursesshop.model.User;
import coursesshop.model.enums.AttendanceMode;
import java.sql.SQLException;
import java.util.List;

/**
 * Business service exposing course browsing features for the application.
 *
 * <p>This service is used by the UI layer to retrieve courses and reference data
 * (categories, attendance modes).
 * </p>
 */
public class CourseBuyingService {

  /**
   * Retrieves all courses.
   *
   * @return a list of all available {@link Course} objects (possibly empty, never {@code null})
   */
  public List<Course> listCourses() {
    // DAO call: fetch all courses from the database
    CourseDao courseDao = new CourseDao();
    return courseDao.findAll();
  }


  public Course findCourseById(int courseId){
    CourseDao courseDao = new CourseDao();
    return courseDao.findById(courseId);
  }
  /**
   * Retrieves all available course categories.
   *
   * @return a list of {@link Category} objects (possibly empty, never {@code null})
   */
  public List<Category> listCategories() {
    // DAO call: fetch all categories from the database
    CategoryDao categoryDao = new CategoryDao();
    return categoryDao.findAll();
  }

  /**
   * Retrieves courses belonging to a specific category.
   *
   * @param categoryId the category identifier (PK of {@code category.id})
   * @return a list of {@link Course} objects in the selected category (possibly empty, never
   * {@code null})
   */
  public List<Course> listCoursesByCategory(int categoryId) {
    // DAO call: fetch courses by category id
    CourseDao courseDao = new CourseDao();
    return courseDao.findByCategory(categoryId);
  }

  /**
   * Retrieves courses matching a keyword in the course name or description.
   *
   * @param keyword the search keyword
   * @return a list of matching {@link Course} objects (possibly empty, never {@code null})
   */
  public List<Course> listCoursesByKeyword(String keyword) {
    // DAO call: fetch courses by keyword (LIKE query)
    CourseDao courseDao = new CourseDao();
    return courseDao.findByKeyword(keyword);
  }

  /**
   * Retrieves all available attendance modes.
   *
   * <p>Attendance modes are stored in the {@code attendance_mode} reference table and mapped
   * to {@link AttendanceMode} enum constants.
   * </p>
   *
   * @return a list of {@link AttendanceMode} values (possibly empty, never {@code null})
   */
  public List<AttendanceMode> listAttendanceModes() {
    // DAO call: fetch all attendance modes from reference table
    AttendanceModeDao attendanceModeDao = new AttendanceModeDao();
    return attendanceModeDao.listAttendanceModeCodes();
  }

  /**
   * Retrieves courses filtered by attendance mode.
   *
   * @param attendanceMode the desired attendance mode (e.g., {@link AttendanceMode#ONSITE} or
   *                       {@link AttendanceMode#REMOTE})
   * @return a list of {@link Course} objects matching the mode (possibly empty, never {@code null})
   */
  public List<Course> listCoursesByAttendance(AttendanceMode attendanceMode) {
    // DAO call: fetch courses by attendance mode
    CourseDao courseDao = new CourseDao();
    return courseDao.findByAttendance(attendanceMode);
  }

  public User register(String firstName, String lastName, String login, String password) {
    UserDao userdao = new UserDao();
    return userdao.register(firstName, lastName, login, password);
  }

  /**
   * Login a User
   * @param login the user login
   * @param password the user password
   * @return a {@link User} object
   */
  public User login(String login, String password){
    UserDao userdao = new UserDao();
    return userdao.login(login, password);
  }



  public Cart createCart(int userId) throws SQLException {
    CartDao cartDao = new CartDao();
    return cartDao.createCart(userId);
  }

  public Cart addToCart(int choice, int id, int i) throws SQLException {
    CartDao cartDao = new CartDao();
    return cartDao.addToCart(choice,id,i);
  }

  public Cart getCartByUserId(int id) {
    CartDao cartDao = new CartDao();
    return cartDao.getCartByUserId(id);
  }
}
