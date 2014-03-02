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

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses Log4J logs were entries use this format:
 * <p/>
 * %d %-5p [%c] %m%n
 * <p/>
 * Sample entry:
 * <p/>
 * 2014-01-18 00:09:30,805 INFO  [hibernate] 58031177,99,454,24070768,901178,10090,5722,2115,0,100,30
 * <p/>
 * The pattern "%d %-5p [%c] (%t) %m%n" is also accepted but the entire "(%t) %m" will be treated as the message.
 *
 * @param <T>
 */
public class Log4JLog<T extends Log4JLogSample> extends AbstractSingleLineSamplesLog<T>
{
    public static final SimpleDateFormat DATE_FORMATTER_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    public static final SimpleDateFormat DATE_FORMATTER_TIME_ONLY = new SimpleDateFormat("HH:mm:ss,SSS");

    private static final String PATTERN_TIME = "\\d{2}:\\d{2}:\\d{2},\\d{3}";
    private static final String PATTERN_DATETIME = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}";
    private static final String PATTERN_PRIORITY = "[A-Z]+";
    private static final String PATTERN_CATEGORY_NAME = "[a-zA-Z0-9.]+";
    private static final Pattern ENTRY_PATTERN = Pattern.compile("" +
            "(" + PATTERN_TIME + "|" + PATTERN_DATETIME + ") " +
            "(" + PATTERN_PRIORITY + ")\\s*\\[(" + PATTERN_CATEGORY_NAME + ")\\]\\s*(.*)", Pattern.DOTALL);

    protected Log4JLog(File... logFiles) throws IOException, XMLStreamException
    {
        super();
        load(logFiles);
        sortSamplesByTimeStamp();
    }

    @Override
    protected T sampleFromLine(String data)
    {
        Matcher matcher = ENTRY_PATTERN.matcher(data);
        if (matcher.find())
        {
            Date timeStamp = parseDate(matcher.group(1), matcher.group(1).length() == DATE_FORMATTER_TIME_ONLY.toPattern().length() ? DATE_FORMATTER_TIME_ONLY : DATE_FORMATTER_YYYYMMDD);
            String priority = matcher.group(2);
            String category = matcher.group(3);
            String message = matcher.group(4);

            if (timeStamp != null)
            {
                if (null == firstSampleDate || firstSampleDate.after(timeStamp))
                {
                    firstSampleDate = timeStamp;
                }
                if (null == lastSampleDate || lastSampleDate.before(timeStamp))
                {
                    lastSampleDate = timeStamp;
                }
            }
            return (T)createSample(timeStamp, priority, category, message);
        }
        return null;
    }

    protected Log4JLogSample createSample(Date timeStamp, String priority, String category, String message)
    {
        return new Log4JLogSample(timeStamp, priority, category, message);
    }

    @Override
    protected boolean startsNewSample(String line)
    {
        return ENTRY_PATTERN.matcher(line).find();
    }

}
