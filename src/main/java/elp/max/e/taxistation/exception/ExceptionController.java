package elp.max.e.taxistation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = {
            WorkerDtoNotFoundException.class,
            EntityNotFoundException.class,
            ValidationDtoException.class
    })
    public ResponseEntity<Response> handleException(RuntimeException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    static class Response {

        private String message;

        public Response() {
        }

        public Response(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }
}
