package com.example.headless.components

import com.raquo.laminar.api.L._

/** Headless counter component: pure state and logic, no rendering. */
final class Counter(val label: String = "Counter", initialValue: Int = 0) {

  private val countVar: Var[Int] = Var(initialValue)

  val count: Signal[Int] = countVar.signal

  def increment(): Unit =
    countVar.update(_ + 1)
}
