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

import java.util.Date;

public class Log4JLogSample extends AbstractSample
{
    private String _priority;
    private String _category;
    private StringBuilder _message;

    public Log4JLogSample(Date timeStamp, String priority, String category, String message)
    {
        super(timeStamp, -1);

        this._priority = priority;
        this._category = category;
        this._message = new StringBuilder(message);
    }

    public String getPriority()
    {
        return _priority;
    }

    public String getCategory()
    {
        return _category;
    }

    public String getMessage()
    {
        return _message.toString();
    }

    public void appendMessageLine(String message)
    {
        this._message.append('\n').append(message);
    }

}
