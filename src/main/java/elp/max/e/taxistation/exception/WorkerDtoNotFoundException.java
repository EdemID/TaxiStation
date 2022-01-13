package elp.max.e.taxistation.exception;

public class WorkerDtoNotFoundException extends RuntimeException {

    public WorkerDtoNotFoundException(String message) {
        super(message + " не работают!");
    }
}
