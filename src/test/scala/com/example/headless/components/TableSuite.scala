package com.example.headless.components

import munit.FunSuite

class TableSuite extends FunSuite {

  test("stores headers") {
    val td = TableData(List("A", "B", "C"), Nil)
    val t  = Table(td)
    assertEquals(t.data.headers, List("A", "B", "C"))
  }

  test("stores rows") {
    val rows = List(List("1", "2"), List("3", "4"))
    val t    = Table(TableData(List("X", "Y"), rows))
    assertEquals(t.data.rows, rows)
  }

  test("empty table has no rows") {
    val t = Table(TableData(List("Col"), Nil))
    assertEquals(t.data.rows, Nil)
  }

  test("different tables have independent values") {
    val t1 = Table(TableData(List("A"), List(List("1"))))
    val t2 = Table(TableData(List("B"), List(List("2"))))
    assertEquals(t1.data.headers, List("A"))
    assertEquals(t2.data.headers, List("B"))
    assertNotEquals(t1, t2)
  }

  test("TableData stores headers and rows") {
    val td = TableData(List("H1", "H2"), List(List("r1c1", "r1c2")))
    assertEquals(td.headers, List("H1", "H2"))
    assertEquals(td.rows, List(List("r1c1", "r1c2")))
  }
}
