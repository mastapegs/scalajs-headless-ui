package com.example.headless.components

import munit.FunSuite

class TableSuite extends FunSuite {

  test("stores headers") {
    val t = Table(None, List("A", "B", "C"), Nil)
    assertEquals(t.headers, List("A", "B", "C"))
  }

  test("stores rows") {
    val rows = List(List("1", "2"), List("3", "4"))
    val t    = Table(None, List("X", "Y"), rows)
    assertEquals(t.rows, rows)
  }

  test("empty table has no rows") {
    val t = Table(None, List("Col"), Nil)
    assertEquals(t.rows, Nil)
  }

  test("different tables have independent values") {
    val t1 = Table(None, List("A"), List(List("1")))
    val t2 = Table(None, List("B"), List(List("2")))
    assertEquals(t1.headers, List("A"))
    assertEquals(t2.headers, List("B"))
    assertNotEquals(t1, t2)
  }

  test("stores caption") {
    val t = Table(Some("My Caption"), List("A"), Nil)
    assertEquals(t.caption, Some("My Caption"))
  }

  test("caption defaults to None") {
    val t = Table(None, List("A"), Nil)
    assertEquals(t.caption, None)
  }
}
