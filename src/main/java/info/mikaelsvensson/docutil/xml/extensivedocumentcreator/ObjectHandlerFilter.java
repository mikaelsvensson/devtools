package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

public interface ObjectHandlerFilter<T extends Object> {
// -------------------------- OTHER METHODS --------------------------

    boolean accept(T object);
}
