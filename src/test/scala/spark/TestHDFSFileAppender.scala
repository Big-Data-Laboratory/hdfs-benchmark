package spark

import benchmarktest.HDFSFileAppender
import org.junit.Test

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class TestHDFSFileAppender {

  @Test
  def test(): Unit = {
    val bufferSize = System.getProperty("bufferSize", "4096")
    val timeBetweenFlushes = System.getProperty("flushInterval", "200")
    val path = Option(System.getProperty("path"))
      .getOrElse(
        throw new RuntimeException("Must specify path")
      )
    val total = System.getProperty("total", "100000")
    val appender = new HDFSFileAppender(bufferSize.toInt, timeBetweenFlushes.toLong, path,
      total.toInt)
    val average = (1 to 50).map(appender.appendEvents).sum / 50
    println("Average write rate over all 50 streams: " + average)
  }

}
