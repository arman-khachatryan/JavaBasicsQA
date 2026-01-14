package com.griddynamics.learning;

import com.griddynamics.learning.service.ReportGenerator;
import com.griddynamics.learning.model.Student;

import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.util.Map;


public class Main {

    public static void main(String[] args) {
        ReportGenerator reportGenerator = new ReportGenerator();
        Map<String, Integer> courses = new HashMap<>();
        courses.put("Math", 16);
        courses.put("Java", 24);

        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.DECEMBER, 17, 12, 30, 0);
        Date start = cal.getTime();

        Student s = new Student("Alice", "CS", start, courses);
        System.out.println(reportGenerator.generate(s));
    }
}
