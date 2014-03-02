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

import java.text.NumberFormat;
import java.util.Locale;

public class ToStringUtil
{
    static final NumberFormat NUMBER_FORMATTER;

    static {
        NUMBER_FORMATTER = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NUMBER_FORMATTER.setMaximumFractionDigits(2);
        NUMBER_FORMATTER.setMinimumFractionDigits(2);
        NUMBER_FORMATTER.setGroupingUsed(false);
    }

    public static String toString(Object o)
    {
        if (o instanceof Integer)
        {
            return Integer.toString((Integer)o);
        }
        else if (o instanceof Number)
        {
            return NUMBER_FORMATTER.format(((Number)o).doubleValue());
        }
        else
        {
            return o != null ? o.toString() : "";
        }
    }
}
