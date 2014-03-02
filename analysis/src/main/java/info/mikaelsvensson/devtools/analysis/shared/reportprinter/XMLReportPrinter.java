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

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public class XMLReportPrinter implements ReportPrinter{
    int tableCount = 0;
    @Override
    public void open(PrintStream printStream) {
        printStream.print("<report>");
    }

    @Override
    public void printTable(PrintStream printStream, String tableHeader, int otherColumnWidth, String[] headerRow, Object[][] dataRows, String[] footerRow) {
        printStream.print("<table");
        printStream.print(" id=\"table");
        printStream.print(Integer.toString(tableCount++));
        printStream.print("\"");
        if (tableHeader != null) {
            printStream.print(" caption=\"");
            printStream.print(tableHeader);
            printStream.print("\"");
            printStream.print(" name=\"");
            printStream.print(simplify(tableHeader));
            printStream.print("\"");
        }
        printStream.println(">");
        printStream.println("<header>");
        if (headerRow != null) {
            printRow(printStream, headerRow, headerRow);
        }
        printStream.println("</header>");
        printStream.println("<body>");
        for (Object[] row : dataRows) {
            printRow(printStream, headerRow, row);
        }
        printStream.println("</body>");
        printStream.println("<footer>");
        if (footerRow != null) {
            printRow(printStream, headerRow, footerRow);
        }
        printStream.println("</footer>");
        printStream.println("</table>");
    }

    @Override
    public void printList(PrintStream printStream, String listHeader, List<Map<String, String>> keyValuePairsList)
    {
        printStream.print("<list");
        printStream.print(" id=\"table");
        printStream.print(Integer.toString(tableCount++));
        printStream.print("\"");
        if (listHeader != null) {
            printStream.print(" caption=\"");
            printStream.print(listHeader);
            printStream.print("\"");
            printStream.print(" name=\"");
            printStream.print(simplify(listHeader));
            printStream.print("\"");
        }
        printStream.println(">");

        for (Map<String, String> map : keyValuePairsList)
        {
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                printStream.print("<item");
                printStream.print(" caption=\"");
                printStream.print(entry.getKey());
                printStream.print("\"");
                printStream.print(" name=\"");
                printStream.print(simplify(entry.getKey()));
                printStream.print("\">");
                printStream.println(entry.getValue());
                printStream.print("</item>");
            }
        }
        printStream.print("</list>");

    }

    private String simplify(String str) {
        return str.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }

    private void printRow(PrintStream printStream, String[] headerRow, Object[] row) {
        printStream.print("<row");
        for (int i = 0; i < row.length; i++) {
            Object value = row[i];
            String key = headerRow != null && headerRow.length > i ? toAttributeName(headerRow[i]) : "col" + i;
            printStream.format(" %s=\"%s\"", key, value);
        }
        printStream.println(" />");
    }

    private String toAttributeName(String s) {
        return s.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
    }

    @Override
    public void close(PrintStream printStream) {
        printStream.print("</report>");
    }

}
