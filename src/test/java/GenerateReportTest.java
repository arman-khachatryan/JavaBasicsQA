import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.HashMap;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import static org.junit.jupiter.api.Assertions.*;

public class GenerateReportTest {

    @Test
    void returnsEmptyString_whenStudentIsNull() {
        Student caller = new Student("caller", "cur", new Date(), new HashMap<>());
        String result = caller.GenerateReport(null);
        assertEquals("", result);
    }

    @Test
    void includesNameAndCurriculum_whenPresentAndHandlesNulls() {
        HashMap<String, Integer> courses = new HashMap<>();
        courses.put("A", 1);
        Student student = new Student(null, null, null, courses);
        Student caller = new Student("caller", "cur", new Date(), new HashMap<>());
        String result = caller.GenerateReport(student);
        assertTrue(result.startsWith(" ("), "expected empty name and curriculum placeholders");
        assertTrue(result.contains("Total course time: 1 hours") || result.contains("Total course time: 1 hour"));
    }

    @Test
    void reportsUnknownStartDate_whenStartDateIsNull() {
        HashMap<String, Integer> courses = new HashMap<>();
        courses.put("Course1", 5);
        Student student = new Student("Alice", "Java", null, courses);
        Student caller = new Student("", "", new Date(), new HashMap<>());
        String result = caller.GenerateReport(student);
        assertTrue(result.contains("Total course time: 5 hours. Start date: unknown."));
    }

    @Test
    void reportsTrainingNotFinished_whenTotalExceedsElapsedHours() {
        HashMap<String, Integer> courses = new HashMap<>();
        courses.put("Course1", 48);
        Date start = Date.from(Instant.now().minus(10, ChronoUnit.HOURS));
        Student student = new Student("Bob", "Dev", start, courses);
        Student caller = new Student("", "", new Date(), new HashMap<>());
        String result = caller.GenerateReport(student);
        assertTrue(result.contains("Training not finished."));
        assertTrue(result.contains("left"));
    }

    @Test
    void reportsTrainingCompleted_whenElapsedExceedsTotal() {
        HashMap<String, Integer> courses = new HashMap<>();
        courses.put("Course1", 10);
        Date start = Date.from(Instant.now().minus(50, ChronoUnit.HOURS));
        Student student = new Student("Eve", "Sec", start, courses);
        Student caller = new Student("", "", new Date(), new HashMap<>());
        String result = caller.GenerateReport(student);
        assertTrue(result.contains("Training completed."));
        assertTrue(result.contains("ago."));
    }

    @Test
    void reportsZeroTotal_whenCoursesNullOrEmpty() {
        Student studentNullCourses = new Student("Sam", "CS", new Date(), null);
        Student studentEmptyCourses = new Student("Sam", "CS", new Date(), new HashMap<>());
        Student caller = new Student("", "", new Date(), new HashMap<>());

        String r1 = caller.GenerateReport(studentNullCourses);
        String r2 = caller.GenerateReport(studentEmptyCourses);

        assertTrue(r1.contains("Total course time: 0 hours"));
        assertTrue(r2.contains("Total course time: 0 hours"));
    }
}