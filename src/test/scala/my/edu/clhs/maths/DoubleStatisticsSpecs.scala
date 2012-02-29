/*
 * StatisticsSpecs.scala
 *
 * Copyright 2011 Jack Leow
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
package my.edu.clhs.maths

import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.junit.MustMatchersForJUnit

/**
 * {@link Statistics} specifications.
 * 
 * @author Jack Leow
 */
@RunWith(classOf[JUnitRunner])
class DoubleStatisticsSpecs
    extends WordSpec with MustMatchersForJUnit {
  "A DoubleStatistics instance" when {
    "composed of a single value" must {
      val stats = DoubleStatistics(42)
      
      "have a mean of that value" in {
        stats.mean must equal (42)
      }
      
      "have a population variance of zero" in {
        stats.populationVariance must equal (0)
      }
      
      "have a population standard deviation of zero" in {
        stats.populationStdev must equal (0)
      }
      
      "have a sample variance of zero" in {
        stats.sampleVariance must equal (0)
      }
      
      "have a sample standard deviation of zero" in {
        stats.sampleStdev must equal (0)
      }
      
      "have a count of one" in {
        stats.count must equal (1)
      }
    }
    
    "composed of two identical values" must {
      val stats = DoubleStatistics(42) + DoubleStatistics(42)
      
      "have a mean of that value" in {
        stats.mean must equal (42)
      }
      
      "have a population variance of zero" in {
        stats.populationVariance must equal (0)
      }
      
      "have a population standard deviation of zero" in {
        stats.populationStdev must equal (0)
      }
      
      "have a sample variance of zero" in {
        stats.sampleVariance must equal (0)
      }
      
      "have a sample standard deviation of zero" in {
        stats.sampleStdev must equal (0)
      }
      
      "have a count of two" in {
        stats.count must equal (2)
      }
    }
    
    "composed of the values 0 and 8" must {
      val stats = DoubleStatistics(0) + DoubleStatistics(8)
      
      "have a mean of 4" in {
        stats.mean must equal (4)
      }
      
      "have a population variance of 16" in {
        stats.populationVariance must equal (16)
      }
      
      "have a population standard deviation of 4" in {
        stats.populationStdev must equal (4)
      }
      
      "have a sample variance of 32" in {
        stats.sampleVariance must equal (32)
      }
      
      "have a sample standard deviation of 5.656854" in {
        stats.sampleStdev must be (5.656854 plusOrMinus 0.000001)
      }
      
      "have a count of two" in {
        stats.count must equal (2)
      }
    }
    
    "composed of the values 1, 1, 2, 3, 5 and 8" must {
      val stats = List(1,1,2,3,5,8).map(DoubleStatistics(_)).reduce(_+_)
      
      "have a mean of 3.333333" in {
        stats.mean must be (3.333333 plusOrMinus 0.000001)
      }
      
      "have a population variance of 6.222222" in {
        stats.populationVariance must be (6.222222 plusOrMinus 0.000001)
      }
      
      "have a population standard deviation of 2.494438" in {
        stats.populationStdev must be (2.494438 plusOrMinus 0.000001)
      }
      
      "have a sample variance of 7.466667" in {
        stats.sampleVariance must be (7.466667 plusOrMinus 0.000001)
      }
      
      "have a sample standard deviation of 2.732520" in {
        stats.sampleStdev must be (2.732520 plusOrMinus 0.000001)
      }
      
      "have a count of 6" in {
        stats.count must equal (6)
      }
    }
  }
}