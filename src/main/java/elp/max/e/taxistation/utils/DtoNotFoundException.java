package elp.max.e.taxistation.utils;

public class DtoNotFoundException extends Exception {

    public DtoNotFoundException(String message) {
        super(message + " не найден");
    }
}
