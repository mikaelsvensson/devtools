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

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

public abstract class SampleCollector<T extends AbstractSample> implements SampleGrouper<T>, SampleFilter<T>
{

    public static final SampleCollector COLLECTOR_BY_SESSION_DATE = new SampleCollector<AbstractSample>()
    {
        @Override
        public String getGroupName(AbstractSample sample)
        {
            return StringUtils.defaultIfEmpty(sample.getSessionDateString(), "unknown");
        }
    };

    @Override
    public boolean accepts(T sample)
    {
        return true;
    }

    @Override
    public String getGroupName(T sample)
    {
        return "";
    }

    public Map<String, Collection<T>> getFilteredAndGrouped(Collection<T> samples)
    {
        return AbstractLog.groupSamples(this, samples);
    }
}
