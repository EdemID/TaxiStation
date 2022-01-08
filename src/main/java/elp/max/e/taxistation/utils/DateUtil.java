package elp.max.e.taxistation.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DateUtil {

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Date convertFromStringToLocalDateTimeViaInstant(String[] hoursAndMinutes) {
        List<Integer>  hoursAndMinutesList = Arrays.stream(hoursAndMinutes)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        //  работает только для текущего дня. например, если сейчас 8.01.22 23:55
        //  и задать время 08:00, то день так и останется 8.01.22
        LocalDateTime dateTime = LocalDateTime.now()
                .withHour(hoursAndMinutesList.get(0))
                .withMinute(hoursAndMinutesList.get(1))
                .withSecond(0)
                .withNano(0);

        return convertToDateViaInstant(dateTime);
    }

    public static String convertFromLocalDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.getHour() +
               ":" +
               localDateTime.getMinute();
    }
}
