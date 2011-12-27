/*
 * Statistics.scala
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

import java.io.DataInput
import java.io.DataOutput

import scala.math.BigDecimal.double2bigDecimal
import scala.math.BigDecimal.long2bigDecimal
import scala.math.sqrt

/**
 * Composable statistics of one or more numbers.
 * 
 * @author Jack Leow
 */
class BigDecimalStatistics private (
    count: Long, sum: BigDecimal, sumOfSquares: BigDecimal) {
  def count(): Long = count
  
  def sum(): BigDecimal = sum
  
  def sumOfSquares(): BigDecimal = sumOfSquares
  
  def mean = sum / count
  
  private def sumOfSqDevs = sumOfSquares - sum*sum/count
  
  def sampleVariance: BigDecimal =
    if (count > 1)
      sumOfSqDevs / (count-1)
    else
      0.0
  
  def sampleStdev = sqrt(sampleVariance.toDouble)
  
  def populationVariance = sumOfSqDevs / count
  
  def populationStdev = sqrt(populationVariance.toDouble)
  
  def +(operand: BigDecimalStatistics) = new BigDecimalStatistics(
    count + operand.count,
    sum + operand.sum,
    sumOfSquares + operand.sumOfSquares
  )
  
  override def toString =
    (mean, populationStdev, count).toString
}
/**
 * {@link Statistics} componion object.
 */
object BigDecimalStatistics {
  def apply(value: BigDecimal) = new BigDecimalStatistics(1, value, value*value)
  
  def apply(count: Long, mean: BigDecimal, populationStdev: Double) = {
    val sum = count * mean
    val sumOfSquares =
      sum*sum / count + populationStdev*populationStdev * count
    new BigDecimalStatistics(count, count * mean, sumOfSquares)
  }
  
  /**
   * {@link Statistics} Hadoop Writable.
   */
  class Writable extends org.apache.hadoop.io.Writable {
    var get: BigDecimalStatistics = null
    
    override def write(out: DataOutput) = {
      out.writeLong(get.count)
      out.writeUTF(get.sum.toString)
      out.writeUTF(get.sumOfSquares.toString)
    }
    
    override def readFields(in: DataInput) = {
      get = new BigDecimalStatistics(
        count = in.readLong(),
        sum = BigDecimal(in.readUTF()),
        sumOfSquares = BigDecimal(in.readUTF())
      )
    }
    
    override def toString = get.toString
  }
  object Writable {
    def apply(stats: BigDecimalStatistics) = {
      val writable = new Writable
      
      writable.get = stats
      writable
    }
    
    implicit def stats2Writable(stats: BigDecimalStatistics) = Writable(stats)
    implicit def writable2Stats(writable: Writable) = writable.get
  }
}