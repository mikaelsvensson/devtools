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

package info.mikaelsvensson.docutil.xml;

import info.mikaelsvensson.docutil.shared.propertyset.PropertySet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlDocletAction {

    public static final String FORMAT_STANDARD = "standard";
    public static final String FORMAT_ENUM = "enum";
    public static final String FORMAT_ELEMENTS_ONLY = "elementsonly";
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

/*
    public DocumentCreator createDocumentCreator(final Map<String, String> parameters) throws DocumentCreatorException {
        try {
            if (FORMAT_STANDARD.equalsIgnoreCase(format)) {
                return new StandardDocumentCreator(parameters);
            } else if (FORMAT_ENUM.equalsIgnoreCase(format)) {
                return new EnumDocumentCreator(parameters);
            } else if (FORMAT_ELEMENTS_ONLY.equalsIgnoreCase(format)) {
                return new ElementsOnlyDocumentCreator();
            }
        } catch (ParserConfigurationException e) {
            throw new DocumentCreatorException("Could not create formatter for the format '" + format + "'.", e);
        }
        throw new DocumentCreatorException("Could not find a suitable formatter for the format '" + format + "'.");
    }
*/

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
