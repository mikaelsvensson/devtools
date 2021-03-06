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

package info.mikaelsvensson.devtools.report.sitemap;

import info.mikaelsvensson.devtools.report.DoctoolsReport;
import info.mikaelsvensson.devtools.report.HtmlFileCreator;
import info.mikaelsvensson.devtools.report.HtmlFileCreatorFactory;
import org.apache.maven.doxia.siterenderer.Renderer;

import java.io.File;
import java.util.Locale;

/**
 * @goal sitemap
 * @phase site
 */
public class SiteMapReport extends DoctoolsReport {
    private static final String SUB_FOLDER_NAME = "site-map";

    /**
     * Directory where reports will go.
     *
     * @parameter expression="${project.build.sourceDirectory}"
     * @required
     * @readonly
     */
    private String sourceDirectory;

    @Override
    protected void render(final HtmlFileCreator defaultPageCreator, final HtmlFileCreatorFactory pageCreatorFactory, final Locale locale) {
        File root = new File(getOutputDirectory());
        new FolderSiteMapGenerator(defaultPageCreator, getDefaultPageTitle(locale)).printFolderContent(root);
    }

    @Override
    protected String getDefaultPageTitle(final Locale locale) {
        return getString(locale, "sitemap.outputfoldertree.title");
    }

    /**
     * @param siteRenderer
     *         The siteRenderer to set.
     */
    public void setSiteRenderer(Renderer siteRenderer) {
        this.siteRenderer = siteRenderer;
    }

    @Override
    public String getOutputName() {
        return SUB_FOLDER_NAME + "/index";
    }

    @Override
    public String getName(Locale locale) {
        return getString(locale, "sitemap.title");
    }

    @Override
    public String getDescription(Locale locale) {
        return getString(locale, "sitemap.description");
    }
}
