package com.griddynamics.learning.main.java.service;

import model.Student;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

public class Generator {

    public String generateReport(Student student) {
        if (student == null) return "";
        String name = student.getName() == null ? "" : student.getName();
        String curriculum = student.getCurriculum() == null ? "" : student.getCurriculum();
        return name + " (" + curriculum + ") - " + generateReportHelper(student);
    }

    private static String generateReportHelper(Student student) {
        if (student == null) return "Total course time: 0 hours" ;

        Map<String, Integer> courses = student.getCourses();
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
