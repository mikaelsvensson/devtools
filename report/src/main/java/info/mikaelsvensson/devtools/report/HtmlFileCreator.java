/*
 * Copyright 2014 Mikael Svensson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package info.mikaelsvensson.devtools.report;

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
        printText(documentTitle);
        sink.title_();
        sink.head_();
        sink.body();
    }

    public void printSectionStart(final int level, final String text) {
        sink.section(level, null);
        sink.sectionTitle(level, null);
        printText(text);
        sink.sectionTitle_(level);
    }

    public void printSectionEnd(final int level) {
        sink.section_(level);
    }

    public void printParagraph(final String text) {
        sink.paragraph();
        printText(text);
        sink.paragraph_();
    }

    public void printText(final String text) {
        sink.text(text);
    }

    public void printLinkListItem(final String text, final String url) {
        sink.listItem();
        printLink(text, url);
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

    public void printLinkParagraph(final String text, final String url) {
        sink.paragraph();
        printLink(text, url);
        sink.paragraph_();
    }

    private void printLink(final String text, final String url) {
        sink.link(url);
        printText(text);
        sink.link_();
    }
}
