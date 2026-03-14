package com.example.headless.components

import com.raquo.airstream.core.Signal
import com.raquo.airstream.ownership.ManualOwner
import munit.FunSuite

class TagsInputSuite extends FunSuite {

  private def signalNow[A](signal: Signal[A]): A = {
    val owner = new ManualOwner
    var value = Option.empty[A]
    signal.foreach(v => value = Some(v))(owner)
    owner.killSubscriptions()
    value.get
  }

  test("initial tags are set") {
    val ti = new TagsInput("Skills", initialTags = List("Scala", "Java"))
    assertEquals(signalNow(ti.tags), List("Scala", "Java"))
  }

  test("addTag appends a tag") {
    val ti = new TagsInput("Skills")
    ti.addTag("Scala")
    assertEquals(signalNow(ti.tags), List("Scala"))
  }

  test("addTag rejects duplicates") {
    val ti     = new TagsInput("Skills", initialTags = List("Scala"))
    val result = ti.addTag("Scala")
    assertEquals(result, false)
    assertEquals(signalNow(ti.tagCount), 1)
  }

  test("addTag rejects empty strings") {
    val ti     = new TagsInput("Skills")
    val result = ti.addTag("  ")
    assertEquals(result, false)
    assertEquals(signalNow(ti.tags), Nil)
  }

  test("addTag respects maxTags") {
    val ti     = new TagsInput("Skills", maxTags = 2, initialTags = List("A", "B"))
    val result = ti.addTag("C")
    assertEquals(result, false)
    assertEquals(signalNow(ti.tagCount), 2)
  }

  test("removeTag removes specific tag") {
    val ti = new TagsInput("Skills", initialTags = List("A", "B", "C"))
    ti.removeTag("B")
    assertEquals(signalNow(ti.tags), List("A", "C"))
  }

  test("removeLastTag removes the last tag") {
    val ti = new TagsInput("Skills", initialTags = List("A", "B", "C"))
    ti.removeLastTag()
    assertEquals(signalNow(ti.tags), List("A", "B"))
  }

  test("clearAll empties tags and input") {
    val ti = new TagsInput("Skills", initialTags = List("A", "B"))
    ti.setInput("test")
    ti.clearAll()
    assertEquals(signalNow(ti.tags), Nil)
    assertEquals(signalNow(ti.input), "")
  }

  test("addTag clears input on success") {
    val ti = new TagsInput("Skills")
    ti.setInput("Scala")
    ti.addTag("Scala")
    assertEquals(signalNow(ti.input), "")
  }
}
