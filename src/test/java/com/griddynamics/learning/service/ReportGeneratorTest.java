package com.griddynamics.learning.service;

import main.java.model.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.HashMap;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;


public class ReportGeneratorTest {
     ReportGenerator generateReport= new ReportGenerator();



    @Test
    @DisplayName("returns empty string when student is null")
    void returnsEmptyStringWhenStudentIsNull() {
        String result = generateReport.generate(null);
        Assertions.assertEquals("", result);
    }

    @Test
    @DisplayName("includes name and curriculum when present and handles nulls")
    void includesNameAndCurriculumWhenPresentAndHandlesNulls() {
        HashMap<String, Integer> courses = new HashMap<>();
        courses.put("A", 1);
        Student student = new Student(null, null, null, courses);
        String result = generateReport.generate(student);
        Assertions.assertTrue(result.startsWith(" ("), "expected empty name and curriculum placeholders");
        Assertions.assertTrue(result.contains("Total course time: 1 hours") || result.contains("Total course time: 1 hour"));
    }

    @Test
    @DisplayName("reports unknown start date when start date is null")
    void reportsUnknownStartDateWhenStartDateIsNull() {
        HashMap<String, Integer> courses = new HashMap<>();
        courses.put("Course1", 5);
        Student student = new Student("Alice", "Java", null, courses);
        String result = generateReport.generate(student);
        Assertions.assertTrue(result.contains("Total course time: 5 hours. Start date: unknown."));
    }

    @Test
    @DisplayName("reports training not finished when total exceeds elapsed hours")
    void reportsTrainingNotFinishedWhenTotalExceedsElapsedHours() {
        HashMap<String, Integer> courses = new HashMap<>();
        courses.put("Course1", 48);
        Date start = Date.from(Instant.now().minus(10, ChronoUnit.HOURS));
        Student student = new Student("Bob", "Dev", start, courses);
        String result = generateReport.generate(student);
        Assertions.assertTrue(result.contains("Training not finished."));
        Assertions.assertTrue(result.contains("left"));
    }

    @Test
    @DisplayName("reports training completed when elapsed exceeds total")
    void reportsTrainingCompletedWhenElapsedExceedsTotal() {
        HashMap<String, Integer> courses = new HashMap<>();
        courses.put("Course1", 10);
        Date start = Date.from(Instant.now().minus(70, ChronoUnit.HOURS));
        Student student = new Student("Eve", "Sec", start, courses);
        String result = generateReport.generate(student);
        Assertions.assertTrue(result.contains("Training completed."));
        Assertions.assertTrue(result.contains("ago."));
    }

    @Test
    @DisplayName("reports zero total when courses null or empty")
    void reportsZeroTotalWhenCoursesNullOrEmpty() {
        Student studentNullCourses = new Student("Sam", "CS", new Date(), null);
        Student studentEmptyCourses = new Student("Sam", "CS", new Date(), new HashMap<>());

        String r1 = generateReport.generate(studentNullCourses);
        String r2 = generateReport.generate(studentEmptyCourses);

        Assertions.assertTrue(r1.contains("Total course time: 0 hours"));
        Assertions.assertTrue(r2.contains("Total course time: 0 hours"));
    }

    @ParameterizedTest
    @MethodSource("relativeStartTimestamps")
    @DisplayName("generateReport handles different start times relative to now")
    void parameterizedStartTimesFromMethodSource(String isoTimestamp) {
        Date startDate = Date.from(Instant.parse(isoTimestamp));
        HashMap<String, Integer> courses = new HashMap<>();
        courses.put("Course1", 16);
        Student student = new Student("Test", "Test", startDate, courses);

        String result = generateReport.generate(student);
        System.out.println(result);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty(), "generateReport returned an empty string for start: " + isoTimestamp);
        Assertions.assertTrue(result.contains(" - "), "expected generated report to contain ' - ' separator");
    }

    static Stream<String> relativeStartTimestamps() {
        Instant now = Instant.now();
        return Stream.of(
                now.minus(9, ChronoUnit.DAYS).minus(3, ChronoUnit.HOURS).toString(),
                now.minus(5, ChronoUnit.DAYS).toString(),
                now.minus(1, ChronoUnit.DAYS).minus(2, ChronoUnit.HOURS).toString(),
                now.minus(23, ChronoUnit.HOURS).toString(),
                now.toString()
        );
    }
}


