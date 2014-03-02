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

import info.mikaelsvensson.devtools.analysis.shared.LoadLogFileCallback;
import info.mikaelsvensson.devtools.analysis.shared.Log4JLog;
import info.mikaelsvensson.devtools.analysis.shared.Log4JLogSample;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerLogSummaryLog extends Log4JLog<Log4JLogSample>
{
    private static final Pattern EXCEPTION_CLASS_NAME_PATTERN = Pattern.compile("(([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+Exception):");

    private List<Entry> entries;

    private ServerLogSummaryLog(File... logFiles) throws IOException, XMLStreamException
    {
        super(logFiles);
    }

    @Override
    public Collection<Log4JLogSample> getSamples()
    {
        throw new UnsupportedOperationException("This is a summary log and individual samples are not available");
    }

    @Override
    protected void load(File logFile, LoadLogFileCallback<Log4JLogSample> callback, boolean updateAllSampleSessionDates) throws FileNotFoundException
    {
        if (entries == null)
        {
            entries = new ArrayList<Entry>();
        }
        super.load(logFile, callback, updateAllSampleSessionDates);
    }

    public List<Entry> getEntries()
    {
        return entries;
    }

    @Override
    protected void addSample(Log4JLogSample sample)
    {
        Entry entry = new Entry(sample.getCategory(), sample.getPriority());
        final int index = entries.indexOf(entry);
        if (index >= 0)
        {
            entry = entries.get(index);
        }
        else
        {
            entries.add(entry);
        }
        entry.count++;
        Matcher matcher = EXCEPTION_CLASS_NAME_PATTERN.matcher(sample.getMessage());
        if (matcher.find())
        {
            entry.countExceptions++;
        }
    }

    static ServerLogSummaryLog fromLogFile(File... logFile) throws IOException, XMLStreamException
    {
        return new ServerLogSummaryLog(logFile);
    }

    static class Entry implements Comparable<Entry>
    {

        private String category;
        private String severity;
        private int count;
        private int countExceptions;

        private Entry(String category, String severity)
        {
            this.category = category;
            this.severity = severity;
        }

        String getCategory()
        {
            return category;
        }

        int getCount()
        {
            return count;
        }

        int getCountExceptions()
        {
            return countExceptions;
        }

        String getSeverity()
        {
            return severity;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            Entry entry = (Entry)o;

            if (category != null ? !category.equals(entry.category) : entry.category != null)
            {
                return false;
            }
            if (severity != null ? !severity.equals(entry.severity) : entry.severity != null)
            {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode()
        {
            int result = severity != null ? severity.hashCode() : 0;
            result = 31 * result + (category != null ? category.hashCode() : 0);
            return result;
        }

        @Override
        public String toString()
        {
            return category + '-' + severity;
        }

        @Override
        public int compareTo(Entry o)
        {
            int categoryValue = category.compareTo(o.category);
            return categoryValue != 0 ? categoryValue : severity.compareTo(o.severity);
        }

    }

}
