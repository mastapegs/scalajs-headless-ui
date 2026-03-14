package com.example.headless.components

import com.raquo.airstream.core.Signal
import com.raquo.airstream.ownership.ManualOwner
import munit.FunSuite

class AccordionSuite extends FunSuite {

  private def signalNow[A](signal: Signal[A]): A = {
    val owner = new ManualOwner
    var value = Option.empty[A]
    signal.foreach(v => value = Some(v))(owner)
    owner.killSubscriptions()
    value.get
  }

  private val sampleItems = List(
    Accordion.ItemDef("a", "Title A", "Content A"),
    Accordion.ItemDef("b", "Title B", "Content B"),
    Accordion.ItemDef("c", "Title C", "Content C")
  )

  test("initially no items are open") {
    val acc = new Accordion(sampleItems)
    assertEquals(signalNow(acc.openKeys), Set.empty[String])
  }

  test("toggle opens an item") {
    val acc = new Accordion(sampleItems)
    acc.toggle("a")
    assertEquals(signalNow(acc.isOpen("a")), true)
  }

  test("toggle closes an open item") {
    val acc = new Accordion(sampleItems)
    acc.toggle("a")
    acc.toggle("a")
    assertEquals(signalNow(acc.isOpen("a")), false)
  }

  test("single mode closes previous when opening new") {
    val acc = new Accordion(sampleItems, allowMultiple = false)
    acc.toggle("a")
    acc.toggle("b")
    assertEquals(signalNow(acc.isOpen("a")), false)
    assertEquals(signalNow(acc.isOpen("b")), true)
  }

  test("multiple mode keeps previous open") {
    val acc = new Accordion(sampleItems, allowMultiple = true)
    acc.toggle("a")
    acc.toggle("b")
    assertEquals(signalNow(acc.isOpen("a")), true)
    assertEquals(signalNow(acc.isOpen("b")), true)
  }

  test("openKeys reflects all open items") {
    val acc = new Accordion(sampleItems, allowMultiple = true)
    acc.toggle("a")
    acc.toggle("c")
    assertEquals(signalNow(acc.openKeys), Set("a", "c"))
  }
}
