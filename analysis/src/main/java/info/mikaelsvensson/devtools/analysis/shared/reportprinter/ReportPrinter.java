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

public interface ReportPrinter
{
    void open(PrintStream printStream);

    void printTable(PrintStream printStream, String tableHeader, int valueColumnWidth, String[] headerRow, Object[][] dataRows, String[] footerRow);

    void printList(PrintStream printStream, String listHeader, List<Map<String, String>> keyValuePairsList);

    void close(PrintStream printStream);
}
