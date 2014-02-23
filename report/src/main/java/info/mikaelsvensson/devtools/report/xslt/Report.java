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

package info.mikaelsvensson.devtools.report.xslt;

public class Report {
    private String name;
    private String description;
    private String xmlFile;
    private String outputFile;
    private String xslFile;

    public String getDescription() {
        return description;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getXmlFile() {
        return xmlFile;
    }

    public String getXslFile() {
        return xslFile;
    }

    public String getOutputFile() {
        return outputFile;
    }
}
