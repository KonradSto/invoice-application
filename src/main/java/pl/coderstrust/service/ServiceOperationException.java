package pl.coderstrust.service;

class ServiceOperationException extends Exception {

    ServiceOperationException(String message) {
        super(message);
    }

    ServiceOperationException() {
    }

    ServiceOperationException(Throwable cause) {
        super(cause);
    }

    ServiceOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
