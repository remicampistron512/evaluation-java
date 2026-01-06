package coursesshop.business;

import coursesshop.dao.CourseDao;
import coursesshop.model.Course;

import java.util.List;

public class CourseBuyingService {
    public List<Course> listCourses(){
        CourseDao coursDao = new CourseDao();
        return coursDao.findAll();
    }
}
