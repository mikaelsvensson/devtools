package info.mikaelsvensson.docutil.shared.commenttext;

public class InlineTagHandlerException extends Exception {
    public InlineTagHandlerException() {
    }

    public InlineTagHandlerException(final Throwable cause) {
        super(cause);
    }

    public InlineTagHandlerException(final String message) {
        super(message);
    }

    public InlineTagHandlerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
