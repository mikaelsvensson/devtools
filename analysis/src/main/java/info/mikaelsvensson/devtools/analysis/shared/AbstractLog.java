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
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AbstractLog<T extends AbstractSample> {
    private static final int MILLISECONDS_IN_MINUTE = 60 * 1000;
    protected List<T> samples = new ArrayList<T>();
    protected Date firstSampleDate = null;
    protected Date lastSampleDate = null;
    protected int minutesBetweenSessions;

    public AbstractLog()
    {
    }

    public AbstractLog(int minutesBetweenSessions)
    {
        this.minutesBetweenSessions = minutesBetweenSessions;
    }

    public static <T extends AbstractSample> int count(Collection<T> samples, SampleFilter filter) {
        int count = 0;
        for (T sample : samples) {
            if (filter == null || filter.accepts(sample)) {
                count++;
            }
        }
        return count;
    }

    public static <T extends AbstractSample> int calculateMax(Collection<T> samples) {
        int max = 0;
        for (T sample : samples) {
            max = sample.getResponseTime() > max ? sample.getResponseTime() : max;
        }
        return max;
    }

    public static <T extends AbstractSample> Date getLatestDate(Collection<T> samples) {
        Date max = null;
        for (T sample : samples) {
            max = max == null || sample.getTimeStamp().after(max) ? sample.getTimeStamp() : max;
        }
        return max;
    }
    public static <T extends AbstractSample> Date getEarliestDate(Collection<T> samples) {
        Date min = null;
        for (T sample : samples) {
            min = min == null || sample.getTimeStamp().before(min) ? sample.getTimeStamp() : min;
        }
        return min;
    }

    public static <T extends AbstractSample> int calculateMin(Collection<T> samples) {
        int min = Integer.MAX_VALUE;
        for (T sample : samples) {
            min = sample.getResponseTime() < min ? sample.getResponseTime() : min;
        }
        return min;
    }

    public static <T extends AbstractSample> int calculateAverage(Collection<T> samples) {
        return Math.round(calculateSum(samples) / samples.size());
    }

    public static <T extends AbstractSample> long calculateSum(Collection<T> samples) {
        long sum = 0;
        for (T sample : samples) {
            sum += sample.getResponseTime();
        }
        return sum;
    }

    public static <T extends AbstractSample> double[] toDurationDoubles(Collection<T> samples) {
        double[] responseTimes = new double[samples.size()];
        int x = 0;
        for (T sample : samples) {
            responseTimes[x++] = sample.getResponseTime();
        }
        return responseTimes;
    }

    public Date getFirstSampleDate() {
        return firstSampleDate;
    }

    public Date getLastSampleDate() {
        return lastSampleDate;
    }

    public Collection<T> getSamples() {
        return samples;
    }

    public Map<String, Collection<T>> getSamplesBy(SampleCollector sampleCollector) {
        return groupSamples(sampleCollector, samples);
    }

    protected Map<String, Collection<T>> getSamplesBy(SampleGrouper sampleGrouper, SampleFilter sampleFilter) {
        return groupSamples(sampleGrouper, sampleFilter, samples);
    }

    public static <X extends AbstractSample> Map<String, Collection<X>> groupSamples(SampleCollector sampleCollector, Collection<X> samples) {
        return groupSamples(sampleCollector, sampleCollector, samples);
    }

    private static <X extends AbstractSample> Map<String, Collection<X>> groupSamples(SampleGrouper sampleGrouper, SampleFilter sampleFilter, Collection<X> samples)
    {
        Map<String, Collection<X>> map = new TreeMap<String, Collection<X>>();
        for (X sample : samples) {
            String groupName = sampleGrouper.getGroupName(sample);
            if (sampleFilter == null || sampleFilter.accepts(sample)) {
                if (!map.containsKey(groupName)) {
                    map.put(groupName, new LinkedList<X>());
                }
                map.get(groupName).add(sample);
            }
        }
        return map;
    }

    public double[] getSampleDurations() {
        return toDurationDoubles(samples);
    }

    public Percentile getPercentileCalculator() {
        return getPercentileCalculator(samples);
    }

    public Percentile getPercentileCalculator(Collection<T> samples) {
        double[] responseTimes = toDurationDoubles(samples);
        Arrays.sort(responseTimes);
        Percentile percentile = new Percentile();
        percentile.setData(responseTimes);
        return percentile;
    }

    protected Date parseDate(String value, SimpleDateFormat simpleDateFormat) {
        Date date = null;
        if (StringUtils.isNotEmpty(value)) {
            try {
                date = simpleDateFormat.parse(value);
            } catch (ParseException e) {
            }
        }
        return date;
    }

    public void sortSamples(Comparator<T> comparator) {
        Collections.sort(samples, comparator);
    }

    protected void sortSamplesByTimeStamp() {
        Collections.sort(samples, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                final boolean isTimeStampSetForBothObjects = o1 != null && o1.getTimeStamp() != null && o2 != null && o2.getTimeStamp() != null;
                return isTimeStampSetForBothObjects ? o1.getTimeStamp().compareTo(o2.getTimeStamp()) : 0;
//                return o1.getTimeStamp().compareTo(o2.getTimeStamp());
            }
        });
    }

    protected void addSample(T sample)
    {
        samples.add(sample);
        Date timeStamp = sample.getTimeStamp();
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
    }

    protected void updateAllSampleSessionDates()
    {
        sortSamplesByTimeStamp();
        Date currentSessionDate = null;
        Date lastTime = null;
        for (T sample : samples)
        {
            Date currentDate = sample.getTimeStamp();
            if (lastTime == null || currentDate.getTime() - lastTime.getTime() > minutesBetweenSessions * MILLISECONDS_IN_MINUTE)
            {
                currentSessionDate = currentDate;
            }
            sample.setSessionDate(currentSessionDate);
            lastTime = currentDate;
        }
    }
}
