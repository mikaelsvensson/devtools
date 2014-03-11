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

package info.mikaelsvensson.devtools.analysis.localaccesslog;

import info.mikaelsvensson.devtools.analysis.shared.AbstractAnalyzer;
import info.mikaelsvensson.devtools.analysis.shared.CliHelp;
import info.mikaelsvensson.devtools.analysis.shared.ReportGenerator;
import info.mikaelsvensson.devtools.analysis.shared.reportprinter.PlainTextReportPrinter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
@CliHelp(text = "" +
        "Counts the number of different http status codes in the http access log, e.g. localhost_access_log.log. " +
        "The result is exported as both a plain-text report and a pie chart.")
public class LocalAccessLogAnalyzer extends AbstractAnalyzer
{

    public static void main(String[] args) throws Exception {
        new LocalAccessLogAnalyzer().run(args);
    }

    @Override
    protected void runImpl(CommandLine commandLine, String[] paths, String reportFileName) throws Exception
    {
        for (String path : paths)
        {
            File f = new File(path);
            Collection<File> files = f.isFile() ? Collections.singletonList(f) : (f.isDirectory() ? FileUtils.listFiles(f, new PrefixFileFilter("localhost_access_log"), TrueFileFilter.INSTANCE) : Collections.<File>emptyList());
            for (File file : files)
            {
                LocalAccessLog log = new LocalAccessLog(file);
                final ReportGenerator reportGenerator = new LocalAccessLogReportGenerator(log);
                final String pathname = getFormattedString(reportFileName, file);
                reportGenerator.generateReport(new File(pathname), new PlainTextReportPrinter());
            }
        }
    }
}
