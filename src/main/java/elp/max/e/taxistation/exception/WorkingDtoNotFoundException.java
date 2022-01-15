package elp.max.e.taxistation.exception;

public class WorkingDtoNotFoundException extends RuntimeException {

    public WorkingDtoNotFoundException(String message) {
        super(message + " не работают!");
    }
}
