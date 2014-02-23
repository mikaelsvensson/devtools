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

package info.mikaelsvensson.devtools.doclet.xml;

import info.mikaelsvensson.devtools.doclet.shared.propertyset.PropertySet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlDocletAction {

    //TODO Move these constants to XmlDoclet
    public static final String FORMAT = "format";
    public static final String FORMAT_NAME = "name";
    public static final String OUTPUT = "output";
    public static final String PROPERTY = "property";
    public static final String POSTPROCESSOR = "postprocessor";
    public static final String TRANSFORMER = "transformer";

    public XmlDocletAction(PropertySet propertySet) {
        this.format = propertySet.getProperty(FORMAT + "." + FORMAT_NAME);

        File outputPath = new File(propertySet.getProperty(OUTPUT));
        if (outputPath != null) {
            this.output = outputPath;
        }

        this.parameters = propertySet.getPropertySet(FORMAT + '.' + PROPERTY);

        this.postProcessingParameters = propertySet.getProperties(POSTPROCESSOR + "." + PROPERTY);

        this.postProcessor = propertySet.getProperty(POSTPROCESSOR + "." + FORMAT_NAME);

        String transformerPath = propertySet.getProperty(TRANSFORMER);
        if (null != transformerPath) {
            this.transformer = new File(transformerPath);
        }
    }

    private File output;
    private File transformer;
    protected String format;

    public void setFormat(final String format) {
        this.format = format;
    }

    //TODO: MISV 20120618 Rename to formatParameters.
    private PropertySet parameters = new PropertySet();

    //TODO: MISV 20120618 Rearrange/sort fields and members.
    private String postProcessor;

    public String getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(String postProcessor) {
        this.postProcessor = postProcessor;
    }

    public Map<String, String> getPostProcessingParameters() {
        return postProcessingParameters;
    }

    private Map<String, String> postProcessingParameters = new HashMap<String, String>();

    public PropertySet getParameters() {
        return parameters;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(final File output) {
        this.output = output;
    }

    public File getTransformer() {
        return transformer;
    }

    public void setTransformer(final File transformer) {
        this.transformer = transformer;
    }

    public static int optionLength(String option) {
        if (option.startsWith("-" + FORMAT) ||
                option.startsWith("-" + OUTPUT) ||
                option.startsWith("-" + POSTPROCESSOR) ||
                option.startsWith("-" + TRANSFORMER)) {
            return 2;
        }
        return 0;
    }
}
