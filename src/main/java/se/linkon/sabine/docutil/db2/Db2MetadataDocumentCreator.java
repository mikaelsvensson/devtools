package se.linkon.sabine.docutil.db2;

import com.sun.javadoc.*;
import org.w3c.dom.Document;
import se.linkon.sabine.docutil.db2.metadata.*;
import se.linkon.sabine.docutil.db2.parser.AbstractCommandHandler;
import se.linkon.sabine.docutil.db2.parser.CommandType;
import se.linkon.sabine.docutil.shared.DocumentCreatorException;
import se.linkon.sabine.docutil.shared.DocumentWrapper;
import se.linkon.sabine.docutil.shared.ElementWrapper;
import se.linkon.sabine.docutil.xml.documentcreator.AbstractDocumentCreator;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.Transient;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Db2MetadataDocumentCreator extends AbstractDocumentCreator {
    private static final String ATTR_PROBABLE_DATABASE_NAME = "probable-database-name";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_QUALIFIED_NAME = "qualified-name";
    private static final String ATTR_TYPE = "type";
    public static final String PROPERTY_DATABASE_NAME = "Database Name";

    private Db2ConverterDocletAction options;

    public Db2MetadataDocumentCreator(Db2ConverterDocletAction options) throws ParserConfigurationException {
        super();
        this.options = options;
    }

    @Override
    public Document generateDocument(RootDoc doc) throws DocumentCreatorException {
        DocumentWrapper dw = new DocumentWrapper(createDocument("data"));

        addDatabaseMetadata(dw);

        addJavadocMetadata(dw, doc);

        return dw.getDocument();
    }

    private void addJavadocMetadata(DocumentWrapper dw, RootDoc doc) {
        ElementWrapper classesEl = dw.addChild("classes");
        for (ClassDoc classDoc : doc.classes()) {
            String probablyTableName = getTranslatedName(classDoc);
            ElementWrapper clsEl = classesEl.addChild("class",
                    ATTR_NAME, classDoc.name(),
                    ATTR_QUALIFIED_NAME, classDoc.qualifiedName(),
                    ATTR_PROBABLE_DATABASE_NAME, probablyTableName,
                    "entity", Boolean.toString(isAnnotated(classDoc, Entity.class)));
            ClassDoc fieldsClass = classDoc;

            addComment(clsEl, classDoc.inlineTags(), doc);
            
            do {
                for (FieldDoc field : fieldsClass.fields(false)) {
                    if (!isAnnotated(field, Transient.class) && !field.isStatic()) {
                        ElementWrapper fieldEl = clsEl.addChild("field",
                                ATTR_NAME, field.name(),
                                ATTR_PROBABLE_DATABASE_NAME, getTranslatedName(field),
                                ATTR_TYPE, field.type().qualifiedTypeName());
                        addComment(fieldEl, field.inlineTags(), doc);
                        ParameterizedType parameterizedType = field.type().asParameterizedType();

                        boolean isList = field.type().simpleTypeName().equals(List.class.getSimpleName());
                        boolean isMap = field.type().simpleTypeName().equals(Map.class.getSimpleName());
                        boolean isGeneric = null != parameterizedType;
                        if (isGeneric && (isList || isMap)) {
                            ClassDoc targetClass = parameterizedType.typeArguments()[isList ? 0 : 1].asClassDoc();
                            String targetTableName = getTranslatedName(targetClass);
                            String sourceTableName = getTranslatedName(classDoc);
                            AnnotationDesc joinTableAnnotation = getAnnotation(field, JoinTable.class);

                            fieldEl.setAttribute("type-parameter", targetClass.qualifiedName());

                            String joinTable = sourceTableName + "_" + targetTableName;
                            if (joinTableAnnotation != null) {
                                Object value = joinTableAnnotation.elementValues()[0].value().value();
                                if (value instanceof String) {
                                    joinTable = ((String) value).toUpperCase();
                                }
                            }
                            fieldEl.setAttribute("join-table", joinTable);
                        }
                    }
                }
            } while ((fieldsClass = fieldsClass.superclass()) != null);
        }
    }

    private String getTranslatedName(ClassDoc classDoc) {
        AnnotationDesc annotation = getAnnotation(classDoc, javax.persistence.Table.class);
        if (null != annotation) {
            for (AnnotationDesc.ElementValuePair pair : annotation.elementValues()) {
                if (pair.element().name().equals("name") && pair.value().value() instanceof String) {
                    String name = (String) pair.value().value();
                    return name.toUpperCase();
                }
            }
        }
        return classDoc.name().replace("Entity", "").toUpperCase();
    }

    private String getTranslatedName(FieldDoc field) {
        return field.name().toUpperCase();
    }

    private boolean isAnnotated(ProgramElementDoc programElementDoc, Class<?> annotationClass) {
        return getAnnotation(programElementDoc, annotationClass) != null;
    }

    private AnnotationDesc getAnnotation(ProgramElementDoc programElementDoc, Class<?> annotationClass) {
        for (AnnotationDesc annotation : programElementDoc.annotations()) {
            if (annotation.annotationType().qualifiedName().equals(annotationClass.getName())) {
                return annotation;
            }
        }
        return null;
    }

    private void addDatabaseMetadata(DocumentWrapper dw) throws DocumentCreatorException {
        Database db = getDatabaseMetadata();

        ElementWrapper databaseEl = dw.addChild("database", ATTR_NAME, db.getName());

        ElementWrapper propertiesEl = databaseEl.addChild("properties");
        for (Map.Entry<String, String> entry : db.getProperties().entrySet()) {
            propertiesEl.addChild("property", ATTR_NAME, entry.getKey(), "value", entry.getValue());
        }
        if (db.getTimeStamp() != null) {
            databaseEl.setAttribute("timestamp", DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(db.getTimeStamp()));
        }

        for (Table table : db.getTables()) {
            ElementWrapper tableEl = databaseEl.addChild("table", ATTR_NAME, table.getName());

            ElementWrapper sqlCmdsEl = tableEl.addChild("sql-commands");
            for (String sqlCommand : table.getSqlCommands()) {
//                System.out.println(sqlCommand);
                sqlCmdsEl.addChild("sql-command").setText(sqlCommand);
            }
            
            for (Column column : table.getColumns()) {
                ElementWrapper columnEl = tableEl.addChild("column", ATTR_NAME, column.getName()).setText(column.getDefinition());
                if (null != column.getDb2Datatype()) {
                    columnEl.setAttribute("db2type", column.getDb2Datatype().name());
                    columnEl.setAttribute("type", column.getDb2Datatype().getType().name().toLowerCase());
                }
                if (table.isReferenceColumn(column)) {
                    columnEl.setAttribute("type", ColumnType.REFERENCE.name().toLowerCase());
                }
            }
            for (ForeignKey foreignKey : table.getForeignKeys()) {
                tableEl.addChild("foreign-key",
                        ATTR_NAME, foreignKey.getName(),
                        "column", foreignKey.getColumn(),
                        "referenced-schema", foreignKey.getReferencedSchema(),
                        "referenced-table", foreignKey.getReferencedTable(),
                        "referenced-column", foreignKey.getReferencedCol());
            }
            String[] primaryKey = table.getPrimaryKey();
            if (null != primaryKey) {
                ElementWrapper primaryKeyEl = tableEl.addChild("primary-key");

                for (String columnName : primaryKey) {
                    primaryKeyEl.addChild("column", ATTR_NAME, columnName);
                }
            }

            for (Index index : table.getIndexes()) {
                ElementWrapper indexEl = tableEl.addChild("index", ATTR_NAME, index.getName(), "unique", Boolean.toString(index.isUnique()));
                for (String columnName : index.getColumns()) {
                    indexEl.addChild("column", ATTR_NAME, columnName);
                }
            }
        }
    }

    public Database getDatabaseMetadata() throws DocumentCreatorException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(options.getDb2SchemaFile()));
            String line;
            StringBuilder sb = new StringBuilder();
            Database db = new Database();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--")) {
                    Pattern propertyPattern = Pattern.compile("--\\s+([\\w\\s]+):(.*)");
                    Matcher matcher = propertyPattern.matcher(line);
                    if (matcher.matches()) {
                        db.setProperty(matcher.group(1), matcher.group(2));
                    }
                } else {
                    sb.append(line).append('\n');
                }
            }
            String timestamp = db.getProperties().get("Timestamp");
            if (timestamp != null) {
//                System.out.println("timestamp = " + timestamp);
                /*                                    Wed 13 Jun 2012 05:05:00 PM CEST */
                DateFormat df = new SimpleDateFormat("EEE dd MMM yyyy hh:mm:ss a z", Locale.ENGLISH);
//                System.out.println(df.format(new Date()));
                try {
                    Date date = df.parse(timestamp.trim());
                    db.setTimeStamp(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Silently ignore parsing error. Printing time stamp is not that important anyway.
                }
            }
            if (db.getProperties().containsKey(PROPERTY_DATABASE_NAME)) {
                db.setName(db.getProperties().get(PROPERTY_DATABASE_NAME).trim());
            }
            int i = 0;
            StringTokenizer tokenizer = new StringTokenizer(sb.toString(), ";", false);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken().trim();
                if (token.length() > 0) {

                    CommandType commandType = CommandType.fromSql(token);
//                String affectedTableName = AbstractCommandHandler.getAffectedTableName(token);
//                System.out.println("AFFECTED TABLE " + affectedTableName);
                    if (null != commandType) {
                        commandType.getCommandHandler().execute(db, token);
                    } else {
                        throw new DocumentCreatorException("Cannot interpret command '" + token + "'");
                    }
                }
            }

            return db;
        } catch (FileNotFoundException e) {
            throw new DocumentCreatorException("Could not find database schema DDL file " + e.getMessage(), e);
        } catch (IOException e) {
            throw new DocumentCreatorException("Generic IO exception when reading database schema DDL file.", e);
        }
    }

}
