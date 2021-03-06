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

package info.mikaelsvensson.devtools.analysis.db2;

import info.mikaelsvensson.devtools.analysis.shared.*;
import info.mikaelsvensson.devtools.analysis.shared.reportprinter.PlainTextReportPrinter;
import org.apache.commons.cli.CommandLine;

import java.io.File;

@CliOptions(opts = {
        @CliOptionConfig(
                required = true,
                argsDescription = "list",
                description = "list of all column names in source files",
                longName = Db2MonAnalyzer.OPT_ALL_COLUMN_NAMES,
                numArgs = CommandLineUtil.UNLIMITED,
                name = "cn"),
        @CliOptionConfig(
                argsDescription = "list",
                description = "list of columns to use in report (use same column names as specified by " + Db2MonAnalyzer.OPT_ALL_COLUMN_NAMES + " option)",
                longName = Db2MonAnalyzer.OPT_OUTPUT_COLUMN_NAMES,
                numArgs = CommandLineUtil.UNLIMITED,
                name = "ocn"),
        @CliOptionConfig(
                required = true,
                argsDescription = "list",
                description = "list of columns to identify rows (use same column names as specified by " + Db2MonAnalyzer.OPT_ALL_COLUMN_NAMES + " option)",
                longName = Db2MonAnalyzer.OPT_ID_COLUMN_NAMES,
                numArgs = CommandLineUtil.UNLIMITED,
                name = "idcn"),
        @CliOptionConfig(
                argsDescription = "regex",
                description = "regular expression used to filter which lines to include in reports",
                longName = Db2MonAnalyzer.OPT_ROW_ID_FILTER_PATTERN,
                numArgs = CommandLineUtil.UNLIMITED,
                name = "rfp"),
        @CliOptionConfig(
                required = true,
                argsDescription = "pattern",
                description = "MessageFormat pattern used for extracting timestamp from date column",
                longName = Db2MonAnalyzer.OPT_DATE_FORMAT,
                numArgs = 1,
                name = "df")
})
@CliHelp(text = "" +
        "Tool explicitly designed to aid in the analysis of DB2 statistics information acquired by " +
        "repeatedly exporting information from views/functions such as MON_GET_TABLE to text files. " +
        "The name of each such text file must contain a timestamp of some sort. All lines of all " +
        "input files must have the same number of columns.")
public class Db2MonAnalyzer extends AbstractAnalyzer {

    static final String OPT_ALL_COLUMN_NAMES = "all-column-names";
    static final String OPT_OUTPUT_COLUMN_NAMES = "output-column-names";
    static final String OPT_ID_COLUMN_NAMES = "id-column-names";
    static final String OPT_DATE_FORMAT = "date-format";
    static final String OPT_ROW_ID_FILTER_PATTERN = "rowid-filter-pattern";

    private static final int MINUTES_BETWEEN_SESSIONS = 1;

    public static void main(String[] args) throws Exception {
        new Db2MonAnalyzer().run(args);
    }

    @Override
    protected void runImpl(CommandLine commandLine, String[] filePaths, String reportFileName) throws Exception {
        String[] columnNames = commandLine.hasOption(OPT_ALL_COLUMN_NAMES) ? commandLine.getOptionValues(OPT_ALL_COLUMN_NAMES) : null;
        String[] outputColumnNames = commandLine.hasOption(OPT_OUTPUT_COLUMN_NAMES) ? commandLine.getOptionValues(OPT_OUTPUT_COLUMN_NAMES) : null;
        String[] idColumnNames = commandLine.hasOption(OPT_ID_COLUMN_NAMES) ? commandLine.getOptionValues(OPT_ID_COLUMN_NAMES) : null;
        String dateFormat = commandLine.getOptionValue(OPT_DATE_FORMAT);
        final String rowIdFilterPattern = commandLine.getOptionValue(OPT_ROW_ID_FILTER_PATTERN);
        final File[] files = new File[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            files[i] = new File(filePaths[i]);
        }
        Db2MonLog log = Db2MonLog.fromLogFile(MINUTES_BETWEEN_SESSIONS, dateFormat, files);
        final Db2MonReportGenerator reportGenerator = new Db2MonReportGenerator(
                log,
                columnNames,
                outputColumnNames,
                idColumnNames,
                rowIdFilterPattern);
        reportGenerator.generateReport(new File(getFormattedString(reportFileName, files[0])), new PlainTextReportPrinter());
    }
}
