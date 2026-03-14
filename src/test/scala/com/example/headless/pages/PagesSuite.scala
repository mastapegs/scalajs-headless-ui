package com.example.headless.pages

import com.example.headless.SignalHelpers
import munit.FunSuite

class PagesSuite extends FunSuite with SignalHelpers {

  test("DashboardPage has correct title") {
    val page = new DashboardPage()
    assertEquals(page.title, "Dashboard")
  }

  test("DashboardPage has correct description") {
    val page = new DashboardPage()
    assertEquals(page.description, "Welcome to the dashboard. This is the main overview page.")
  }

  test("DashboardPage composes two counters") {
    val page = new DashboardPage()
    assertEquals(page.counters.length, 2)
  }

  test("DashboardPage primary counter starts at 0 with label") {
    val page    = new DashboardPage()
    val primary = page.counters.head
    assertEquals(primary.label, "Primary")
    assertEquals(signalNow(primary.count), 0)
  }

  test("DashboardPage secondary counter starts at 100 with label") {
    val page      = new DashboardPage()
    val secondary = page.counters(1)
    assertEquals(secondary.label, "Secondary")
    assertEquals(signalNow(secondary.count), 100)
  }

  test("DashboardPage counters have independent state") {
    val page = new DashboardPage()
    page.counters.head.increment()
    page.counters.head.increment()
    page.counters(1).increment()
    assertEquals(signalNow(page.counters.head.count), 2)
    assertEquals(signalNow(page.counters(1).count), 101)
  }

  test("MetricsPage has correct title") {
    val page = new MetricsPage()
    assertEquals(page.title, "Metrics")
  }

  test("MetricsPage has correct description") {
    val page = new MetricsPage()
    assertEquals(page.description, "Charts and metrics would be displayed here.")
  }

  test("SettingsPage has correct title") {
    val page = new SettingsPage()
    assertEquals(page.title, "Settings")
  }

  test("SettingsPage has correct description") {
    val page = new SettingsPage()
    assertEquals(page.description, "Application settings and preferences.")
  }

  test("UIShowcasePage has correct title") {
    val page = new UIShowcasePage()
    assertEquals(page.title, "UI Showcase")
  }

  test("UIShowcasePage has correct description") {
    val page = new UIShowcasePage()
    assert(page.description.contains("headless UI components"))
  }
}
