package elp.max.e.taxistation.utils;

public class Utils {

    public static String randomizer() {
        int a = 1000000; // Начальное значение диапазона - "от"
        int b = 9000000; // Конечное значение диапазона - "до"

        int randomNumber = a + (int) (Math.random() * b);
        return String.valueOf(randomNumber);
    }
}
