package elp.max.e.taxistation.exception;

public class CallBeforeCompletionOfOrderException extends RuntimeException{

    public CallBeforeCompletionOfOrderException(String message) {
        super("Вызов такси до завршения заказа невозможен. Ваш заказ: " + message);
    }
}
