package com.example.headless.components

import com.raquo.airstream.core.Signal
import com.raquo.airstream.ownership.ManualOwner
import munit.FunSuite

class ToggleSuite extends FunSuite {

  private def signalNow[A](signal: Signal[A]): A = {
    val owner = new ManualOwner
    var value = Option.empty[A]
    signal.foreach(v => value = Some(v))(owner)
    owner.killSubscriptions()
    value.get
  }

  test("default initial value is false") {
    val toggle = new Toggle("Test")
    assertEquals(signalNow(toggle.isOn), false)
  }

  test("custom initial value is respected") {
    val toggle = new Toggle("Test", initialValue = true)
    assertEquals(signalNow(toggle.isOn), true)
  }

  test("toggle flips state") {
    val toggle = new Toggle("Test")
    toggle.toggle()
    assertEquals(signalNow(toggle.isOn), true)
    toggle.toggle()
    assertEquals(signalNow(toggle.isOn), false)
  }

  test("setOn and setOff work") {
    val toggle = new Toggle("Test")
    toggle.setOn()
    assertEquals(signalNow(toggle.isOn), true)
    toggle.setOff()
    assertEquals(signalNow(toggle.isOn), false)
  }

  test("label is stored") {
    val toggle = new Toggle("Dark Mode")
    assertEquals(toggle.label, "Dark Mode")
  }
}
