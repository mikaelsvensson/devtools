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

package info.mikaelsvensson.devtools.analysis.db2eventlog;

import org.apache.commons.lang3.mutable.MutableLong;

import java.util.*;

class QueryStatistics
{
    static final Comparator<QueryStatistics> TOTAL_TIME_COMPARATOR = Collections.reverseOrder(new Comparator<QueryStatistics>()
    {
        @Override
        public int compare(QueryStatistics o1, QueryStatistics o2)
        {
            long o1Time = o1.getAccumulatedExecutionTime();
            long o2Time = o2.getAccumulatedExecutionTime();
            return o1Time == o2Time ? 0 : o1Time > o2Time ? 1 : -1;
        }
    });
    static final Comparator<QueryStatistics> CALL_COUNT_COMPARATOR = Collections.reverseOrder(new Comparator<QueryStatistics>()
    {
        @Override
        public int compare(QueryStatistics o1, QueryStatistics o2)
        {
            return o1.getTotalSamples() - o2.getTotalSamples();
        }
    });

    String getQuery()
    {
        return query;
    }

    int getTotalSamples()
    {
        return totalSamples;
    }

    Map<String, MutableLong> getOperationsTime()
    {
        return operationsTime;
    }

    int getId()
    {
        return id;
    }

    private String query;
    private int totalSamples;
    private Date firstTimeStamp;
    private Date lastTimeStamp;
    private long fetchCount;
    private long sorts;
    private long totalSortTime;
    private long sortOverflows;
    private long rowsRead;
    private long rowsWritten;
    private Map<String, MutableLong> operationsTime = new LinkedHashMap<String, MutableLong>();
    private int id;

    QueryStatistics(String query, int id)
    {
        this.query = query;
        this.id = id;
    }

    void addSampleData(Db2EventLogSample sample)
    {
        MutableLong sum = operationsTime.get(sample.getOperation());
        if (sum == null)
        {
            sum = new MutableLong(sample.getResponseTime());
            operationsTime.put(sample.getOperation(), sum);
        }
        else
        {
            sum.add(sample.getResponseTime());
        }
        totalSamples++;
        fetchCount += sample.getFetchCount();
        sorts += sample.getSorts();
        totalSortTime += sample.getTotalSortTime();
        sortOverflows += sample.getSortOverflows();
        rowsRead += sample.getRowsRead();
        rowsWritten += sample.getRowsWritten();

        if (lastTimeStamp == null || sample.getTimeStamp().after(lastTimeStamp))
        {
            lastTimeStamp = sample.getTimeStamp();
        }
        if (firstTimeStamp == null || sample.getTimeStamp().before(firstTimeStamp))
        {
            firstTimeStamp = sample.getTimeStamp();
        }
    }

    long getAccumulatedExecutionTime()
    {
        long sum = 0;
        for (MutableLong mutableLong : operationsTime.values())
        {
            sum += mutableLong.longValue();
        }
        return sum;
    }

    Date getLastTimeStamp()
    {
        return lastTimeStamp;
    }

    Date getFirstTimeStamp()
    {
        return firstTimeStamp;
    }

    long getFetchCount()
    {
        return fetchCount;
    }

    long getRowsRead()
    {
        return rowsRead;
    }

    long getRowsWritten()
    {
        return rowsWritten;
    }

    long getSortOverflows()
    {
        return sortOverflows;
    }

    long getSorts()
    {
        return sorts;
    }

    long getTotalSortTime()
    {
        return totalSortTime;
    }
}
