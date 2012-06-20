package se.linkon.sabine.docutil.shared;

import com.sun.javadoc.RootDoc;
import org.w3c.dom.Document;
import se.linkon.sabine.docutil.shared.propertyset.PropertySet;

public interface DocumentCreator {
    Document generateDocument(RootDoc doc, final PropertySet properties) throws DocumentCreatorException;
}
