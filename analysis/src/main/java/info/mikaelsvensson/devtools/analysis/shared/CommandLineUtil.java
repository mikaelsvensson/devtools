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

public class CommandLineUtil
{
    public static CommandLine parseArgs(String[] args, String usageHelp, Class<?> appClass, Option... opts)
    {
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
        catch (ParseException e)
        {
            final PrintWriter pw = new PrintWriter(System.out);
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
            System.exit(0);
            return null;
        }
    }
}
