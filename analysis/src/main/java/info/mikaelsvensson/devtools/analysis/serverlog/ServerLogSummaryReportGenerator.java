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

import info.mikaelsvensson.devtools.analysis.shared.ReportGenerator;
import info.mikaelsvensson.devtools.analysis.shared.reportprinter.ReportPrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ServerLogSummaryReportGenerator implements ReportGenerator
{
    private static final Comparator<ServerLogSummaryLog.Entry> CATEGORY_COMPARATOR = new Comparator<ServerLogSummaryLog.Entry>()
    {
        @Override
        public int compare(ServerLogSummaryLog.Entry o1, ServerLogSummaryLog.Entry o2)
        {
            return o1.getCategory().compareTo(o2.getCategory());
        }
    };
    private static final Comparator<ServerLogSummaryLog.Entry> COUNT_COMPARATOR_DESC = new Comparator<ServerLogSummaryLog.Entry>()
    {
        @Override
        public int compare(ServerLogSummaryLog.Entry o1, ServerLogSummaryLog.Entry o2)
        {
            return o2.getCount() - o1.getCount();
        }
    };
    private ServerLogSummaryLog _log;

    public ServerLogSummaryReportGenerator(ServerLogSummaryLog log)
    {
        _log = log;
    }

    @Override
    public void generateReport(File outputFile, ReportPrinter reportPrinter) throws IOException
    {
        final PrintStream stream = new PrintStream(outputFile);
        reportPrinter.open(stream);

        String[] headers = {"Category", "Priority", "# total", "# exceptions"};
        final int valueColumnWidth = 15;

        final List<ServerLogSummaryLog.Entry> entries = _log.getEntries();

        Collections.sort(entries, CATEGORY_COMPARATOR);
        reportPrinter.printTable(stream,
                "Grouped And Sorted By Category",
                valueColumnWidth,
                headers,
                toArray(entries),
                null);

        Collections.sort(entries, COUNT_COMPARATOR_DESC);
        reportPrinter.printTable(stream,
                "Grouped By Category And Sorted By Count",
                valueColumnWidth,
                headers,
                toArray(entries),
                null);

        reportPrinter.close(stream);
        stream.close();
    }

    private Object[][] toArray(List<ServerLogSummaryLog.Entry> entries)
    {
        Object[][] data = new Object[entries.size()][];
        for (int i = 0; i < entries.size(); i++)
        {
            ServerLogSummaryLog.Entry entry = entries.get(i);
            data[i] = new Object[]{
                    entry.getCategory(),
                    entry.getSeverity(),
                    entry.getCount(),
                    entry.getCountExceptions()};
        }
        return data;
    }

    @Override
    public void generateSamplesCSV(File file) throws FileNotFoundException
    {
        throw new UnsupportedOperationException();
    }
}
