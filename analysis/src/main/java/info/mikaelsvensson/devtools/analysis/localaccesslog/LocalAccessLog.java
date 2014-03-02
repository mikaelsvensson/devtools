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

package info.mikaelsvensson.devtools.analysis.localaccesslog;

import info.mikaelsvensson.devtools.analysis.shared.AbstractSingleLineSamplesLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalAccessLog extends AbstractSingleLineSamplesLog<LocalAccessLogSample>
{

    public static final String REGEXP_STATUS_CODE = "\\d+";
    public static final String REGEXP_RESPONSE_LENGTH = "\\d+";
    public static final String REGEXP_REPONSE_TIME = "\\d+";
    public static final String REGEXP_REQUEST = "[^\"]+";
    public static final String REGEXP_TIME_STAMP = "\\d+/\\w+/[\\d:+\\s]+";
    public static final String REGEXP_IP_ADDRESS = "\\d+.\\d+.\\d+.\\d+";
    public static final Pattern LINE_PATTERN = Pattern.compile("(" +
            REGEXP_IP_ADDRESS +
            ") - - \\[(" +
            REGEXP_TIME_STAMP +
            ")\\] \"(" +
            REGEXP_REQUEST +
            ")\" (" +
            REGEXP_STATUS_CODE +
            ") (" +
            REGEXP_RESPONSE_LENGTH +
            ") (" +
            REGEXP_REPONSE_TIME + ")");
    public static final SimpleDateFormat DATE_FORMAT_TIME_STAMP = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");

    public LocalAccessLog(File file) throws FileNotFoundException
    {
        super();
        load(file, true);
    }

    @Override
    protected LocalAccessLogSample sampleFromLine(String data)
    {
        Matcher matcher = LINE_PATTERN.matcher(data);
        if (matcher.matches())
        {
            try
            {
                String ipAddress = matcher.group(1);
                Date timeStamp = DATE_FORMAT_TIME_STAMP.parse(matcher.group(2));
                String request = matcher.group(3);
                int statusCode = Integer.parseInt(matcher.group(4));
                int responseLength = Integer.parseInt(matcher.group(5));
                int responseTime = Integer.parseInt(matcher.group(6));
                return new LocalAccessLogSample(timeStamp, responseTime, ipAddress, request, statusCode, responseLength);
            }
            catch (ParseException e)
            {
                return null;
            }
        }
        return null;
    }

    @Override
    protected boolean startsNewSample(String line)
    {
        return true;
    }
}
