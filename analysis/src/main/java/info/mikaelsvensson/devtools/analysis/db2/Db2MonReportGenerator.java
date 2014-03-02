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

import info.mikaelsvensson.devtools.analysis.shared.AbstractLog;
import info.mikaelsvensson.devtools.analysis.shared.ReportGenerator;
import info.mikaelsvensson.devtools.analysis.shared.SampleCollector;
import info.mikaelsvensson.devtools.analysis.shared.reportprinter.ReportPrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.regex.Pattern;

public class Db2MonReportGenerator implements ReportGenerator
{
    private static final int MIN_VALUE_COLUMN_WIDTH = 10;
    private final String[] _columnNames;
    private final int[] _outputColumnIndexes;
    private final int[] _idColumnIndexes;
    private Db2MonLog _log;
    private Pattern _rowIdFilter;

    public Db2MonReportGenerator(Db2MonLog log, String[] columnNames, String[] outputColumnNames, String[] idColumnNames, String rowIdFilterPattern)
    {
        _log = log;
        _columnNames = columnNames;
        final List<String> names = Arrays.asList(_columnNames);
        if (outputColumnNames == null || outputColumnNames.length == 0)
        {
            List<String> temp = new ArrayList<String>(names);
            for (String idColumnName : idColumnNames)
            {
                temp.remove(idColumnName);
            }
            outputColumnNames = temp.toArray(new String[temp.size()]);
        }
        _outputColumnIndexes = new int[outputColumnNames.length];
        for (int i = 0; i < outputColumnNames.length; i++)
        {
            _outputColumnIndexes[i] = names.indexOf(outputColumnNames[i]);
        }
        _idColumnIndexes = new int[idColumnNames.length];
        for (int i = 0; i < idColumnNames.length; i++)
        {
            _idColumnIndexes[i] = names.indexOf(idColumnNames[i]);
        }
        if (rowIdFilterPattern != null)
        {
            _rowIdFilter = Pattern.compile(rowIdFilterPattern);
        }
    }

    @Override
    public void generateReport(File outputFile, ReportPrinter reportPrinter) throws FileNotFoundException
    {
        final PrintStream printStream = new PrintStream(outputFile);

        final Map<String, Collection<Db2MonSample>> samplesBySnapshot = AbstractLog.groupSamples(SampleCollector.COLLECTOR_BY_SESSION_DATE, _log.getSamples());
        Map<String, Map<String, String[]>> snapshotsMap = new TreeMap<String, Map<String, String[]>>();
        for (Map.Entry<String, Collection<Db2MonSample>> entry : samplesBySnapshot.entrySet())
        {
            final TreeMap<String, String[]> statsMap = new TreeMap<String, String[]>();
            snapshotsMap.put(entry.getKey(), statsMap);
            final Collection<Db2MonSample> snapshotSamples = entry.getValue();
            for (Db2MonSample sample : snapshotSamples)
            {
                final String rowId = getJoinedIdColumns(sample.getValues());
                statsMap.put(rowId, sample.getValues());
            }
        }
        Map.Entry<String, Map<String, String[]>>[] snapshots = snapshotsMap.entrySet().toArray(new Map.Entry[snapshotsMap.entrySet().size()]);
        for (int i = 1; i < snapshots.length; i++)
        {
            final String curSnapshotId = snapshots[i].getKey();
            final Map<String, String[]> curSnapshotStatsMap = snapshots[i].getValue();

            final String prevSnapshotId = snapshots[i - 1].getKey();
            final Map<String, String[]> prevSnapshotStatsMap = snapshots[i - 1].getValue();

            List<Object[]> data = new ArrayList<Object[]>();

            final int outputColumnCount = _outputColumnIndexes.length;
            String[] columnHeaders = new String[1 + outputColumnCount];
            int valueColumnWidth = MIN_VALUE_COLUMN_WIDTH;
            columnHeaders[0] = getJoinedIdColumns(_columnNames);
            for (int x = 0; x < outputColumnCount; x++)
            {
                int columnNameIndex = _outputColumnIndexes[x];
                columnHeaders[1 + x] = (_columnNames != null && columnNameIndex < _columnNames.length) ? _columnNames[columnNameIndex] : "Column " + columnNameIndex;
                valueColumnWidth = Math.max(valueColumnWidth, columnHeaders[1 + x].length() + 1);
            }
            long[] columnSums = new long[outputColumnCount];
            for (Map.Entry<String, String[]> entry : curSnapshotStatsMap.entrySet())
            {
                final String rowId = entry.getKey();
                if (_rowIdFilter == null || _rowIdFilter.matcher(rowId).find())
                {
                    final String[] curSnapshotStats = entry.getValue();
                    final String[] prevSnapshotStats = prevSnapshotStatsMap.containsKey(rowId) ? prevSnapshotStatsMap.get(rowId) : null;

                    Object[] row = new Object[1 + outputColumnCount];
                    row[0] = rowId;

                    boolean atLeastOneColumnValueDiffersComparedToPreviousSnapshot = false;

                    for (int x = 0; x < outputColumnCount; x++)
                    {
                        int columnNameIndex = _outputColumnIndexes[x];
                        String curValue = curSnapshotStats[columnNameIndex];
                        String prevValue = prevSnapshotStats != null ? prevSnapshotStats[columnNameIndex] : "0";
                        try
                        {
                            final int diff = Integer.parseInt(curValue) - Integer.parseInt(prevValue);
                            row[1 + x] = diff;
                            atLeastOneColumnValueDiffersComparedToPreviousSnapshot |= (diff != 0);
                            columnSums[x] += diff;
                        }
                        catch (NumberFormatException e)
                        {
                            row[1 + x] = "-";
                        }
                    }
                    if (atLeastOneColumnValueDiffersComparedToPreviousSnapshot)
                    {
                        data.add(row);
                    }
                }
            }

            final String[] footerRow = new String[1 + outputColumnCount];
            footerRow[0] = "SUM:";
            for (int j = 0; j < columnSums.length; j++)
            {
                long sum = columnSums[j];
                footerRow[1 + j] = Long.toString(sum);
            }
            reportPrinter.printTable(printStream,
                    "Difference between " + prevSnapshotId + " and " + curSnapshotId,
                    valueColumnWidth,
                    columnHeaders,
                    data.toArray(new Object[data.size()][]),
                    footerRow);
        }
        printStream.close();
    }

    private String getJoinedIdColumns(String[] row)
    {
        StringBuilder id = new StringBuilder();
        for (int idColumnIndex : _idColumnIndexes)
        {
            if (id.length() > 0)
            {
                id.append(' ');
            }
            id.append(row[idColumnIndex]);
        }
        return id.toString();
    }

    @Override
    public void generateSamplesCSV(File file) throws FileNotFoundException
    {
        throw new UnsupportedOperationException("CSV reports not supported by " + getClass().getSimpleName());
    }

}
