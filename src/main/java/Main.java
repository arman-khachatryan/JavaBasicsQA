import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Integer> courses = new HashMap<>();
        courses.put("Math", 16);
        courses.put("Java", 24);

        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.DECEMBER, 17, 12, 30, 0);
        Date start = cal.getTime();

        Student s = new Student("Alice", "CS", start, courses);
        System.out.println(s.GenerateReport(s));
    }
}
