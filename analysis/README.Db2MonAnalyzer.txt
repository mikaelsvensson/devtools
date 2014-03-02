About
-----

Tool explicitly designed to aid in the analysis of DB2 statistics information acquired by repeatedly exporting
information from views/functions such as MON_GET_TABLE to text files. The name of each such text file must contain a
timestamp of some sort. All lines of all input files must have the same number of columns.

The timestamp for each sample is extracted from the name of the source file column using the MessageFormat pattern
specified using the date-format argument.

The output will show the differences between the different sessions (i.e. the different source files).

usage: java info.mikaelsvensson.devtools.analysis.db2.Db2MonAnalyzer -cn <list> -df <pattern> -f <input log files> 
       -idcn <list> [-ocn <list>] -rfn <output report file path> [-rfp <regex>]

ARGUMENTS:
-cn,--all-column-names <list>                     list of all column names in source files
-df,--date-format <pattern>                       MessageFormat pattern used for extracting timestamp from date column
-f,--files <input log files>                      list of log files to analyze
-idcn,--id-column-names <list>                    list of columns to identify rows (use same column names as specified
                                                  by all-column-names option)
-ocn,--output-column-names <list>                 list of columns to use in report (use same column names as specified
                                                  by all-column-names option)
-rfn,--report-file-name <output report file path> file name for the report
-rfp,--rowid-filter-pattern <regex>               regular expression used to filter which lines to include in reports


Compiling
---------

  analysis$ mvn clean install dependency:copy-dependencies


Running
-------
Check out run-sqlandsoapanalyzer.bat for some ideas =)

  java [classpath] info.mikaelsvensson.devtools.analysis.db2.Db2MonAnalyzer \
    -rfn {parentPath}\db2-mon_get_table-report.txt \
    -df db2-mon_get_table-{0,date,yyyyMMdd-HHmm}.csv \
    -rfp SCHEMA \
    -f db2-mon_get_table-* \
    -idcn SCHEMA TABLENAME \
    -ocn TABLE_SCANS ROWS_READ ROWS_INSERTED ROWS_UPDATED ROWS_DELETED \
    -cn SCHEMA TABLENAME MEMBER TAB_TYPE TABLE_SCANS ROWS_READ ROWS_INSERTED ROWS_UPDATED ROWS_DELETED OVERFL_ACCESS OVERFL_CREATES PAGE_REORGS

The above example assumes that the input files (db2-mon_get_table-*) look something like this:

    "SCHEMA";"TABLE1";0;"USER_TABLE";0;60;0;0;0;0;0;0
    "SCHEMA";"TABLE2";0;"USER_TABLE";0;1;0;0;0;0;0;0
    "SCHEMA";"TABLE3";0;"USER_TABLE";0;16;0;0;0;0;0;0
    ...
    "SCHEMA";"TABLE1";0;"USER_TABLE";0;60;0;0;0;0;0;0
    "SCHEMA";"TABLE2";0;"USER_TABLE";0;149375;0;0;0;0;0;0
    "SCHEMA";"TABLE3";0;"USER_TABLE";0;16;0;0;0;0;0;0
    ...
    "SCHEMA";"TABLE1";0;"USER_TABLE";0;60;0;0;0;0;0;0
    "SCHEMA";"TABLE2";0;"USER_TABLE";0;149375;0;0;0;0;0;0
    "SCHEMA";"TABLE3";0;"USER_TABLE";0;16;0;0;0;0;0;0
    ...
    "SCHEMA";"TABLE1";0;"USER_TABLE";0;60;0;0;0;0;0;0
    "SCHEMA";"TABLE2";0;"USER_TABLE";0;1301410;0;0;0;0;0;0
    "SCHEMA";"TABLE3";0;"USER_TABLE";0;16;0;0;0;0;0;0

The output (Db2MonAnalyzer.Report.txt) will then look something like this:

    Difference between 2014-02-17 18:16 and 2014-02-17 18:21
    --------------------------------------------------------
    SCHEMA TABLENAME     TABLE_SCANS      ROWS_READ  ROWS_INSERTED   ROWS_UPDATED   ROWS_DELETED
    SCHEMA TABLE2                  0         149374              0              0              0
    SCHEMA TABLE5                  0           1038              0              0              0
    SCHEMA TABLE4                  0              6              3              3              0
    ...

    Difference between 2014-02-17 18:25 and 2014-02-17 19:54
    --------------------------------------------------------
    SCHEMA TABLENAME     TABLE_SCANS      ROWS_READ  ROWS_INSERTED   ROWS_UPDATED   ROWS_DELETED
    SCHEMA TABLE2                  0        1152035              0              0              0
    SCHEMA TABLE5                  0           4709              0              0              0
    SCHEMA TABLE4                  0             36             18             18              0
    ...