package info.mikaelsvensson.doclet.db2;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.doclet.db2.metadata.Column;
import info.mikaelsvensson.doclet.db2.metadata.Database;
import info.mikaelsvensson.doclet.db2.metadata.Table;
import info.mikaelsvensson.doclet.db2.parser.CommandType;
import info.mikaelsvensson.doclet.shared.DocumentWrapper;
import info.mikaelsvensson.doclet.shared.ElementWrapper;
import info.mikaelsvensson.doclet.xml.documentcreator.AbstractDocumentCreator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.StringTokenizer;

public class Db2MetadataDocumentCreator extends AbstractDocumentCreator {

    private Db2ConverterDocletOptions options;

    protected Db2MetadataDocumentCreator(Db2ConverterDocletOptions options) throws ParserConfigurationException {
        super();
        this.options = options;
    }

    @Override
    public Document generateDocument(RootDoc doc) {
        DocumentWrapper dw = new DocumentWrapper(createDocument("data"));

        addDatabaseMetadata(dw);

        addJavadocMetadata(dw, doc);

        return dw.getDocument();
    }

    private void addJavadocMetadata(DocumentWrapper dw, RootDoc doc) {
        ElementWrapper classesEl = dw.addChild("classes");
        for (ClassDoc classDoc : doc.classes()) {
            String probablyTableName = classDoc.name().replace("Entity", "").toUpperCase();
            classesEl.addChild("class", 
                    "name", classDoc.name(),
                    "qualifiedname", classDoc.qualifiedName(),
                    "probabletablename", probablyTableName);
        }
    }

    private void addDatabaseMetadata(DocumentWrapper dw) {
        try {
            Database db = getDatabaseMetadata();

            ElementWrapper databaseEl = dw.addChild("database", "name", db.getName());
            for (Table table : db.getTables()) {
                ElementWrapper tableEl = databaseEl.addChild("table", "name", table.getName());
                for (Column column : table.getColumns()) {
                    tableEl.addChild("column", "name", column.getName()).setText(column.getDefinition());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Database getDatabaseMetadata() throws IOException, ParserConfigurationException, TransformerException {
        BufferedReader reader = new BufferedReader(new FileReader(options.getDb2SchemaFile()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith("--")) {
                sb.append(line);
            }
        }
        Database db = new Database("SPACE12");
        int i = 0;
        StringTokenizer tokenizer = new StringTokenizer(sb.toString(), ";", false);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            CommandType commandType = CommandType.fromSql(token);
//            System.out.println((commandType != null ? commandType.name() : "Unknown") + " command " + i++ + ": " + token.substring(0, Math.min(token.length(), 100)));
            if (null != commandType) {
                commandType.getCommandHandler().execute(db, token);
            } else {
                System.err.println("Unknown command: " + token);
            }
        }
        return db;
    }

}
