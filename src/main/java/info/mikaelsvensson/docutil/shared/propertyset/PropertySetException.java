package info.mikaelsvensson.docutil.shared.propertyset;

public class PropertySetException extends Exception {
    public PropertySetException() {
    }

    public PropertySetException(final Throwable cause) {
        super(cause);
    }

    public PropertySetException(final String message) {
        super(message);
    }

    public PropertySetException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
