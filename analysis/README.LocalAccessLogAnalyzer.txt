About
-----

Counts the number of different http status codes in the http access log, e.g. localhost_access_log.log. The result is
exported as both a plain-text report and a pie chart.

usage: java info.mikaelsvensson.devtools.analysis.localaccesslog.LocalAccessLogAnalyzer -f <input log files>
       -rfn <output report file path>

ARGUMENTS:
-f,--files <input log files>                      list of log files to analyze
-rfn,--report-file-name <output report file path> file name for the report


Compiling
---------

analysis$ mvn clean install dependency:copy-dependencies


Running
-------

Usage example:

    java info.mikaelsvensson.devtools.analysis.localaccesslog.LocalAccessLogAnalyzer \
        -rfn LocalAccessLogAnalyzer.Report.{4}.txt \
        -f jboss-eap-5.1\jboss-as\server\default\log\local*

Resulting files:

    The report:
    LocalAccessLogAnalyzer.Report.log.txt

    A pie chart for each session mentioned in the report:
    LocalAccessLogAnalyzer.Report.log.txt.2014-01-22-0806.png
    LocalAccessLogAnalyzer.Report.log.txt.2014-01-22-1033.png
    ...

    Note how {4} in the "rfn" argument value is replaced by the name of the log file's folder.