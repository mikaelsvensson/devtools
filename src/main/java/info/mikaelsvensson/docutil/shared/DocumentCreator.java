package info.mikaelsvensson.docutil.shared;

import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.docutil.shared.propertyset.PropertySet;
import org.w3c.dom.Document;

public interface DocumentCreator {
    Document generateDocument(RootDoc doc, final PropertySet properties) throws DocumentCreatorException;
}
