package edu.tacoma.uw.csquizzer.model;

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
