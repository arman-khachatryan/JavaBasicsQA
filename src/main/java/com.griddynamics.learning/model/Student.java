package com.griddynamics.learning.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Student {
    private String name;
    private String curriculum;
    private Date startDate;
    private Map<String, Integer> courses;

    public Student(String name, String curriculum, Date startDate, Map<String, Integer> courses) {
        this.name = name;
        this.curriculum = curriculum;
        this.startDate = startDate;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }


    public String getCurriculum() {
        return curriculum;
    }

    public Date getStartDate() {
        return startDate;
    }


    public Map<String, Integer> getCourses() {
        return courses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setCourses(Map<String, Integer> courses) {
        this.courses = courses;
    }

    public void addCourse(String courseName, int duration) {
        if (this.courses == null) {
            this.courses = new HashMap<>();
        }
        this.courses.put(courseName, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name) && Objects.equals(curriculum, student.curriculum) && Objects.equals(startDate, student.startDate) && Objects.equals(courses, student.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, curriculum, startDate, courses);
    }
}
