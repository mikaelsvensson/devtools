package info.mikaelsvensson.doctools.report.siteindexerplugin;

import net.sf.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.doxia.site.decoration.io.xpp3.DecorationXpp3Reader;
import org.apache.maven.doxia.tools.SiteTool;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @goal siteindexer
 * @phase post-site
 */
public class SiteIndexerPlugin extends AbstractMojo {

    private static final String PROPERTY_SEARCHBOX_CONTAINER_INSERTAT = "searchboxContainerInsertat";
    private static final String PROPERTY_SEARCHBOX_CONTAINER_INSERTACTION = "searchboxContainerInsertaction";
    private static final String PROPERTY_SEARCHBOX_CONTAINER_ID = "searchboxContainerId";
    private static final String PROPERTY_SEARCHBOX_BUTTON_LABEL = "searchboxButtonLabel";
    private static final String PROPERTY_SEARCHBOX_TITLE_LABEL = "searchboxTitleLabel";
    private static final String PROPERTY_SEARCHBOX_INDEX_EXCLUDECOMMONWORDS = "searchboxIndexExcludecommonwords";
    private static final String PROPERTY_SEARCHBOX_INDEX_EXCLUDENUMBERS = "searchboxIndexExcludenumbers";
    private static final String PROPERTY_SEARCHBOX_INDEX_MINWORDLENGTH = "searchboxIndexMinwordlength";
    private static final String PROPERTY_SEARCHBOX_INDEX_EXCLUDEFOLDER = "searchboxIndexExcludefolder";

    private static final Comparator<Map.Entry<String, Integer>> DESCENDING_VALUE_ORDER = new Comparator<Map.Entry<String, Integer>>() {
        @Override
        public int compare(final Map.Entry<String, Integer> o1, final Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    };
    private static final String SEARCH_FORM_TEMPLATE = "" +
            "<div id=\"{1}\" class=\"siteindexer-form\">" +
            "   <div id=\"{1}-title\">" +
            "       {0}" +
            "   </div>" +
            "   <div id=\"{1}-field\">" +
            "       <input type=\"text\" id=\"{1}-searchfield\" />" +
            "   </div>" +
            "</div>";
    private static final String SCRIPT_FILE_ELEMENT_TEMPLATE = "<script src=\"{0}\" type=\"text/javascript\"></script>";
    private static final String STYLESHEET_FILE_ELEMENT_TEMPLATE = "<link href=\"{0}\" type=\"text/css\" rel=\"stylesheet\">";
    private static final String SCRIPT_ELEMENT_TEMPLATE = "<script type=\"text/javascript\">{0}</script>";
    private static final String JAVASCRIPT_LIBRARY_FILE_NAME = "siteindexer.js";
    private static final String JAVASCRIPT_DATABASE_FILE_NAME = "siteindexer-data.js";
    private static final String STYLESHEET_FILE_NAME = "siteindexer.css";
    private static final Pattern END_OF_HEAD_PATTERN = Pattern.compile("</(head|HEAD)>");
    private static final String JAVASCRIPT_VARIABLE_NAME_INDEX_DATA = "SiteIndexerData";
    private static final String JAVASCRIPT_DATA_TEMPLATE = "var " + JAVASCRIPT_VARIABLE_NAME_INDEX_DATA + " = {0};";
    private static final String JAVASCRIPT_INIT_TEMPLATE = "" +
            "$(document).ready(" +
            "   function () '{' " +
            "       var si = new SiteIndexer(\"{1}\", {2}, \"{0}\"); " +
            "       si.init(); " +
            "   '}'" +
            ");";
    /**
     * Directory where reports will go.
     *
     * @parameter property="project.build.directory"
     * @required
     * @readonly
     */
    private String buildDirectoryPath;

    /** @parameter expression="src/site" */
    protected String siteDirectory;

    /** @parameter property="basedir" */
    protected File baseDirectory;

    /** @component */
    private SiteTool siteTool;

    /**
     * @parameter property="project"
     * @required
     * @readonly
     */
    protected MavenProject project;

    private static final FileFilter HTML_FILES_FILTER = new FileFilter() {
        @Override
        public boolean accept(final File pathname) {
            if (pathname.isDirectory()) {
                return true;
            } else {
                String name = pathname.getName().toLowerCase();
                return name.endsWith(".html") || name.endsWith(".htm");
            }
        }
    };
    private static final WordEntryFilter MORE_THAN_ONE_OCCURRENCE = new AbstractWordEntryFilter() {
        @Override
        public boolean accept(final WordCount entry) {
            return entry.getCount() > 1;
        }
    };

    private static final WordEntryFilter LONGER_THAN_2_CHARS = new AbstractWordEntryFilter() {
        @Override
        public boolean accept(final WordCount entry) {
            return entry.getWord().length() > 2;
        }
    };
    private HashMap<String, String> properties;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File siteFolder = getSiteOutputFolder();

        Map<String, String> properties = getProperties();

        getLog().info(properties.get(PROPERTY_SEARCHBOX_TITLE_LABEL));

        Collection<IndexEntry> result = new LinkedList<IndexEntry>();

        processFolder(siteFolder, result);

        createJavascriptDatabaseFile(result);

        saveResourceAsFile(JAVASCRIPT_LIBRARY_FILE_NAME, getJavascriptLibraryFile());
        saveResourceAsFile(STYLESHEET_FILE_NAME, getStylesheetFile());

        addSearchFormToPagesInFolder(siteFolder);
    }

    private File getSiteOutputFolder() {
        return new File(buildDirectoryPath, "site");
    }

    private void saveResourceAsFile(final String resourceName, final File file) {
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName);
            byte[] bytes = IOUtils.toByteArray(stream);
            FileUtils.writeByteArrayToFile(file, bytes);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private Map<String, String> getProperties() {
        if (null == properties) {
            properties = new HashMap<String, String>();
            try {
                DecorationModel decorationModel = getDecorationModel();
                Xpp3Dom[] propertyElements = ((Xpp3Dom) decorationModel.getCustom()).getChild("siteIndexerPlugin").getChildren();
                for (Xpp3Dom propertyElement : propertyElements) {
                    properties.put(propertyElement.getName(), propertyElement.getValue());
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return properties;
    }

    private DecorationModel getDecorationModel() throws IOException, XmlPullParserException {
        File file = siteTool.getSiteDescriptorFromBasedir(siteDirectory, baseDirectory, null);
        return new DecorationXpp3Reader().read(new FileReader(file));
    }

    private void createJavascriptDatabaseFile(Collection<IndexEntry> result) {
        try {
            String jsonRepresentation = JSONArray.fromObject(result).toString();
            FileUtils.writeStringToFile(getJavascriptDatabaseFile(), MessageFormat.format(JAVASCRIPT_DATA_TEMPLATE, jsonRepresentation));
        } catch (IOException e) {
            getLog().error(e);
        }
    }

    private void processFolder(final File folder, Collection<IndexEntry> result) {
        File[] items = folder.listFiles(HTML_FILES_FILTER);
        if (items != null) {
            for (File item : items) {
                if (item.isDirectory()) {
                    processFolder(item, result);
                } else {
                    result.add(createIndexEntry(item));
                }
            }
        }
    }

    private void addSearchFormToPagesInFolder(final File folder) {
        File[] items = folder.listFiles(HTML_FILES_FILTER);
        if (items != null) {
            for (File item : items) {
                if (item.isDirectory()) {
                    addSearchFormToPagesInFolder(item);
                } else {
                    addSearchFormToPage(item);
                }
            }
        }
    }

    private IndexEntry createIndexEntry(final File file) {
        try {
            Document document = Jsoup.parse(file, "UTF-8", "http://invalid.host");
            String text = Jsoup.clean(document.getElementById("contentBox").html(), Whitelist.simpleText());
            Collection<WordCount> wordCount = getWordCount(text);
            Collection<WordCount> filteredWordCount = filterWordCount(wordCount);
            return new IndexEntry(document.title(), getRelativePath(getSiteOutputFolder(), file), filteredWordCount);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private void addSearchFormToPage(final File file) {
        try {
            StringBuilder content = new StringBuilder(FileUtils.readFileToString(file));

            modifyStringBuilder(content,
                    END_OF_HEAD_PATTERN,
                    ModifyAction.PREPEND,
                    getScriptFileElement(getRelativePath(file, getJavascriptLibraryFile())));

            modifyStringBuilder(content,
                    END_OF_HEAD_PATTERN,
                    ModifyAction.PREPEND,
                    getScriptFileElement(getRelativePath(file, getJavascriptDatabaseFile()).toString()));

            modifyStringBuilder(content,
                    END_OF_HEAD_PATTERN,
                    ModifyAction.PREPEND,
                    getStylesheetFileElement(getRelativePath(file, getStylesheetFile()).toString()));

            modifyStringBuilder(content,
                    END_OF_HEAD_PATTERN,
                    ModifyAction.PREPEND,
                    getScriptElement(MessageFormat.format(JAVASCRIPT_INIT_TEMPLATE, getProperties().get(PROPERTY_SEARCHBOX_CONTAINER_ID), getRelativePath(file, getSiteOutputFolder()), JAVASCRIPT_VARIABLE_NAME_INDEX_DATA)));

            Map<String, String> props = getProperties();
            String findExpr = props.get(PROPERTY_SEARCHBOX_CONTAINER_INSERTAT);
            Pattern insertionPointPattern = Pattern.compile(findExpr);

            ModifyAction modifyAction = ModifyAction.valueOf(props.get(PROPERTY_SEARCHBOX_CONTAINER_INSERTACTION).toUpperCase());
            String html = getSearchForm();
            modifyStringBuilder(content,
                    insertionPointPattern,
                    modifyAction,
                    html);

            FileUtils.writeStringToFile(file, content.toString());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private String getRelativePath(final File currentFile, final File linkTargetFile) {
        return PathUtils.getRelativePath(currentFile, linkTargetFile);
    }

    private File getJavascriptLibraryFile() {
        return new File(getSiteOutputFolder(), JAVASCRIPT_LIBRARY_FILE_NAME);
    }

    private File getJavascriptDatabaseFile() {
        return new File(getSiteOutputFolder(), JAVASCRIPT_DATABASE_FILE_NAME);
    }

    private File getStylesheetFile() {
        return new File(getSiteOutputFolder(), STYLESHEET_FILE_NAME);
    }

    private void modifyStringBuilder(final StringBuilder sb, final Pattern insertionPointPattern, final ModifyAction modifyAction, final String text) {
        Matcher matcher = insertionPointPattern.matcher(sb);
        if (matcher.find()) {
            switch (modifyAction) {
                case APPEND:
                    sb.insert(matcher.end(), text);
                    break;
                case PREPEND:
                    sb.insert(matcher.start(), text);
                    break;
                case REPLACE:
                    sb.replace(matcher.start(), matcher.end(), text);
                    break;
            }
        }
    }

    private String getScriptFileElement(final String fileName) {
        return MessageFormat.format(SCRIPT_FILE_ELEMENT_TEMPLATE, fileName);
    }

    private String getStylesheetFileElement(final String fileName) {
        return MessageFormat.format(STYLESHEET_FILE_ELEMENT_TEMPLATE, fileName);
    }

    private String getScriptElement(final String script) {
        return MessageFormat.format(SCRIPT_ELEMENT_TEMPLATE, script);
    }

    private String getSearchForm() {
        return MessageFormat.format(
                SEARCH_FORM_TEMPLATE,
                getProperties().get(PROPERTY_SEARCHBOX_TITLE_LABEL),
                getProperties().get(PROPERTY_SEARCHBOX_CONTAINER_ID),
                getProperties().get(PROPERTY_SEARCHBOX_BUTTON_LABEL));
    }

    private Collection<WordCount> filterWordCount(final Collection<WordCount> wordCount) {
//        CollectionUtils.filter(wordCount, MORE_THAN_ONE_OCCURRENCE);
        return wordCount;
    }

    private Collection<WordCount> getWordCount(final String text) {
        List<WordCount> words = new ArrayList<WordCount>();
        StringTokenizer tokenizer = new StringTokenizer(text);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().toLowerCase();
            WordCount wordCount = new WordCount(token, 1);
            if (words.contains(wordCount)) {
                words.get(words.indexOf(wordCount)).increaseCount();
            } else {
                words.add(wordCount);
            }
        }
        return words;
    }

}
