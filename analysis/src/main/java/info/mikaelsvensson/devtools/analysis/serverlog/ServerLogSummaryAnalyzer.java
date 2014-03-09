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

package info.mikaelsvensson.devtools.analysis.serverlog;

import info.mikaelsvensson.devtools.analysis.shared.AbstractAnalyzer;
import info.mikaelsvensson.devtools.analysis.shared.CliHelp;
import info.mikaelsvensson.devtools.analysis.shared.reportprinter.PlainTextReportPrinter;
import org.apache.commons.cli.CommandLine;

import java.io.File;

@CliHelp(text = "" +
        "Utility for summarizing the JBoss server log file based on message categories and priorities.")
public class ServerLogSummaryAnalyzer extends AbstractAnalyzer {
    public static void main(String[] args) throws Exception {
        new ServerLogSummaryAnalyzer().run(args);
    }

    @Override
    protected void runImpl(CommandLine commandLine, String[] files, String reportFileName) throws Exception {
        File[] fs = new File[files.length];
        for (int i = 0; i < files.length; i++) {
            String path = files[i];
            fs[i] = new File(path);
        }
        ServerLogSummaryLog log = ServerLogSummaryLog.fromLogFile(fs);
        final ServerLogSummaryReportGenerator reportGenerator = new ServerLogSummaryReportGenerator(log);
        reportGenerator.generateReport(new File(reportFileName), new PlainTextReportPrinter());
    }
}
