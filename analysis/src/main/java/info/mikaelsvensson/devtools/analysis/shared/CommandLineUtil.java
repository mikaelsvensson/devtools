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

import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class CommandLineUtil {
    private static CommandLineUtil ourInstance = new CommandLineUtil();

    public static CommandLineUtil getInstance() {
        return ourInstance;
    }

    private CommandLineUtil() {
    }

    public static final int UNLIMITED = Option.UNLIMITED_VALUES;
    public static final int OPTIONAL = -3;

    public CommandLine parseArgs(String[] args, String usageHelp, Class<?> appClass, Option... opts) throws CommandLineException {
        final Options options = new Options();
        try
        {
            for (Option opt : opts)
            {
                options.addOption(opt);
            }

            CommandLineParser commandLineParser = new GnuParser();
            final CommandLine commandLine = commandLineParser.parse(options, args);
            return commandLine;
        }
        catch (ParseException e) {
            StringWriter writer = new StringWriter();
            final PrintWriter pw = new PrintWriter(writer);
            pw.println("ERROR:");
            pw.println(e.getMessage());

            HelpFormatter helpFormatter = new HelpFormatter();
            pw.println();
            pw.println("ABOUT:");
            final int consoleWidth = 120;
            helpFormatter.printWrapped(pw, consoleWidth, usageHelp);
            pw.println();
            helpFormatter.printUsage(pw,
                    consoleWidth,
                    "java " + appClass.getName(),
                    options);
            pw.println();
            pw.println("ARGUMENTS:");
            helpFormatter.printOptions(pw,
                    consoleWidth,
                    options,
                    0,
                    1);
            pw.close();
            throw new CommandLineException("Could not parse command line", writer.toString(), e);
        }
    }

    public List<Option> getOptions(Object owner) throws IllegalAccessException {
        List<Option> options = new ArrayList<Option>();
        Class<?> cls = owner.getClass();
        do {
            CliOptions cliOptions = cls.getAnnotation(CliOptions.class);
            if (cliOptions != null) {
                for (CliOptionConfig config : cliOptions.opts()) {

                    if (config != null) {
                        Option option = new Option(config.name(), config.description());
                        if (config.longName().length() > 0) {
                            option.setLongOpt(config.longName());
                        }
                        if (config.numArgs() == OPTIONAL) {
                            option.setOptionalArg(true);
                        } else {
                            option.setArgs(config.numArgs());
                        }
                        option.setArgName(config.argsDescription());
                        option.setRequired(config.required());
                        option.setValueSeparator(config.separator());
                        options.add(option);
                    }
                }
            }
        } while ((cls = cls.getSuperclass()) != null);
        return options;
    }

    public String getCliHelp(Object owner) {
        CliHelp cliHelp = owner.getClass().getAnnotation(CliHelp.class);
        if (cliHelp != null) {
            return cliHelp.text();
        }
        return null;
    }
}
