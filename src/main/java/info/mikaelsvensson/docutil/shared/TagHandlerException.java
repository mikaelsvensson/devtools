package info.mikaelsvensson.docutil.shared;

public class TagHandlerException extends Exception {
    public TagHandlerException() {
    }

    public TagHandlerException(final Throwable cause) {
        super(cause);
    }

    public TagHandlerException(final String message) {
        super(message);
    }

    public TagHandlerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
