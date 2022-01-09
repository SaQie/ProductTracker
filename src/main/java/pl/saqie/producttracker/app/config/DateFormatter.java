package pl.saqie.producttracker.app.config;

import java.time.format.DateTimeFormatter;

public class DateFormatter {

    public static DateTimeFormatter formatDate(){
        return DateTimeFormatter.ofPattern("HH:mm | MM-dd");
    }
}
