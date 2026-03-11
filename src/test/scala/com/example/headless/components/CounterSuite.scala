package com.example.headless.components

import munit.FunSuite

class CounterSuite extends FunSuite {

  test("default initial value is 0") {
    val counter = new Counter()
    assertEquals(counter.count.now(), 0)
  }

  test("custom initial value is respected") {
    val counter = new Counter(42)
    assertEquals(counter.count.now(), 42)
  }

  test("increment increases count by 1") {
    val counter = new Counter()
    counter.increment()
    assertEquals(counter.count.now(), 1)
  }

  test("multiple increments accumulate") {
    val counter = new Counter()
    counter.increment()
    counter.increment()
    counter.increment()
    assertEquals(counter.count.now(), 3)
  }

  test("increment from custom initial value") {
    val counter = new Counter(10)
    counter.increment()
    assertEquals(counter.count.now(), 11)
  }
}
