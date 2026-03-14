package com.example.headless.components

import com.example.headless.SignalHelpers
import munit.FunSuite

class CounterSuite extends FunSuite with SignalHelpers {

  test("default label is Counter") {
    val counter = new Counter()
    assertEquals(counter.label, "Counter")
  }

  test("custom label is respected") {
    val counter = new Counter(label = "Custom")
    assertEquals(counter.label, "Custom")
  }

  test("default initial value is 0") {
    val counter = new Counter()
    assertEquals(signalNow(counter.count), 0)
  }

  test("custom initial value is respected") {
    val counter = new Counter(initialValue = 42)
    assertEquals(signalNow(counter.count), 42)
  }

  test("increment increases count by 1") {
    val counter = new Counter()
    counter.increment()
    assertEquals(signalNow(counter.count), 1)
  }

  test("multiple increments accumulate") {
    val counter = new Counter()
    counter.increment()
    counter.increment()
    counter.increment()
    assertEquals(signalNow(counter.count), 3)
  }

  test("increment from custom initial value") {
    val counter = new Counter(initialValue = 10)
    counter.increment()
    assertEquals(signalNow(counter.count), 11)
  }
}
