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

import au.com.bytecode.opencsv.CSVReader;
import info.mikaelsvensson.devtools.analysis.shared.AbstractLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

public class Db2MonLog extends AbstractLog
{

    private static final char COL_SEPARATOR = ',';
    private MessageFormat _dateFormat;

    static Db2MonLog fromLogFile(int minutesBetweenSessions, String dateFormat, File... files) throws FileNotFoundException
    {
        return new Db2MonLog(minutesBetweenSessions, dateFormat, files);
    }

    private Db2MonLog(int minutesBetweenSessions, String dateFormat, File... logFiles) throws FileNotFoundException
    {
        super(minutesBetweenSessions);
        _dateFormat = new MessageFormat(dateFormat);
        load(logFiles);
    }

    protected void load(File[] logFiles) throws FileNotFoundException
    {
        for (File logFile : logFiles)
        {
            CSVReader reader = new CSVReader(new FileReader(logFile), COL_SEPARATOR, '\"');

            try
            {
                String[] values;
                while ((values = reader.readNext()) != null)
                {
                    final Date date = (Date)_dateFormat.parse(logFile.getName())[0];

                    for (int i = 0; i < values.length; i++)
                    {
                        values[i] = values[i].replace('\n', ' ');
                    }
                    addSample(new Db2MonSample(date, values));
                }
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            catch (ParseException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            finally
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        updateAllSampleSessionDates();
    }
}
