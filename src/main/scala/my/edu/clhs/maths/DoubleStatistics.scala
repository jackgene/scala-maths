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

import scala.math.sqrt

/**
 * Composable statistics of one or more numbers.
 * 
 * @author Jack Leow
 */
class DoubleStatistics private (
    val count: Long, 
    private val sum: Double, 
    private val sumOfSquares: Double) {
  def mean = sum / count
  
  private def sumOfSqDevs = sumOfSquares - sum*sum/count
  
  def sampleVariance =
    if (count > 1)
      sumOfSqDevs / (count-1)
    else
      0.0
  
  def sampleStdev = sqrt(sampleVariance)
  
  def populationVariance = sumOfSqDevs / count
  
  def populationStdev = sqrt(populationVariance)
  
  def +(operand: DoubleStatistics) = new DoubleStatistics(
    count + operand.count,
    sum + operand.sum,
    sumOfSquares + operand.sumOfSquares
  )
  
  override def toString =
    (count, mean, populationStdev).toString
}
/**
 * {@link Statistics} companion object.
 */
object DoubleStatistics {
  def apply(value: Double) = new DoubleStatistics(1, value, value*value)
  
  // TODO more meaningful name?
  def apply(count: Long, mean: Double, populationStdev: Double) = {
    val sum = count * mean
    val sumOfSquares =
      sum*sum / count + populationStdev*populationStdev * count
    new DoubleStatistics(count, count * mean, sumOfSquares)
  }
  
  /**
   * {@link Statistics} Hadoop Writable.
   */
  class Writable extends org.apache.hadoop.io.Writable {
    var get: DoubleStatistics = null
    
    override def write(out: DataOutput) = {
      out.writeLong(get.count)
      out.writeDouble(get.sum)
      out.writeDouble(get.sumOfSquares)
    }
    
    override def readFields(in: DataInput) = {
      get = new DoubleStatistics(
        count = in.readLong(),
        sum = in.readDouble(),
        sumOfSquares = in.readDouble()
      )
    }
    
    override def toString = get.toString
  }
  object Writable {
    def apply(stats: DoubleStatistics) = {
      val writable = new Writable
      
      writable.get = stats
      writable
    }
    
    implicit def stats2Writable(stats: DoubleStatistics) = Writable(stats)
    implicit def writable2Stats(writable: Writable) = writable.get
  }
}