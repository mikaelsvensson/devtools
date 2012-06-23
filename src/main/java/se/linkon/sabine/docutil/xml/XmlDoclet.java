/*
 * Copyright (c) 2012, Mikael Svensson
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the copyright holder nor the names of the
 *       contributors of this software may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MIKAEL SVENSSON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package se.linkon.sabine.docutil.xml;

import com.sun.javadoc.RootDoc;
import org.w3c.dom.Document;
import se.linkon.sabine.docutil.AbstractDoclet;
import se.linkon.sabine.docutil.shared.DocumentCreator;
import se.linkon.sabine.docutil.shared.DocumentCreatorException;
import se.linkon.sabine.docutil.shared.DocumentCreatorFactory;
import se.linkon.sabine.docutil.shared.FileUtil;
import se.linkon.sabine.docutil.shared.propertyset.PropertySet;
import se.linkon.sabine.docutil.shared.propertyset.PropertySetException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class XmlDoclet extends AbstractDoclet {

    private XmlDocletOptions options;

    protected XmlDoclet(RootDoc root, XmlDocletOptions options) {
        super(root);
        this.options = options;
    }

    public static boolean start(RootDoc root) throws PropertySetException {
        return new XmlDoclet(root, new XmlDocletOptions(new PropertySet(root.options()))).generate();
    }

    public boolean generate() {
        for (Map.Entry<String, XmlDocletAction> entry : options.getActions().entrySet()) {
            try {
                XmlDocletAction action = entry.getValue();

                root.printNotice("Building XML document.");
                DocumentCreator documentCreator = DocumentCreatorFactory.getDocumentCreator(action.format);
                Document document = documentCreator.generateDocument(root, action.getParameters());
                root.printNotice("Finished building XML document.");

                generate(document, action.getOutput(), action.getTransformer(), action.getParameters().getProperties());

                postProcess(action);
            } catch (IOException e) {
                printError(new DocumentCreatorException("Could not post-process action " + entry.getKey() + ".", e));
            } catch (DocumentCreatorException e) {
                printError(e);
            }
        }
        return true;
    }

    //TODO: MISV 20120618 Refactor postProcess into the same kind of "factory mechanism" used for DocumentCreator. Perhaps call it PostProcessor?
    private void postProcess(XmlDocletAction action) throws IOException {
        if (action.getPostProcessor() != null && action.getPostProcessor().length() > 0) {
            String folderExpr = action.getPostProcessingParameters().get("folder");
            String templateFileExpr = action.getPostProcessingParameters().get("templateFile");
            String filePatternExpr = action.getPostProcessingParameters().get("filePattern");
            String replaceStringExpr = action.getPostProcessingParameters().get("replaceString");

            System.out.println(action.getPostProcessingParameters());

            File folder = new File(folderExpr);
            Pattern filePattern = Pattern.compile(filePatternExpr);
            File templateFile = new File(templateFileExpr);

            String template = FileUtil.getFileContent(templateFile);

            List<File> files = FileUtil.findFiles(folder, filePattern);
            for (File file : files) {
                System.out.println("Look for " + replaceStringExpr + " in " + templateFile.getName() + " and apply it to " + file.getName());

                String source = FileUtil.getFileContent(file);
                String result = template.replace(replaceStringExpr, source);
                FileWriter writer = new FileWriter(file);
                writer.write(result);
                writer.close();

                System.out.println(file.getAbsolutePath());
            }
        }
    }

    public void generate(Document doc, File outputFile, File xsltFile, Map<String, String> parameters) {
        try {
            if (xsltFile != null) {
                root.printNotice("Transforming XML document using XSLT");
                writeFile(doc, outputFile, xsltFile, parameters);
                root.printNotice("Transformed XML document saved as " + outputFile.getAbsolutePath());
            } else {
                root.printNotice("Saving XML document");
                writeFile(doc, outputFile);
                root.printNotice("XML document saved as " + outputFile.getAbsolutePath());
            }
        } catch (TransformerException e) {
            root.printError(e.getMessage());
        }
    }

    public static void writeFile(Document doc, File file, File xsltFile, Map<String, String> parameters) throws TransformerException {
        Source xsltSource = new StreamSource(xsltFile);
        writeFile(doc, file, xsltSource, parameters);
    }

    private static void writeFile(Document doc, File file, Source xsltSource, Map<String, String> parameters) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltSource);
        StreamResult outputTarget = new StreamResult(file);

        Source xmlSource = new DOMSource(doc);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            transformer.setParameter(entry.getKey(), entry.getValue());
        }

        transformer.setParameter("outputFile", file.getName());
        transformer.transform(xmlSource, outputTarget);
    }

    public static void writeFile(Document doc, File file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(file));
        System.out.println(file.getAbsolutePath());
    }

    public static int optionLength(String option) {
        int len = XmlDocletOptions.optionLength(option);
        return len;
//        return len > 0 ? len : Standard.optionLength(option);
    }

    public static void main(String[] args) {
/*
        com.sun.tools.javadoc.Main.execute(
                "javadoc",
                XmlDoclet.class.getName(),
                new String[]{
                        XmlDocletOptions.PARAMETER_FORMAT,
                        XmlDocletAction.Format.STANDARD.name(),
                        XmlDocletOptions.PARAMETER_OUTPUT,
                        "out.xml",
                        "D:\\Dokument\\Utveckling\\doclet\\src\\test\\java\\info\\mikaelsvensson\\doclet\\XmlDocletTest.java"
                });
*/
/*
        com.sun.tools.javadoc.Main.execute(
                "javadoc",
                XmlDoclet.class.getName(),
                new String[]{
                        XmlDocletOptions.PARAMETER_FORMAT,
                        XmlDocletAction.FORMAT_STANDARD,
//                        ElementsOnlyDocumentCreator.class.getSimpleName(),
                        XmlDocletOptions.PARAMETER_OUTPUT,
                        "index2.html",
                        XmlDocletOptions.PARAMETER_TRANSFORMER,
                        "D:\\Dokument\\Utveckling\\doclet\\src\\test\\resources\\multiple-files.xslt",
                        "-sourcepath",
                        "D:\\Dokument\\Utveckling\\doclet\\src\\test\\java",
//                        "D:\\Dokument\\Utveckling\\doclet\\src\\main\\java",
                        "-subpackages",
                        "se.linkon.sabine.doclet"
                });
*/
    }
}
