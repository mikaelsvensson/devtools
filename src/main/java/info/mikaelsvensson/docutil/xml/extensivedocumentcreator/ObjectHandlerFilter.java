package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

public interface ObjectHandlerFilter<T extends Object> {
    boolean accept(T object);
}
