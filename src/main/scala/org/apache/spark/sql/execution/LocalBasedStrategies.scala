package org.apache.spark.sql.execution

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.optimizer.StarryLocalRelationReplace

/**
  * Created by passionke on 2018/6/27.
  * 紫微无姓，红尘留行，扁舟越沧溟，何须山高龙自灵。
  * 一朝鹏程，快意风云，挥手功名
  */
object LocalBasedStrategies {

  def register(sparkSession: SparkSession): Unit = {
    sparkSession.experimental.extraStrategies = Seq(
      StarryAggStrategy(),
      StarryJoinLocalStrategy(sparkSession.sessionState.conf),
      StarryUnionLocalStrategy(),
      StarryLimitLocalStrategy(),
      StarryLocalTableScanStrategies()
    ) ++: sparkSession.experimental.extraStrategies

    sparkSession.experimental.extraOptimizations = Seq(
      StarryLocalRelationReplace
    )
  }

  def unRegister(sparkSession: SparkSession): Unit = {
    sparkSession.experimental.extraStrategies =
      sparkSession.experimental.extraStrategies
        .filter(strategy => !strategy.isInstanceOf[StarryJoinLocalStrategy])
        .filter(strategy => !strategy.isInstanceOf[StarryUnionLocalStrategy])
        .filter(strategy => !strategy.isInstanceOf[StarryLimitLocalStrategy])
        .filter(strategy => !strategy.isInstanceOf[StarryAggStrategy])
        .filter(strategy => !strategy.isInstanceOf[StarryLocalTableScanStrategies])

    sparkSession.experimental.extraOptimizations = Seq()
  }

}
