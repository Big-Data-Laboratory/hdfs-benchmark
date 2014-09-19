package benchmarktest

import java.util.concurrent.atomic.AtomicInteger
import java.util.{Timer, TimerTask}

import com.google.common.base.Stopwatch
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

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
class HDFSFileAppender(val bufferSize: Int, val timeBetweenFlushes: Long, val path: String,
                       val total: Int) {
  val buffer = new Array[Byte](bufferSize)
  new Random().nextBytes(buffer)
  val dfs = new Path(path).getFileSystem(new Configuration())

  val stopWatch = new Stopwatch()
  val timer = new Timer()
  var countInBatch = 0
  var last = 0l

  def appendEvents(i: Int): Unit = {
    val outputStream = dfs.create(new Path(path + "-" + i))
    val bufferOfTimes = new ArrayBuffer[Int]()
    last = System.currentTimeMillis()
    (1 to 5).foreach {_ =>
      (1 to total/5).foreach(x => {
        val current = System.currentTimeMillis()
        if (current - last > timeBetweenFlushes) {
          stopWatch.reset()
          stopWatch.start()
          outputStream.hflush()
          stopWatch.stop()
          last = current
          buffer ++ bufferOfTimes
          countInBatch = 0
        }
        outputStream.write(buffer)
        countInBatch += 1
      })
      Thread.sleep(2500)
    }
    println("Writes for stream " + i + ": " + bufferOfTimes.mkString)
    outputStream.close()
  }
}
