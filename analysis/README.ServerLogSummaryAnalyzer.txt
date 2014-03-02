About
-----

  Utility for summarizing the JBoss server log file based on message categories and priorities.

  The generated report will contain information about how many times the different Log4J categories are used, and for
  which priorities/severities. Lets take a look at an example:


      Grouped And Sorted By Category
      ------------------------------
      Category                                                            Priority        # total   # exceptions
      STDOUT                                                                  INFO             64              0
      javax.enterprise.resource.webcontainer.jsf.config                    WARNING              3              0
      org.jboss.aspects.tx.TxPolicy                                          ERROR            554            534
      org.jboss.bootstrap.microcontainer.ServerImpl                           INFO              1              0
      ...

  The example shows that 64 log entries written directly to "standard out". Furthermore, the category
  "org.jboss.aspects.tx.TxPolicy" has been used 554 times and 534 of those messages seem to mention some kind of
  exception class (a qualified Java class name ending in "Exception").


Usage
-----
  java info.mikaelsvensson.devtools.analysis.serverlog.ServerLogSummaryAnalyzer -f <input log files> -rfn <output report file path>

  ARGUMENTS:
  -f,--files <input log files>                      list of log files to analyze
  -rfn,--report-file-name <output report file path> file name for the report

  Example (assuming utility has been compiled using instructions below):

  analysis> java -cp target/classes;target/dependency/* \
      info.mikaelsvensson.devtools.analysis.serverlog.ServerLogSummaryAnalyzer \
      -f <jboss_home>/log/server.log \
      -rfn report.txt

Compiling
---------

  analysis> mvn clean install dependency:copy-dependencies
