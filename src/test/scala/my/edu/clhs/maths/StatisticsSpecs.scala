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
class StatisticsSpecs
    extends WordSpec with MustMatchersForJUnit {
  "A Statistics instance" when {
    "composed of a single value" must {
      val stats = Statistics(42)
      
      "have a mean of that value" is (pending)
      
      "have a population variance of zero" is (pending)
      
      "have a population standard deviation of zero" is (pending)
      
      "have a sample variance of zero" is (pending)
      
      "have a sample standard deviation of zero" is (pending)
      
      "have a count of one" is (pending)
    }
    
    "composed of two identical values" must {
      val stats = Statistics(42) + Statistics(42)
      
      "have a mean of that value" is (pending)
      
      "have a population variance of zero" is (pending)
      
      "have a population standard deviation of zero" is (pending)
      
      "have a sample variance of zero" is (pending)
      
      "have a sample standard deviation of zero" is (pending)
      
      "have a count of two" is (pending)
    }
    
    "composed of the values 0 and 8" must {
      val stats = Statistics(42) + Statistics(42)
      
      "have a mean of 4" is (pending)
      
      "have a population variance of 16" is (pending)
      
      "have a population standard deviation of 4" is (pending)
      
      "have a sample variance of 32" is (pending)
      
      "have a sample standard deviation of 5.656..." is (pending)
      
      "have a count of two" is (pending)
    }
    
    "composed of the values 1, 1, 2, 3, 5 and 8" must {
      val stats = List(1,1,2,3,5,8).map(Statistics(_)).reduce(_+_)
      
      "have a mean of 3.333..." is (pending)
      
      "have a population variance of 6.222..." is (pending)
      
      "have a population standard deviation of 2.494..." is (pending)
      
      "have a sample variance of 7.466..." is (pending)
      
      "have a sample standard deviation of 2.732..." is (pending)
      
      "have a count of 6" is (pending)
    }
  }
}