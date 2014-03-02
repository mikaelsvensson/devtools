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

import info.mikaelsvensson.devtools.analysis.shared.ReportGenerator;
import info.mikaelsvensson.devtools.analysis.shared.SampleCollector;
import info.mikaelsvensson.devtools.analysis.shared.ToStringUtil;
import info.mikaelsvensson.devtools.analysis.shared.reportprinter.ReportPrinter;
import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public class LocalAccessLogReportGenerator implements ReportGenerator
{
    public static final SampleCollector SAMPLE_COLLECTOR = new SampleCollector<LocalAccessLogSample>()
    {
        @Override
        public String getGroupName(LocalAccessLogSample sample)
        {
            return Integer.toString(sample.getStatusCode());
        }
    };
    private static final int NUMBER_OF_REQUESTS_IN_SHORT_TEST = 100;

    private final LocalAccessLog _log;

    public LocalAccessLogReportGenerator(LocalAccessLog log)
    {
        _log = log;
    }

    @Override
    public void generateReport(File outputFile, ReportPrinter reportPrinter) throws FileNotFoundException
    {
        final PrintStream ps = new PrintStream(outputFile);
        final Collection<LocalAccessLogSample> allSamples = _log.getSamples();
        Map<String, Collection<LocalAccessLogSample>> samplesByTestSession = SampleCollector.COLLECTOR_BY_SESSION_DATE.getFilteredAndGrouped(allSamples);
        for (Map.Entry<String, Collection<LocalAccessLogSample>> sessionEntry : samplesByTestSession.entrySet())
        {
            final Collection<LocalAccessLogSample> sessionSamples = sessionEntry.getValue();
            Map<String, Collection<LocalAccessLogSample>> samples = SAMPLE_COLLECTOR.getFilteredAndGrouped(sessionSamples);
            String[][] data = new String[samples.size() + 1][];
            int i = 0;
            int sumCount = 0;
            final DefaultPieDataset dataset = new DefaultPieDataset();
            final JFreeChart chart = ChartFactory.createPieChart("Status Codes For Session " + sessionEntry.getKey(), dataset, true, false, Locale.ENGLISH);
            final File chartFile = new File(outputFile.getAbsolutePath() + "." + StringUtils.remove(sessionEntry.getKey(), ':').replace(' ', '-') + ".png");
            final PiePlot plot = (PiePlot)chart.getPlot();
            for (Map.Entry<String, Collection<LocalAccessLogSample>> entry : samples.entrySet())
            {
                final Collection<LocalAccessLogSample> responseCodeSamples = entry.getValue();
                final int count = responseCodeSamples.size();
                data[i++] = new String[]{
                        entry.getKey(),
                        ToStringUtil.toString(count),
                        ToStringUtil.toString(_log.calculateAverage(responseCodeSamples)),
                        ToStringUtil.toString(_log.calculateMin(responseCodeSamples)),
                        ToStringUtil.toString(_log.calculateMax(responseCodeSamples))
                };
                sumCount += count;

                final String label = entry.getKey() + " (" + count + " reqs)";
                dataset.setValue(label, count);
                plot.setSectionPaint(label, entry.getKey().equals("200") ? Color.GREEN : Color.RED);
            }
            data[i] = new String[]{
                    "All",
                    ToStringUtil.toString(sumCount),
                    ToStringUtil.toString(_log.calculateAverage(sessionSamples)),
                    ToStringUtil.toString(_log.calculateMin(sessionSamples)),
                    ToStringUtil.toString(_log.calculateMax(sessionSamples))};

            reportPrinter.printTable(ps,
                    sessionEntry.getKey(),
                    10,
                    new String[]{"Status Code", "# Requests", "Avg [ms]", "Min [ms]", "Max [ms]"},
                    data,
                    null);

            if (sumCount > NUMBER_OF_REQUESTS_IN_SHORT_TEST)
            {
                try
                {
                    ChartUtilities.saveChartAsPNG(chartFile, chart, 500, 500);
                }
                catch (IOException e)
                {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        ps.close();
    }

    @Override
    public void generateSamplesCSV(File file) throws FileNotFoundException
    {
        throw new UnsupportedOperationException("CSV reports not supported by " + getClass().getSimpleName());
    }
}
