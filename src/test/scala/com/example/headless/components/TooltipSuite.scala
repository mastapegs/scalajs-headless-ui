package com.example.headless.components

import com.example.headless.SignalHelpers
import munit.FunSuite

class TooltipSuite extends FunSuite with SignalHelpers {

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
