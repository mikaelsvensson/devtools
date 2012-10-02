package info.mikaelsvensson.doctools.report.siteindexerplugin;

import net.sf.json.JSONObject;
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
            "<div id=\"{1}\">" +
            "   <div id=\"{1}-title\">" +
            "       {0}" +
            "   </div>" +
            "   <div id=\"{1}-field\">" +
            "       <input type=\"text\" id=\"{1}-searchfield\" />" +
            "   </div>" +
            "   <div>" +
            "       <button type=\"button\" id=\"{1}-button\">{2}</button>" +
            "   </div>" +
            "</div>";
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
    private static final WordEntryFilter MORE_THAN_ONE_OCCURRENCE = new WordEntryFilter() {
        @Override
        public boolean accept(final Map.Entry<String, Integer> entry) {
            return entry.getValue() > 1;
        }
    };

    private static final WordEntryFilter LONGER_THAN_2_CHARS = new WordEntryFilter() {
        @Override
        public boolean accept(final Map.Entry<String, Integer> entry) {
            return entry.getKey().length() > 2;
        }
    };
    private HashMap<String, String> properties;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File buildFolder = new File(buildDirectoryPath);
        File siteFolder = new File(buildFolder, "site");

        Map<String, String> properties = getProperties();

        getLog().info(properties.get(PROPERTY_SEARCHBOX_TITLE_LABEL));

        HashMap<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();

        processFolder(siteFolder, result);

        writeIndexFile(result, new File(siteFolder, "index.js"));

        addJavascriptFile(siteFolder);

        addSearchFormToPagesInFolder(siteFolder);
    }

    private void addJavascriptFile(final File folder) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("test.js");
        InputStreamReader reader = new InputStreamReader(stream);

        try {
            byte[] bytes = IOUtils.toByteArray(stream);
            FileUtils.writeByteArrayToFile(new File(folder, "test-copy.js"), bytes);
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

    private void writeIndexFile(final HashMap<String, Map<String, Integer>> result, final File file) {
        try {
            String jsonRepresentation = JSONObject.fromObject(result).toString();
            FileUtils.writeStringToFile(file, jsonRepresentation);
        } catch (IOException e) {
            getLog().error(e);
        }
    }

    private void processFolder(final File folder, Map<String, Map<String, Integer>> result) {
        File[] items = folder.listFiles(HTML_FILES_FILTER);
        if (items != null) {
            for (File item : items) {
                if (item.isDirectory()) {
                    processFolder(item, result);
                } else {
                    Map<String, Integer> words = processFile(item);
                    result.put(item.getName(), words);
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

    private Map<String, Integer> processFile(final File file) {
        try {
            Document document = Jsoup.parse(file, "UTF-8", "http://invalid.host");
            String text = Jsoup.clean(document.body().html(), Whitelist.simpleText());
            Map<String, Integer> wordCount = getWordCount(text);
            Map<String, Integer> filteredWordCount = filterWordCount(wordCount);

            return filteredWordCount;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private void addSearchFormToPage(final File file) {
        try {
            StringBuilder content = new StringBuilder(FileUtils.readFileToString(file));

            Matcher endOfHeadMatcher = Pattern.compile("</(head|HEAD)>").matcher(content);
            if (endOfHeadMatcher.find()) {
                content.insert(endOfHeadMatcher.start(), "<script src=\"test-copy.js\"></script>");
            }
            String findExpr = getProperties().get(PROPERTY_SEARCHBOX_CONTAINER_INSERTAT);
            Matcher matcher = Pattern.compile(findExpr).matcher(content);
            if (matcher.find()) {
                if ("append".equals(getProperties().get(PROPERTY_SEARCHBOX_CONTAINER_INSERTACTION))) {
                    content.insert(matcher.end(), getSearchForm());
                } else if ("prepend".equals(getProperties().get(PROPERTY_SEARCHBOX_CONTAINER_INSERTACTION))) {
                    content.insert(matcher.start(), getSearchForm());
                } else if ("replace".equals(getProperties().get(PROPERTY_SEARCHBOX_CONTAINER_INSERTACTION))) {
                    content.replace(matcher.start(), matcher.end(), getSearchForm());
                }
            }

            FileUtils.writeStringToFile(file, content.toString());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private String getSearchForm() {
        return MessageFormat.format(
                SEARCH_FORM_TEMPLATE,
                getProperties().get(PROPERTY_SEARCHBOX_TITLE_LABEL),
                getProperties().get(PROPERTY_SEARCHBOX_CONTAINER_ID),
                getProperties().get(PROPERTY_SEARCHBOX_BUTTON_LABEL));
    }

    private Map<String, Integer> filterWordCount(final Map<String, Integer> wordCount) {
        SortedSet<Map.Entry<String, Integer>> words = new TreeSet(DESCENDING_VALUE_ORDER);
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (MORE_THAN_ONE_OCCURRENCE.accept(entry)) {
                words.add(entry);
            }
        }
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> word : words) {
            map.put(word.getKey(), word.getValue());
        }
        return map;
    }

    private Map<String, Integer> getWordCount(final String text) {
        Map<String, Integer> words = new HashMap<String, Integer>();
        StringTokenizer tokenizer = new StringTokenizer(text);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (words.containsKey(token)) {
                words.put(token, words.get(token) + 1);
            } else {
                words.put(token, 1);
            }
        }
        return words;
    }

}
