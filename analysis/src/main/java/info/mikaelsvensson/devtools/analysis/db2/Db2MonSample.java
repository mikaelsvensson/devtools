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

import info.mikaelsvensson.devtools.analysis.shared.AbstractSample;

import java.util.Date;

public class Db2MonSample extends AbstractSample
{
    private String[] _values;

    public Db2MonSample(Date timeStamp, String[] values)
    {
        super(timeStamp, -1);
        this._values = values;
    }

    public String[] getValues()
    {
        return _values;
    }

}
