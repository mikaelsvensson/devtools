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

package info.mikaelsvensson.doctools.shared.commenttext;

import com.sun.javadoc.Doc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.text.MessageFormat;
import java.util.Iterator;

public class SourceFileInlineTagHandler extends AbstractInlineTagHandler {

    public static final String TAG_NAME = "embed";

    private enum FileType {
        CLASS {
            @Override
            String getFile(final File sourceFolder, final String fileExpression) throws IOException {
                File sourceFile = new File(sourceFolder, fileExpression.replace('.', File.separatorChar) + ".java");
                return MessageFormat.format("</p><pre class=\"{0}\"><![CDATA[{1}]]></pre><p>", getStyleSheetClassByFileType(sourceFile), getFileContent(sourceFile));
            }
        },
        XML {
            @Override
            String getFile(File sourceFolder, String fileExpression) throws IOException, InlineTagHandlerException {
                return getXMLString(sourceFolder, fileExpression, true);
            }
        },
        XML_NO_NAMESPACE {
            @Override
            String getFile(File sourceFolder, String fileExpression) throws IOException, InlineTagHandlerException {
                return getXMLString(sourceFolder, fileExpression, false);
            }
        },
        FILE {
            @Override
            String getFile(final File sourceFolder, final String fileExpression) throws IOException {
                File sourceFile = new File(sourceFolder.getParentFile(), fileExpression.replace('/', File.separatorChar));
                return MessageFormat.format("</p><pre class=\"{0}\"><![CDATA[{1}]]></pre><p>", getStyleSheetClassByFileType(sourceFile), getFileContent(sourceFile));
            }
        },
        INCLUDE {
            @Override
            String getFile(final File sourceFolder, final String fileExpression) throws IOException {
                File sourceFile = new File(sourceFolder.getParentFile(), fileExpression.replace('/', File.separatorChar));
                return getFileContent(sourceFile);
            }
        };

        //TODO Refactor method into multiple methods
        //TODO Improve error messages when using invalid XPath expressions or expressions which does not return any nodes
        private static String getXMLString(File sourceFolder,
                                           String fileExpression,
                                           boolean isNamespaceAware) throws IOException,
                InlineTagHandlerException {
            int spacePos = fileExpression.indexOf(' ');
            String filePath = spacePos != -1 ? fileExpression.substring(0, spacePos) : fileExpression;
            String xpathExpr = spacePos != -1 ? fileExpression.substring(spacePos + 1) : "";

            filePath = filePath.replace('/', File.separatorChar);

            File sourceFile = new File(sourceFolder.getParentFile(), filePath);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(isNamespaceAware);

            try {
                DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                final Document document = documentBuilder.parse(sourceFile);
                final String namespaceURI = document.getDocumentElement().getNamespaceURI();

                Node resultNode = null;
                if (xpathExpr.length() > 0) {
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    if (isNamespaceAware) {
                        xPath.setNamespaceContext(new NamespaceContext() {

                            @Override
                            public String getNamespaceURI(String prefix) {
                                System.err.println("getNamespaceURI '" + prefix +"'");
                                if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
                                    return document.lookupNamespaceURI(null);
                                } else {
                                    return document.lookupNamespaceURI(prefix);
                                }
                            }

                            @Override
                            public String getPrefix(String namespaceURI) {
                                return null;
//                                return "x";
                            }

                            @Override
                            public Iterator getPrefixes(String namespaceURI) {
                                return null;
//                                return Collections.singletonList("x").iterator();
                            }
                        });
                    }
                    XPathExpression expression = xPath.compile(xpathExpr);
                    Object result = expression.evaluate(document, XPathConstants.NODESET);
                    System.out.println("Using XPath expression " + xpathExpr + " resulted in " +
                            "a " + result);
                    if (result instanceof NodeList) {
                        NodeList nodes = (NodeList) result;
                        if (nodes.getLength() > 0) {
                            resultNode = nodes.item(0);
                        }
                    }
                    if (null == resultNode) {
                        throw new InlineTagHandlerException("XPath expression '" + xpathExpr
                                + "' yielded no nodes. You might want to double-check if you " +
                                "use the correct namespaces in the XPath expression.");
                    }
                } else {
                    resultNode = document;
                }

                StringWriter sw = new StringWriter();
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
//                    transformerFactory.setAttribute("indent-number", new Integer(4));

                System.out.println("Result node = " + resultNode);

                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(4));

                transformer.transform(new DOMSource(resultNode), new StreamResult(sw));

                String s = sw.toString();
                return MessageFormat.format("</p><pre class=\"{0}\"><![CDATA[{1}]]></pre><p>", getStyleSheetClassByFileType(sourceFile), s);

            } catch (XPathExpressionException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (SAXException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (TransformerException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        private static String getStyleSheetClassByFileType(File file) {
            int pos = file.getName().lastIndexOf('.');
            if (pos > -1) {
                String ext = file.getName().toLowerCase().substring(pos + 1);
                return "embedded-" + ext + "-file";
            }
            return "";
        }

        abstract String getFile(final File sourceFolder, String fileExpression) throws IOException, InlineTagHandlerException;

        private static String getFileContent(final File file) throws IOException {
            if (file.exists() && file.isFile()) {
                FileReader reader = new FileReader(file);
                int size = (int) file.length();
                char[] content = new char[size];
                reader.read(content, 0, size);
                return new String(content);
            } else {
                throw new FileNotFoundException(file.getAbsolutePath());
            }
        }
    }

    public SourceFileInlineTagHandler() {
        super(TAG_NAME);
    }

    @Override
    public String toString(final Tag tag) throws InlineTagHandlerException {
        String text = tag.text();
        try {
            File sourceFolder = null;
            Doc holder = tag.holder();
            if (holder instanceof PackageDoc) {
                sourceFolder = getSourceFolder((PackageDoc) holder, tag.position().file());
            } else if (holder instanceof ProgramElementDoc) {
                sourceFolder = getSourceFolder(((ProgramElementDoc) holder).containingPackage(), tag.position().file());
            } else {
                throw new InlineTagHandlerException("Cannot use source tag in documentation for " + tag.holder().name() + ".");
            }
            int pos = text.indexOf(' ');
            if (pos > -1) {
                String type = text.substring(0, pos).trim();
                String expr = text.substring(pos + 1).trim();
                return FileType.valueOf(type.toUpperCase()).getFile(sourceFolder, expr);
            } else {
                throw new InlineTagHandlerException(TAG_NAME + " must have two parameters, resource type and a resource specifier.");
            }
        } catch (IOException e) {
            throw new InlineTagHandlerException(e);
        } catch (IllegalArgumentException e) {
            throw new InlineTagHandlerException(e);
        }
    }

    private File getSourceFolder(PackageDoc currentClassPackage, final File currentSourceFile) {
        File sourceFolder = currentSourceFile.getParentFile();
        if (!"".equals(currentClassPackage)) {
            int i = -1;
            do {
                sourceFolder = sourceFolder.getParentFile();
            } while ((i = currentClassPackage.name().indexOf('.', i + 1)) != -1);
        }
        return sourceFolder;
    }

}
