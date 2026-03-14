package com.example.headless.components

import com.raquo.airstream.core.Signal
import com.raquo.airstream.ownership.ManualOwner
import munit.FunSuite

class TooltipSuite extends FunSuite {

  private def signalNow[A](signal: Signal[A]): A = {
    val owner = new ManualOwner
    var value = Option.empty[A]
    signal.foreach(v => value = Some(v))(owner)
    owner.killSubscriptions()
    value.get
  }

  test("initially hidden") {
    val tooltip = new Tooltip("Hello")
    assertEquals(signalNow(tooltip.isVisible), false)
  }

  test("show makes it visible") {
    val tooltip = new Tooltip("Hello")
    tooltip.show()
    assertEquals(signalNow(tooltip.isVisible), true)
  }

  test("hide makes it hidden") {
    val tooltip = new Tooltip("Hello")
    tooltip.show()
    tooltip.hide()
    assertEquals(signalNow(tooltip.isVisible), false)
  }

  test("text and placement are stored") {
    val tooltip = new Tooltip("My tip", placement = "bottom")
    assertEquals(tooltip.text, "My tip")
    assertEquals(tooltip.placement, "bottom")
  }
}
