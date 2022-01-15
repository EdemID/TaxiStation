package elp.max.e.taxistation.exception;

public class CallBackException extends RuntimeException{

    public CallBackException(String message) {
        super("Повторный вызов такси невозможен до завершения заказа. Ваш заказ: " + message);
    }
}
