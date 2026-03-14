package com.example.headless.pages

import com.raquo.airstream.core.Signal
import com.raquo.airstream.ownership.ManualOwner
import munit.FunSuite

class UIShowcasePageSuite extends FunSuite {

  private def signalNow[A](signal: Signal[A]): A = {
    val owner = new ManualOwner
    var value = Option.empty[A]
    signal.foreach(v => value = Some(v))(owner)
    owner.killSubscriptions()
    value.get
  }

  test("has correct title") {
    val page = new UIShowcasePage()
    assertEquals(page.title, "UI Showcase")
  }

  test("has correct description") {
    val page = new UIShowcasePage()
    assert(page.description.contains("headless UI components"))
  }

  test("composes tabs with 3 tab definitions") {
    val page = new UIShowcasePage()
    assertEquals(page.tabs.tabs.length, 3)
  }

  test("composes accordion with 3 items") {
    val page = new UIShowcasePage()
    assertEquals(page.accordion.items.length, 3)
  }

  test("composes two toggles") {
    val page = new UIShowcasePage()
    assertEquals(page.toggleDarkMode.label, "Dark Mode")
    assertEquals(page.toggleNotifications.label, "Notifications")
  }

  test("notifications toggle starts enabled") {
    val page = new UIShowcasePage()
    assertEquals(signalNow(page.toggleNotifications.isOn), true)
  }

  test("composes progress component") {
    val page = new UIShowcasePage()
    assertEquals(page.progress.label, "Upload Progress")
    assertEquals(signalNow(page.progress.value), 0)
  }

  test("composes tags input with initial tags") {
    val page = new UIShowcasePage()
    assertEquals(signalNow(page.tagsInput.tags), List("Scala", "Laminar"))
  }

  test("composes tooltip") {
    val page = new UIShowcasePage()
    assert(page.tooltip.text.nonEmpty)
  }

  test("all components have independent state") {
    val page = new UIShowcasePage()
    page.toggleDarkMode.toggle()
    page.progress.increment(50)
    page.tabs.select(2)
    assertEquals(signalNow(page.toggleDarkMode.isOn), true)
    assertEquals(signalNow(page.progress.value), 50)
    assertEquals(signalNow(page.tabs.selectedIndex), 2)
  }
}
