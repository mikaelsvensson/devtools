package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.Doc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;
import info.mikaelsvensson.docutil.shared.TagHandlerException;

public class DocHandler<T extends Doc> extends Handler<T> {
    DocHandler(final Class<T> handledClass) {
        super(handledClass);
    }

    public DocHandler() {
        super((Class<T>) Doc.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        String text = doc.commentText();
        if (null != text && text.length() > 0) {
            try {
                el.addCommentChild(doc);
            } catch (TagHandlerException e) {
                throw new JavadocItemHandlerException("Could not parse/process one of the Javadoc tags. ", e);
            }
        }
        el.setAttribute("name", doc.name());

        handleDocImpl(el, doc.tags(), "tags", "tag");
    }
}
