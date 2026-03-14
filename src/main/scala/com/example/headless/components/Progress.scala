package com.example.headless.components

import com.raquo.laminar.api.L._

final class Progress(val label: String, val min: Int = 0, val max: Int = 100, initialValue: Int = 0) {

  private val valueVar: Var[Int] = Var(initialValue.max(min).min(max))

  val value: Signal[Int] = valueVar.signal

  val percentage: Signal[Int] = value.map(v => ((v - min).toDouble / (max - min) * 100).toInt)

  val isComplete: Signal[Boolean] = value.map(_ >= max)

  def setValue(v: Int): Unit =
    valueVar.set(v.max(min).min(max))

  def increment(amount: Int = 1): Unit =
    valueVar.update(v => (v + amount).min(max))

  def reset(): Unit =
    valueVar.set(min)
}
