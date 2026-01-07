import java.util.Date;
import java.util.HashMap;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.DayOfWeek;

public class Student {
    private String name;
    private String Curriculum;
    private Date StartDate;
    private HashMap<String, Integer> Courses;

    public Student(String name, String Curriculum, Date StartDate, HashMap<String, Integer> Courses) {
        this.name = name;
        this.Curriculum = Curriculum;
        this.StartDate = StartDate;
        this.Courses = Courses;
    }

    public String getName() {
        return name;
    }

    public String getCurriculum() {
        return Curriculum;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public HashMap<String, Integer> getCourses() {
        return Courses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurriculum(String Curriculum) {
        this.Curriculum = Curriculum;
    }

    public void setStartDate(Date StartDate) {
        this.StartDate = StartDate;
    }

    public void setCourses(HashMap<String, Integer> Courses) {
        this.Courses = Courses;
    }

    public void addCourse(String courseName, int duration) {
        this.Courses.put(courseName, duration);
    }

    public String GenerateReport(Student student) {
        if (student == null) return "";
        String name = student.getName() == null ? "" : student.getName();
        String curriculum = student.getCurriculum() == null ? "" : student.getCurriculum();
        return name + " (" + curriculum + ") - " + GenerateReportHelper(student);
    }

    private String GenerateReportHelper(Student student) {
        if (student == null) return "Total course time: 0 hours" ;

        HashMap<String, Integer> courses = student.getCourses();
        if (courses == null || courses.isEmpty()) return "Total course time: 0 hours";

        int total = 0;
        for (Integer duration : courses.values()) {
            if (duration != null) total += duration;
        }

        Date startDate = student.getStartDate();
        if (startDate == null) {
            return "Total course time: " + total + " hours. Start date: unknown.";
        }

        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime startZdt = ZonedDateTime.ofInstant(startDate.toInstant(), zone);
        ZonedDateTime nowZdt = ZonedDateTime.now(zone);
        long hoursElapsed = 0;
        if (!nowZdt.isBefore(startZdt)) {
            LocalDate cur = startZdt.toLocalDate();
            LocalDate end = nowZdt.toLocalDate();
            for (LocalDate d = cur; !d.isAfter(end); d = d.plusDays(1)) {
                DayOfWeek dow = d.getDayOfWeek();
                if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) continue;

                ZonedDateTime dayStart = d.atStartOfDay(zone);
                ZonedDateTime dayEnd = d.plusDays(1).atStartOfDay(zone);

                ZonedDateTime intervalStart = startZdt.isAfter(dayStart) && startZdt.toLocalDate().equals(d) ? startZdt : dayStart;
                ZonedDateTime intervalEnd = nowZdt.isBefore(dayEnd) && nowZdt.toLocalDate().equals(d) ? nowZdt : dayEnd;

                long minutes = ChronoUnit.MINUTES.between(intervalStart, intervalEnd);
                if (minutes <= 0) continue;
                long hrs = minutes / 60;
                if (hrs > 8) hrs = 8;
                hoursElapsed += hrs;
            }
        }
        long hourDifference = hoursElapsed - total;
        long absHours = Math.abs(hourDifference);
        long days = absHours / 24;
        long hours = absHours % 24;
        String formatted = (days > 0 ? days + (days == 1 ? " day" : " days" ) : "") + (days>0 && hours>0 ? " and ": "") + (hours > 0 ? hours + (hours == 1 ? " hour" : " hours" ) : "");

        if (hourDifference < 0) {
            return "Training not finished. " + formatted  + " left";
        } else {
            return "Training completed. " + formatted + " ago.";
        }

    }
}
