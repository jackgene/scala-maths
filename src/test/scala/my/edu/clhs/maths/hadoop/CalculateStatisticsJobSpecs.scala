/*
 * CalculateStatisticsJobSpecs.scala
 *
 * Copyright 2012 Jack Leow
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package my.edu.clhs.maths.hadoop

import java.io.File

import scala.io.Source

import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.junit.MustMatchersForJUnit

/**
 * {@link CalculateStatisticsJobSpecs} specifications.
 * 
 * @author Jack Leow
 */
@RunWith(classOf[JUnitRunner])
class CalculateStatisticsJobSpecs extends WordSpec with MustMatchersForJUnit {
  val suiteBaseDir = getClass.getResource(getClass.getSimpleName).toURI.getPath
  
  private def recursiveDelete(dir: File): Unit = {
    if (dir.exists()) {
      if (dir.isDirectory()) {
        val dirContents = dir.listFiles()
        dirContents.foreach(recursiveDelete(_))
      }
      dir.delete()
    }
  }
  
  "The CalculateStatisticsJob" must {
    val job = new CalculateStatisticsJob
    
    "calculate the statitics of a properly formatted data file" in {
      // Set up
      val OUTPUT_DIR = "build/integration-test/output"
      recursiveDelete(new File(OUTPUT_DIR))
      
      // Test
      job.run(Array(suiteBaseDir + "/input", OUTPUT_DIR))
      
      // Verify
      val expected = "(6,3.3333333333333335,2.494438257849294)"
      val actual = Source.fromFile(OUTPUT_DIR + "/part-r-00000").
        getLines.toIndexedSeq(0)
      (actual) must equal (expected)
    }
  }
}