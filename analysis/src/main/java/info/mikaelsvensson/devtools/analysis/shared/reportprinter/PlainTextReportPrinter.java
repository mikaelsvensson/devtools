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

package info.mikaelsvensson.devtools.analysis.shared.reportprinter;

import info.mikaelsvensson.devtools.analysis.shared.ToStringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public class PlainTextReportPrinter implements ReportPrinter {
    @Override
    public void open(PrintStream printStream) {
    }

    @Override
    public void printTable(PrintStream printStream, String tableHeader, int valueColumnWidth, String[] headerRow, Object[][] dataRows, String[] footerRow) {
        int firstColumnWidth = Math.max(
                headerRow != null && headerRow.length > 0 ? headerRow[0].length() : 0,
                footerRow != null && footerRow.length > 0 ? footerRow[0].length() : 0);
        for (Object[] row : dataRows) {
            if (row != null)
            {
                firstColumnWidth = Math.max(firstColumnWidth, row[0].toString().length());
            }
        }
        if (tableHeader != null) {
            printHeader(tableHeader, true, printStream);
        }
        String rowFormat = "%-" + (firstColumnWidth + 1) + "s" + StringUtils.repeat("%" + valueColumnWidth + "s", dataRows != null && dataRows.length > 0 ? dataRows[0].length - 1 : headerRow.length - 1) + "\n";
        if (headerRow != null) {
            printStream.format(rowFormat, headerRow);
        }
        if (dataRows != null)
        {
            for (Object[] row : dataRows) {
                if (row != null)
                {
                    for (int i = 0; i < row.length; i++)
                    {
                        row[i] = ToStringUtil.toString(row[i]);
                    }
                    if (row != null)
                    {
                        printStream.format(rowFormat, row);
                    }
                }
            }
        }
        if (footerRow != null) {
            printStream.format(rowFormat, footerRow);
        }
    }

    @Override
    public void printList(PrintStream printStream, String listHeader, List<Map<String, String>> keyValuePairsList)
    {
        int i = 1;
        for (Map<String, String> map : keyValuePairsList)
        {
            printHeader(listHeader + (keyValuePairsList.size() > 1 ? ", item " + (i++) + " of " + keyValuePairsList.size() : ""), false, printStream);
            int maxKeyLength = 0;
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                maxKeyLength = Math.max(maxKeyLength, entry.getKey().length());
            }
            maxKeyLength += 3;
            String newLineReplacement = '\n' + StringUtils.repeat(' ', maxKeyLength);
            final String format = "    %-" + maxKeyLength + "s%s%n";
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                printStream.format(format, entry.getKey(), entry.getValue().replace("\n", newLineReplacement));
            }
        }
    }

    @Override
    public void close(PrintStream printStream) {
    }

    private void printHeader(String text, boolean marginTop, PrintStream printStream) {
        if (marginTop) {
            printStream.println();
        }
        printStream.println(text);
        printStream.println(StringUtils.repeat('-', text.length()));
    }
}
