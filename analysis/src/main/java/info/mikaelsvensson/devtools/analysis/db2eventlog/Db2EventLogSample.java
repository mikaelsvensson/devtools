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

import info.mikaelsvensson.devtools.analysis.shared.AbstractSample;

import java.util.Date;

public class Db2EventLogSample extends AbstractSample {

    private String text;
    private String operation;

    private long fetchCount;
    private long sorts;
    private long totalSortTime;
    private long sortOverflows;
    private long rowsRead;
    private long rowsWritten;
//    private long internalRowsDeleted;
//    private long internalRowsUpdated;
//    private long internalRowsInserted;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Db2EventLogSample(Date timeStamp, int responseTime) {
        super(timeStamp, responseTime);
    }

    public long getFetchCount() {
        return fetchCount;
    }

    public void increaseFetchCount(long fetchCount) {
        this.fetchCount += fetchCount;
    }

/*
    public long getInternalRowsDeleted()
    {
        return internalRowsDeleted;
    }

    public void increaseInternalRowsDeleted(long internalRowsDeleted)
    {
        this.internalRowsDeleted += internalRowsDeleted;
    }

    public long getInternalRowsInserted()
    {
        return internalRowsInserted;
    }

    public void increaseInternalRowsInserted(long internalRowsInserted)
    {
        this.internalRowsInserted += internalRowsInserted;
    }

    public long getInternalRowsUpdated()
    {
        return internalRowsUpdated;
    }

    public void increaseInternalRowsUpdated(long internalRowsUpdated)
    {
        this.internalRowsUpdated += internalRowsUpdated;
    }
*/

    public long getRowsRead() {
        return rowsRead;
    }

    public void increaseRowsRead(long rowsRead) {
        this.rowsRead += rowsRead;
    }

    public long getRowsWritten() {
        return rowsWritten;
    }

    public void increaseRowsWritten(long rowsWritten) {
        this.rowsWritten += rowsWritten;
    }

    public long getSortOverflows() {
        return sortOverflows;
    }

    public void increaseSortOverflows(long sortOverflows) {
        this.sortOverflows += sortOverflows;
    }

    public long getSorts() {
        return sorts;
    }

    public void increaseSorts(long sorts) {
        this.sorts += sorts;
    }

    public long getTotalSortTime() {
        return totalSortTime;
    }

    public void increaseTotalSortTime(long totalSortTime) {
        this.totalSortTime += totalSortTime;
    }
}
