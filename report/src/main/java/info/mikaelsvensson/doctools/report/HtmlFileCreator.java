package info.mikaelsvensson.doctools.report;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkEventAttributeSet;

import java.io.File;

public class HtmlFileCreator {
    private Sink sink;
    private File file;

    public HtmlFileCreator(final Sink sink, final String documentTitle, final File file) {
        this.sink = sink;
        this.file = file;
        init(documentTitle);
    }

    private void init(final String documentTitle) {
        sink.head();
        sink.title();
        sink.text(documentTitle);
        sink.title_();
        sink.head_();
        sink.body();
    }

    public void printHeading1(final String text) {
        sink.sectionTitle1();
        sink.text(text);
        sink.sectionTitle1_();
    }

    public void printHeading2(final String text) {
        sink.sectionTitle2();
        sink.text(text);
        sink.sectionTitle2_();
    }

    public void printParagraph(final String text) {
        sink.paragraph();
        sink.text(text);
        sink.paragraph();
    }

    public void printText(final String text) {
        sink.text(text);
    }

    public void printListItemLink(final String text, final String url) {
        sink.listItem();
        sink.link(url);
        sink.text(text);
        sink.link_();
        sink.listItem_();
    }

    public void listStart() {
        listStart(null);
    }
    public void listStart(final String cssClass) {
        sink.list(createAttributeSet(cssClass));
    }

    private SinkEventAttributeSet createAttributeSet(final String cssClass) {
        SinkEventAttributeSet set = new SinkEventAttributeSet();
        if (StringUtils.isNotEmpty(cssClass)) {
            set.addAttribute(SinkEventAttributeSet.CLASS, cssClass);
        }
        return set;
    }

    public void listEnd() {
        sink.list_();
    }

    public void close() {
        sink.body_();
        sink.flush();
        sink.close();
    }

    public void listItemStart() {
        sink.listItem();
    }

    public void listItemEnd() {
        sink.listItem_();
    }

    public void printRaw(final String content) {
        // TODO: rawText is deprecated (see http://maven.apache.org/doxia/developers/sink.html#Avoid_sink.rawText)
        sink.rawText(content);
    }

    public File getFile() {
        return file;
    }
}
