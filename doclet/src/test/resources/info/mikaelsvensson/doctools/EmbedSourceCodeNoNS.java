package info.mikaelsvensson.doctools;

/**
 * The {@code @embed} tag can be used to include XML documents into the documentation.
 *
 * Like this:
 *
 * {@embed xml resources/info/mikaelsvensson/doctools/EmbedSourceCodeNoNS.data.xml}
 *
 * As an added bonus it is possible to only include a part of the XML document by specifying an XPath expression after
 * the file name:
 *
 * {@embed xml resources/info/mikaelsvensson/doctools/EmbedSourceCodeNoNS.data.xml //member[role/text() = 'father']}
 */
public class EmbedSourceCodeNoNS {
}
