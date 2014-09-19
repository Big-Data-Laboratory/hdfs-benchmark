hdfs-benchmark
==============
Tool to benchmark time taken for hflushes to HDFS. It writes to 50 different files. 
You must have write permissions to the directory which you are writing to. 
The path is really a prefix -1...-n is appended to that path while writing.

To run: mvn test -Dpath=hdfs://xy.example.com/data/op -DbufferSize=1024 -Dtotal=100000 

Time in between flushes defaults to 200ms, which can be set using -DflushInterval=500 (in millis). 
Buffer size is in bytes, total is the total number of events per file. 
The above command will create files op-1...op-50 in the /data directory.
