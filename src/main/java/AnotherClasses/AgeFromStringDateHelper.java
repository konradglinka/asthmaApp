package AnotherClasses;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AgeFromStringDateHelper {
    public static int getAgeFromStringDate(String inputDate){ //Actual date - date from past
        return (int)ChronoUnit.YEARS.between(LocalDate.parse(inputDate,DateTimeFormatter.ofPattern("dd-MM-yyyy")),LocalDate.now());
    }
}
