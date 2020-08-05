package edu.tacoma.uw.csquizzer.model;
/**
 * The Course class
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class Course {
    private int CourseId;
    private String CourseName;

    public Course(int courseId, String courseName) {
        CourseId = courseId;
        CourseName = courseName;
    }

    public int getCourseId() {
        return CourseId;
    }

    public void setCourseId(int courseId) {
        CourseId = courseId;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }
}
