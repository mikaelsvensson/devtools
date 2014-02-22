package info.mikaelsvensson.devtools.sitesearch;

import info.mikaelsvensson.devtools.common.PathUtils;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AllPredicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.doxia.site.decoration.io.xpp3.DecorationXpp3Reader;
import org.apache.maven.doxia.tools.SiteTool;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @goal sitesearch
 * @phase post-site
 */
public class SiteSearchPlugin extends AbstractMojo {

    private static final String PLUGIN_ARTIFACT_ID = "site-search";

    private static final String PROPERTY_SEARCHBOX_CONTAINER_INSERTAT = "searchboxContainerInsertat";
    private static final String PROPERTY_SEARCHBOX_CONTAINER_INSERTACTION = "searchboxContainerInsertaction";
    private static final String PROPERTY_SEARCHBOX_CONTAINER_ID = "searchboxContainerId";
    private static final String PROPERTY_SEARCHBOX_BUTTON_LABEL = "searchboxButtonLabel";
    private static final String PROPERTY_SEARCHBOX_TITLE_LABEL = "searchboxTitleLabel";
    private static final String PROPERTY_SEARCHBOX_INDEX_EXCLUDECOMMONWORDS = "searchboxIndexExcludecommonwords";
    private static final String PROPERTY_SEARCHBOX_INDEX_EXCLUDENUMBERS = "searchboxIndexExcludenumbers";
    private static final String PROPERTY_SEARCHBOX_INDEX_MINWORDLENGTH = "searchboxIndexMinwordlength";
    private static final String PROPERTY_SEARCHBOX_INDEX_EXCLUDEFOLDER = "searchboxIndexExcludefolder";
    private static final String PROPERTY_SKIP_AUTO_JQUERY_INCLUSION = "skipAutoJqueryInclusion";
    private static final String PROPERTY_ADDITIONAL_MODULES_FOLDERS = "additionalIndexedModules";
    private static final String PROPERTY_AUTO_EMBED_RELATED_INDEXED_MODULES = "autoEmbedRelatedIndexedModules";

    private static final Comparator<Map.Entry<String, Integer>> DESCENDING_VALUE_ORDER = new Comparator<Map.Entry<String, Integer>>() {
        @Override
        public int compare(final Map.Entry<String, Integer> o1, final Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    };
    private static final String SEARCH_FORM_TEMPLATE = "" +
            "<div id=\"{1}\" class=\"sitesearch-form\">" +
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
    private static final String JAVASCRIPT_LIBRARY_FILE_NAME = "sitesearch.js";
    private static final String JAVASCRIPT_JQUERY_FILE_NAME = "jquery-1.8.2.min.js";
    private static final String JAVASCRIPT_DATABASE_FILE_NAME = "sitesearch-index.js";
    private static final String STYLESHEET_FILE_NAME = "sitesearch.css";
    private static final Pattern END_OF_HEAD_PATTERN = Pattern.compile("</(head|HEAD)>");
    private static final String JAVASCRIPT_VARIABLE_NAME_INDEX_DATA = "SiteIndexerData";
    private static final String JAVASCRIPT_DATA_TEMPLATE = "" +
            "var " + JAVASCRIPT_VARIABLE_NAME_INDEX_DATA + " = " + JAVASCRIPT_VARIABLE_NAME_INDEX_DATA + " || [];" +
            JAVASCRIPT_VARIABLE_NAME_INDEX_DATA + ".push('{' baseUrl: \"{2}\", data: {0} '}');";
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
        if (shouldJqueryLibraryFileBeIncluded()) {
            saveResourceAsFile(JAVASCRIPT_JQUERY_FILE_NAME, getJqueryLibraryFile());
        }

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
                File dir = baseDirectory;
                MavenProject proj = project;
                while (proj != null) {
                    File file = siteTool.getSiteDescriptorFromBasedir(siteDirectory, dir, null);
                    if (file.exists() && file.isFile()) {
                        DecorationModel decorationModel = getDecorationModel(file);
                        Xpp3Dom custom = (Xpp3Dom) decorationModel.getCustom();
                        if (custom != null) {
                            Xpp3Dom siteSearchPluginElement = custom.getChild("siteSearchPlugin");
                            if (null != siteSearchPluginElement) {
                                Xpp3Dom[] propertyElements = siteSearchPluginElement.getChildren();
                                for (Xpp3Dom propertyElement : propertyElements) {
                                    if (!properties.containsKey(propertyElement.getName())) {
                                        properties.put(propertyElement.getName(), propertyElement.getValue());
                                    }
                                }
                                getLog().info("Loaded configuration parameters from " + file.getAbsolutePath() + ".");
                            } else {
                                getLog().info("No site indexer configuration parameters defined in " + file.getAbsolutePath() + ".");
                            }
                        }
                    } else {
                        getLog().info("Looked for " + file.getAbsolutePath() + " but couldn't find it.");
                    }
                    proj = proj.getParent();
                    dir = proj != null ? proj.getBasedir() : null;
                }
            } catch (XmlPullParserException e) {
                getLog().warn(e);
            } catch (IOException e) {
                getLog().warn(e);
            }
        }
        return properties;
    }

    private DecorationModel getDecorationModel(final File file) throws IOException, XmlPullParserException {
        return new DecorationXpp3Reader().read(new FileReader(file));
    }

    private void createJavascriptDatabaseFile(Collection<IndexEntry> result) {
        try {
            String jsonRepresentation = JSONArray.fromObject(result).toString();
            String projectUrl = project.getUrl();
            FileUtils.writeStringToFile(getJavascriptDatabaseFile(), MessageFormat.format(
                    JAVASCRIPT_DATA_TEMPLATE,
                    jsonRepresentation,
                    project.getArtifactId(),
                    PathUtils.trailingSlash(projectUrl)));
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
                    IndexEntry indexEntry = createIndexEntry(item);
                    if (indexEntry != null) {
                        result.add(indexEntry);
                    } else {
                        getLog().info("Could not index " + item.getAbsolutePath());
                    }
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
            Element contentEl = document.getElementById("contentBox");
            if (contentEl == null) {
                contentEl = document.body();
            }
            if (contentEl != null) {
                String text = Jsoup.clean(contentEl.html(), Whitelist.simpleText());
                Collection<WordCount> wordCount = getWordCount(text);
                Collection<WordCount> filteredWordCount = filterWordCount(wordCount);
                return new IndexEntry(document.title(), getRelativePath(getSiteOutputFolder(), file), filteredWordCount);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private void addSearchFormToPage(final File file) {
        try {
            Set<String> relatedProjectPaths = getRelatedIndexedProjectPaths();

            StringBuilder content = new StringBuilder(FileUtils.readFileToString(file));

            Map<String, String> props = getProperties();

            String findExpr = props.get(PROPERTY_SEARCHBOX_CONTAINER_INSERTAT);
            Pattern insertionPointPattern = Pattern.compile(findExpr);
            ModifyAction modifyAction = ModifyAction.valueOf(props.get(PROPERTY_SEARCHBOX_CONTAINER_INSERTACTION).toUpperCase());
            String html = getSearchForm();
            boolean isFormInserted = modifyStringBuilder(content,
                    insertionPointPattern,
                    modifyAction,
                    html);
            if (isFormInserted) {
                if (shouldJqueryLibraryFileBeIncluded()) {
                    modifyStringBuilder(content,
                            END_OF_HEAD_PATTERN,
                            ModifyAction.PREPEND,
                            getScriptFileElement(getRelativePath(file, getJqueryLibraryFile())));
                }

                modifyStringBuilder(content,
                        END_OF_HEAD_PATTERN,
                        ModifyAction.PREPEND,
                        getScriptFileElement(getRelativePath(file, getJavascriptLibraryFile())));

                String pathToProjectRoot = getRelativePath(file, getSiteOutputFolder());

                for (String relatedProjectPath : relatedProjectPaths) {
                    if (StringUtils.isNotBlank(relatedProjectPath)) {
                        modifyStringBuilder(content,
                                END_OF_HEAD_PATTERN,
                                ModifyAction.PREPEND,
                                getScriptFileElement(PathUtils.trailingSlash(pathToProjectRoot) + PathUtils.trailingSlash(relatedProjectPath) + JAVASCRIPT_DATABASE_FILE_NAME));
                    }
                }

                String pathToAncestorProjectRoot = pathToProjectRoot;
                MavenProject ancestorProject = project;
                while (true) {
                    if (ancestorProject.getParent() != null) {
                        pathToAncestorProjectRoot = "../" + pathToAncestorProjectRoot;
                        ancestorProject = ancestorProject.getParent();
                    } else {
                        break;
                    }
                }

                modifyStringBuilder(content,
                        END_OF_HEAD_PATTERN,
                        ModifyAction.PREPEND,
                        getScriptFileElement(getRelativePath(file, getJavascriptDatabaseFile())));

                modifyStringBuilder(content,
                        END_OF_HEAD_PATTERN,
                        ModifyAction.PREPEND,
                        getStylesheetFileElement(getRelativePath(file, getStylesheetFile())));

                modifyStringBuilder(content,
                        END_OF_HEAD_PATTERN,
                        ModifyAction.PREPEND,
                        getScriptElement(MessageFormat.format(JAVASCRIPT_INIT_TEMPLATE, getProperties().get(PROPERTY_SEARCHBOX_CONTAINER_ID), getRelativePath(file, getSiteOutputFolder()), JAVASCRIPT_VARIABLE_NAME_INDEX_DATA)));
            }

            FileUtils.writeStringToFile(file, content.toString());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private Set<String> getRelatedIndexedProjectPaths() throws IOException {
        final Map<String, String> props = getProperties();
        Set<String> relatedProjectPaths = new HashSet<String>();

        if (Boolean.parseBoolean(props.get(PROPERTY_AUTO_EMBED_RELATED_INDEXED_MODULES))) {
            scanForProjectsInProjectHierarchyWithSiteIndexing(relatedProjectPaths);
        }

        String additionalModuleFolders = props.get(PROPERTY_ADDITIONAL_MODULES_FOLDERS);
        if (StringUtils.isNotEmpty(additionalModuleFolders)) {
            relatedProjectPaths.addAll(Arrays.asList(additionalModuleFolders.split("\\s")));
        }
        return relatedProjectPaths;
    }

    private void scanForProjectsInProjectHierarchyWithSiteIndexing(final Set<String> modulePaths) throws IOException {
        scanForProjectsInProjectHierarchyWithSiteIndexing(getRootProject(), false, modulePaths);
    }

    private MavenProject getRootProject() {
        MavenProject rootProject = project;
        while (rootProject.getParent() != null) {
            rootProject = rootProject.getParent();
        }
        return rootProject;
    }

    private void scanForProjectsInProjectHierarchyWithSiteIndexing(final MavenProject proj, boolean forceEmbed, Set<String> modulePaths) throws IOException {
        forceEmbed |= isSiteIndexerPluginReferences(proj);
        if (forceEmbed) {
            String pathFromProjectRootToOtherProject = getRelativePath(project.getBasedir(), proj.getModel().getPomFile().getParentFile());
            modulePaths.add(pathFromProjectRootToOtherProject);
        }

        for (Object moduleName : proj.getModules()) {
            try {
                File pomFile = new File(proj.getFile().getParentFile(), moduleName.toString() + File.separatorChar + "pom.xml");

                MavenProject subModuleProj = loadPomFile(pomFile);

                scanForProjectsInProjectHierarchyWithSiteIndexing(subModuleProj, forceEmbed, modulePaths);
            } catch (XmlPullParserException e) {
                getLog().warn("Could not read POM file for module '" + moduleName + "'.", e);
            }
        }
    }

    private static boolean isSiteIndexerPluginReferences(final MavenProject proj) {
        for (Object o : proj.getBuildPlugins()) {
            Plugin buildPlugin = (Plugin) o;
            if (PLUGIN_ARTIFACT_ID.equals(buildPlugin.getArtifactId())) {
                return true;
            }
        }
        return false;
    }

    private MavenProject loadPomFile(final File pomFile) throws IOException, XmlPullParserException {
        MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
        Model model = mavenXpp3Reader.read(new FileReader(pomFile));
        model.setPomFile(pomFile);
        return new MavenProject(model);
    }

    private boolean shouldJqueryLibraryFileBeIncluded() {
        return !Boolean.parseBoolean(properties.get(PROPERTY_SKIP_AUTO_JQUERY_INCLUSION));
    }

    private String getRelativePath(final File currentFile, final File linkTargetFile) {
        return PathUtils.getRelativePath(currentFile, linkTargetFile);
    }

    private File getJavascriptLibraryFile() {
        return new File(getSiteOutputFolder(), JAVASCRIPT_LIBRARY_FILE_NAME);
    }

    private File getJqueryLibraryFile() {
        return new File(getSiteOutputFolder(), JAVASCRIPT_JQUERY_FILE_NAME);
    }

    private File getJavascriptDatabaseFile() {
        return new File(getSiteOutputFolder(), JAVASCRIPT_DATABASE_FILE_NAME);
    }

    private File getStylesheetFile() {
        return new File(getSiteOutputFolder(), STYLESHEET_FILE_NAME);
    }

    private boolean modifyStringBuilder(final StringBuilder sb, final Pattern insertionPointPattern, final ModifyAction modifyAction, final String text) {
        Matcher matcher = insertionPointPattern.matcher(sb);
        if (matcher.find()) {
            switch (modifyAction) {
                case APPEND:
                    sb.insert(matcher.end(), text);
                    return true;
                case PREPEND:
                    sb.insert(matcher.start(), text);
                    return true;
                case REPLACE:
                    sb.replace(matcher.start(), matcher.end(), text);
                    return true;
            }
        }
        return false;
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
        CollectionUtils.filter(wordCount, getWordFilters());
        return wordCount;
    }

    private Predicate getWordFilters() {
        Collection<Predicate> predicates = new LinkedList<Predicate>();

        predicates.add(new WordEntryNotNullFilter());

        try {
            int minWordLength = Integer.parseInt(getProperties().get(PROPERTY_SEARCHBOX_INDEX_MINWORDLENGTH));
            if (minWordLength > 0) {
                predicates.add(new WordEntryLengthFilter(minWordLength));
            }
        } catch (NumberFormatException e) {
            getLog().info("Could not convert value of " + PROPERTY_SEARCHBOX_INDEX_MINWORDLENGTH + " to a number.", e);
        }

        boolean excludeNumbers = Boolean.getBoolean(getProperties().get(PROPERTY_SEARCHBOX_INDEX_EXCLUDENUMBERS));
        if (excludeNumbers) {
            predicates.add(new WordEntryNoNumbersFilter());
        }

        String excludeCommonWords = getProperties().get(PROPERTY_SEARCHBOX_INDEX_EXCLUDECOMMONWORDS);
        if (!StringUtils.isEmpty(excludeCommonWords)) {
            String[] localeNames = StringUtils.split(excludeCommonWords, ',');
            predicates.add(new WordEntryBlackListFilter(localeNames));
        }

        return new AllPredicate(predicates.toArray(new Predicate[predicates.size()]));
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
