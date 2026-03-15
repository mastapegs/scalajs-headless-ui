package com.example.headless.components

import munit.FunSuite

class TableSuite extends FunSuite {

  test("stores headers") {
    val t = Table(List("A", "B", "C"), Nil)
    assertEquals(t.headers, List("A", "B", "C"))
  }

  test("stores rows") {
    val rows = List(List("1", "2"), List("3", "4"))
    val t    = Table(List("X", "Y"), rows)
    assertEquals(t.rows, rows)
  }

  test("empty table has no rows") {
    val t = Table(List("Col"), Nil)
    assertEquals(t.rows, Nil)
  }

  test("different tables have independent values") {
    val t1 = Table(List("A"), List(List("1")))
    val t2 = Table(List("B"), List(List("2")))
    assertEquals(t1.headers, List("A"))
    assertEquals(t2.headers, List("B"))
    assertNotEquals(t1, t2)
  }
}
