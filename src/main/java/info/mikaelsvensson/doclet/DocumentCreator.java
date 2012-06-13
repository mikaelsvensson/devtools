package info.mikaelsvensson.doclet;

import com.sun.javadoc.RootDoc;
import org.w3c.dom.Document;

public interface DocumentCreator {
    Document generateDocument(RootDoc doc);
}
