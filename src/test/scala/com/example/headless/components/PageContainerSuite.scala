package com.example.headless.components

import munit.FunSuite

class PageContainerSuite extends FunSuite {

  test("stores title") {
    val pc = PageContainer("Dashboard", "Welcome", "content")
    assertEquals(pc.title, "Dashboard")
  }

  test("stores description") {
    val pc = PageContainer("Dashboard", "Welcome", "content")
    assertEquals(pc.description, "Welcome")
  }

  test("stores content") {
    val pc = PageContainer("Dashboard", "Welcome", "body")
    assertEquals(pc.content, "body")
  }

  test("different containers have independent values") {
    val pc1 = PageContainer("First", "Desc 1", "a")
    val pc2 = PageContainer("Second", "Desc 2", "b")
    assertEquals(pc1.title, "First")
    assertEquals(pc2.title, "Second")
    assertNotEquals(pc1, pc2)
  }

  test("supports different type parameters for content") {
    val strContainer = PageContainer("title", "desc", "content")
    val intContainer = PageContainer("title", "desc", 42)
    assertEquals(strContainer.content, "content")
    assertEquals(intContainer.content, 42)
  }
}
