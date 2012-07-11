/*
 * CalculateStatisticsJob.scala
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

import scala.collection.JavaConversions.iterableAsScalaIterable

import org.apache.hadoop.conf.Configured
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.util.GenericOptionsParser
import org.apache.hadoop.util.Tool
import org.apache.hadoop.util.ToolRunner

import my.edu.clhs.maths._

/**
 * Simple job that calculates the statistics (currently count, mean
 * and standard deviation) of a list of numbers.
 * 
 * @author Jack Leow
 */
class CalculateStatisticsJob extends Configured with Tool {
  override def run(args: Array[String]): Int = {
    val optsParser = new GenericOptionsParser(args)
    
    val (inputPath, outputPath) = optsParser.getRemainingArgs match {
      case Array(inputPath, outputPath) =>
        (inputPath, outputPath)
      case _ =>
        throw new IllegalArgumentException("Usage: job input_path output_path")
    }
    
    val job = new Job(
      optsParser.getConfiguration(),
      "scala-maths CalculateStatisticsJob"
    )
    job.setJarByClass(getClass())
    
    job.setMapperClass(classOf[CalculateStatisticsJob.CalcStatsMapper])
    job.setCombinerClass(classOf[CalculateStatisticsJob.CalcStatsReducer])
    job.setReducerClass(classOf[CalculateStatisticsJob.CalcStatsReducer])
    
    job.setOutputKeyClass(classOf[NullWritable])
    job.setOutputValueClass(classOf[DoubleStatistics.Writable])
    
    FileInputFormat.addInputPath(job, new Path(inputPath))
    FileOutputFormat.setOutputPath(job, new Path(outputPath))
    
    job.waitForCompletion(false)
    return 0
  }
}
object CalculateStatisticsJob {
  class CalcStatsMapper
      extends Mapper[LongWritable,Text,NullWritable,DoubleStatistics.Writable] {
    override def map(
        offSet: LongWritable, value: Text,
        context: Mapper[LongWritable,Text,NullWritable,DoubleStatistics.Writable]#Context
        ) = {
      context.write(
        NullWritable.get(), Statistics(value.toString.toDouble))
    }
  }
  
  class CalcStatsReducer
      extends Reducer[NullWritable,DoubleStatistics.Writable,NullWritable,DoubleStatistics.Writable] {
    override def reduce(
        nullKey: NullWritable, allStats: java.lang.Iterable[DoubleStatistics.Writable],
        context: Reducer[NullWritable,DoubleStatistics.Writable,NullWritable,DoubleStatistics.Writable]#Context
        ) = {
      context.write(nullKey, allStats.map(_.get).reduce(_ + _))
    }
  }
  
  def main(args : Array[String]) : Unit =
    ToolRunner.run(new CalculateStatisticsJob(), args)
}