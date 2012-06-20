package se.linkon.sabine.docutil.shared;

public class DocumentCreatorException extends Exception {
    public DocumentCreatorException() {
    }

    public DocumentCreatorException(String message) {
        super(message);
    }

    public DocumentCreatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentCreatorException(Throwable cause) {
        super(cause);
    }
}
