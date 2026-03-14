package com.example.headless.components

import com.raquo.airstream.core.Signal
import com.raquo.airstream.ownership.ManualOwner
import munit.FunSuite

class ProgressSuite extends FunSuite {

  private def signalNow[A](signal: Signal[A]): A = {
    val owner = new ManualOwner
    var value = Option.empty[A]
    signal.foreach(v => value = Some(v))(owner)
    owner.killSubscriptions()
    value.get
  }

  test("initial value defaults to 0") {
    val p = new Progress("Test")
    assertEquals(signalNow(p.value), 0)
  }

  test("percentage is computed correctly") {
    val p = new Progress("Test", max = 200, initialValue = 100)
    assertEquals(signalNow(p.percentage), 50)
  }

  test("increment increases value") {
    val p = new Progress("Test")
    p.increment(25)
    assertEquals(signalNow(p.value), 25)
  }

  test("increment does not exceed max") {
    val p = new Progress("Test", max = 100, initialValue = 95)
    p.increment(10)
    assertEquals(signalNow(p.value), 100)
  }

  test("setValue clamps to bounds") {
    val p = new Progress("Test", min = 10, max = 50)
    p.setValue(100)
    assertEquals(signalNow(p.value), 50)
    p.setValue(-5)
    assertEquals(signalNow(p.value), 10)
  }

  test("isComplete returns true at max") {
    val p = new Progress("Test", max = 100, initialValue = 100)
    assertEquals(signalNow(p.isComplete), true)
  }

  test("reset sets value to min") {
    val p = new Progress("Test", min = 10, initialValue = 50)
    p.reset()
    assertEquals(signalNow(p.value), 10)
  }
}
