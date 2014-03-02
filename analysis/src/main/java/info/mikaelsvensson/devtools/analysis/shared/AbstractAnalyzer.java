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

package info.mikaelsvensson.devtools.analysis.shared;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.File;
import java.util.LinkedHashMap;

public abstract class AbstractAnalyzer
{
    protected static final String FILE_PATH_GROUPER_DESCRIPTION = "optional string (none|merged|merge_by_folder) which, if present, affects which files are covered by each generated report ('none' = each log file processed individually, 'merge_all' = all log files will be used to generate a single report, 'merge_by_folder' = log files from the same folder will be processed together).";
    private static final String OPT_REPORT_FILE_NAME = "report-file-name";
    private static final String OPT_FILES = "files";

    protected void run(String[] args, String usageHelp, Option... commandLineOptions) throws Exception
    {
        Option[] defaultOptions = new Option[]{
                OptionBuilder.withArgName("output report file path").withDescription("file name for the report").isRequired().withLongOpt(OPT_REPORT_FILE_NAME).hasArg().create("rfn"),
                OptionBuilder.withArgName("input log files").withDescription("list of log files to analyze").isRequired().withLongOpt(OPT_FILES).hasArgs().create("f")
        };
        Option[] allOptions = new Option[defaultOptions.length + commandLineOptions.length];
        System.arraycopy(defaultOptions, 0, allOptions, 0, defaultOptions.length);
        System.arraycopy(commandLineOptions, 0, allOptions, defaultOptions.length, commandLineOptions.length);

        final CommandLine commandLine = CommandLineUtil.parseArgs(args, usageHelp, this.getClass(), allOptions);

        String reportFileName = commandLine.getOptionValue(OPT_REPORT_FILE_NAME);
        String[] files = commandLine.getOptionValues(OPT_FILES);
        runImpl(commandLine, files, reportFileName);
    }

    protected abstract void runImpl(CommandLine commandLine, String[] files, String reportFileName) throws Exception;

    public static String getFormattedString(String pattern, File patternArgumentSourceFile)
    {
        // LinkedHashMap since the values should be returned in the order of insertion (for backwards compatibility)
        final LinkedHashMap<String, Object> values = new LinkedHashMap<String, Object>();
        values.put("logFileName", patternArgumentSourceFile.getName());
        values.put("logFileNameWithoutExt", StringUtils.substringBeforeLast(patternArgumentSourceFile.getName(), "."));
        values.put("logFilePath", StringUtils.substringBeforeLast(patternArgumentSourceFile.getAbsolutePath(), "."));
        values.put("logFilePathWithoutExt", StringUtils.substringBeforeLast(patternArgumentSourceFile.getAbsolutePath(), "."));
        values.put("parentName", patternArgumentSourceFile.getParentFile().getName());
        values.put("parentPath", patternArgumentSourceFile.getParentFile().getAbsolutePath());

        // Add numeric "key" for each "parameter" (for backwards compability)
        int i = 0;
        for (Object value : values.values().toArray())
        {
            values.put(String.valueOf(i++), value);
        }

        // Use {key} format instead of ${key}, thus providing backwards compatibility with previously used MessageFormat.
        return StrSubstitutor.replace(pattern, values, "{", "}");
    }
}
