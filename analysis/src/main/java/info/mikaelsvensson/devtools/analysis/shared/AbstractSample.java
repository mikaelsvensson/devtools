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

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractSample {
    protected int responseTime;
    private Date _sessionDate;
    private String _sessionDateString;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private File _sourceLogFile;

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    protected Date timeStamp;

    public AbstractSample(Date timeStamp, int responseTime) {
        this.timeStamp = timeStamp;
        this.responseTime = responseTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public void setSessionDate(Date sessionDate)
    {
        _sessionDate = sessionDate;
        _sessionDateString = DATE_FORMAT.format(sessionDate);
    }

    public Date getSessionDate()
    {
        return _sessionDate;
    }

    public String getSessionDateString()
    {
        return _sessionDateString;
    }

    public void setSourceLogFile(File sourceLogFile)
    {
        _sourceLogFile = sourceLogFile;
    }

    public File getSourceLogFile()
    {
        return _sourceLogFile;
    }
}
