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

import java.io.*;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

public abstract class AbstractSingleLineSamplesLog<T extends AbstractSample> extends AbstractLog<T>
{

    private static final int DEFAULT_MINUTES_BETWEEN_SESSIONS = 2;

    private Collection<File> _loadedFiles = new LinkedList<File>();
    private boolean _normalizeTimeStamps = false;

    /**
     * @param minutesBetweenSessions
     * @param normalizeTimeStamps    normalize all timestamps so that all logs are considered to begin at the exact
     *                               same time (the time of the first log entry in the first log file). This is useful
     *                               when using log files from servers whose clocks are not synchronized.
     */
    protected AbstractSingleLineSamplesLog(int minutesBetweenSessions, boolean normalizeTimeStamps)
    {
        super(minutesBetweenSessions);
        _normalizeTimeStamps = normalizeTimeStamps;
    }

    protected AbstractSingleLineSamplesLog()
    {
        this(DEFAULT_MINUTES_BETWEEN_SESSIONS, false);
    }

    protected void load(File logFile, boolean updateAllSampleSessionDates) throws FileNotFoundException
    {
        load(logFile, null, updateAllSampleSessionDates);
    }

    protected void load(File logFile, LoadLogFileCallback<T> callback, boolean updateAllSampleSessionDates) throws FileNotFoundException
    {
        Long timeStampOffset = null;
        _loadedFiles.add(logFile);
        BufferedReader reader = new BufferedReader(new FileReader(logFile));
        String line;
        StringBuilder sampleBuffer = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                if (startsNewSample(line))
                {
                    if (sampleBuffer != null && sampleBuffer.length() > 0)
                    {
                        T sample = createSample(sampleBuffer, logFile, callback);
                        if (sample != null)
                        {
                            timeStampOffset = addSampleAndReturnTimeStampOffset(sample, timeStampOffset);
                        }
                        sampleBuffer = null;
                    }
                }
                if (sampleBuffer == null)
                {
                    sampleBuffer = new StringBuilder();
                }
                else
                {
                    sampleBuffer.append('\n');
                }
                sampleBuffer.append(line);
            }
            if (sampleBuffer != null && sampleBuffer.length() > 0)
            {
                T sample = createSample(sampleBuffer, logFile, callback);
                if (sample != null)
                {
                    timeStampOffset = addSampleAndReturnTimeStampOffset(sample, timeStampOffset);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("IOException when reading " + logFile.getAbsolutePath() + ". " + e.getMessage());
        }
        catch (NumberFormatException e)
        {
            System.out.println("NumberFormatException when reading " + logFile.getAbsolutePath() + ". " + e.getMessage());
        }
        finally
        {
            if (callback != null)
            {
                callback.onDone();
            }
            if (updateAllSampleSessionDates)
            {
                updateAllSampleSessionDates();
            }
        }
    }

    private T createSample(StringBuilder sampleBuffer, File logFile, LoadLogFileCallback<T> callback)
    {
        T sample = sampleFromLine(sampleBuffer.toString());
        if (callback != null)
        {
            callback.onSampleRead(sample, sampleBuffer.toString());
        }
        if (sample != null)
        {
            sample.setSourceLogFile(logFile);
        }
        return sample;
    }

    /**
     * @param sample          the sample to add to the log.
     * @param timeStampOffset Calculated time stamp offset. Since this value should only be calculated once for each
     *                        processed log file, it is important that the supplied value is only null the first time
     *                        the method is called. Save the method's return value and supply it as the argument's value
     *                        the next time this method is invoked.
     * @return the (possibly updated) timeStampOffset value will be returned, so that
     *         the updated (non-null) value can be used the method is called.
     */
    private Long addSampleAndReturnTimeStampOffset(T sample, Long timeStampOffset)
    {
        if (_normalizeTimeStamps)
        {
            // _normalizeTimeStamps is set and that means that we will fake that all log files start with the same time stamp.

            if (_loadedFiles.size() > 1 && timeStampOffset == null)
            {
                // The log is based on multiple log files and the "time stamp offset" that should be applied to each
                // sample has not yet been calculated. Calculate the offset by subtracting the first recorded timestamp
                // from the current sample's time stamp (this assumes that the current sample is the first time sample
                // in the log file which is being loaded).
                timeStampOffset = samples.get(0).getTimeStamp().getTime() - sample.getTimeStamp().getTime();
            }
            if (timeStampOffset != null && timeStampOffset != 0)
            {
                // Perform the time stamp normalization (offsetting).
                sample.setTimeStamp(new Date(sample.getTimeStamp().getTime() + timeStampOffset));
            }
        }
        addSample(sample);
        return timeStampOffset;
    }

    /**
     * Creates a log sample object based on data from a log file (usually the data is a single line of text).
     *
     * @param data
     * @return
     */
    protected abstract T sampleFromLine(String data);

    /**
     * Returns whether or not the supplied line represents the beginning of a (new) log entry.
     *
     * @param line the line from the log file
     * @return
     */
    protected abstract boolean startsNewSample(String line);

    protected void load(File[] logFiles) throws FileNotFoundException
    {
        load(logFiles, true);
    }

    protected void load(File[] logFiles, boolean updateAllSampleSessionDates) throws FileNotFoundException
    {
        for (File logFile : logFiles)
        {
            load(logFile, updateAllSampleSessionDates);
        }
    }

    public Collection<File> getLoadedFiles()
    {
        return _loadedFiles;
    }

}
