package coursesshop.business;

import coursesshop.dao.CategoryDao;
import coursesshop.dao.CourseDao;
import coursesshop.model.Category;
import coursesshop.model.Course;

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
}
