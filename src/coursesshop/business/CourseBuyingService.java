package coursesshop.business;

import coursesshop.dao.AttendanceModeDao;
import coursesshop.dao.CategoryDao;
import coursesshop.dao.CourseDao;
import coursesshop.model.Category;
import coursesshop.model.Course;
import coursesshop.model.enums.AttendanceMode;

import java.util.List;

public class CourseBuyingService {
    public List<Course> listCourses(){
        CourseDao coursDao = new CourseDao();
        return coursDao.findAll();
    }

    public List<Category> listCategories(){
        CategoryDao categoryDao = new CategoryDao();
        return categoryDao.findAll();
    }

    public List<Course> listCoursesByCategory(int categoryId) {
        CourseDao courseDao = new CourseDao();
        return courseDao.findByCategory(categoryId);
    }

    public List<Course> listCoursesByKeyword(String keyword) {
        CourseDao courseDao = new CourseDao();
        return courseDao.findByKeyword(keyword);
    }

    public List<AttendanceMode> listAttendanceModes() {
        AttendanceModeDao attendanceModeDao = new AttendanceModeDao();
        return attendanceModeDao.listAttendanceModeCodes();
    }

    public List<Course> listCoursesByAttendance(AttendanceMode attendanceMode) {
        CourseDao courseDao = new CourseDao();
        return courseDao.findByAttendance(attendanceMode);
    }
}
