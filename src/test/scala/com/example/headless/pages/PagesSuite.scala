package com.example.headless.pages

import com.example.headless.components.Counter
import munit.FunSuite

class PagesSuite extends FunSuite {

  test("DashboardPage has correct title") {
    val page = new DashboardPage(new Counter())
    assertEquals(page.title, "Dashboard")
  }

  test("DashboardPage has correct description") {
    val page = new DashboardPage(new Counter())
    assertEquals(page.description, "Welcome to the dashboard. This is the main overview page.")
  }

  test("DashboardPage holds a reference to the injected counter") {
    val counter = new Counter(5)
    val page    = new DashboardPage(counter)
    assert(page.counter eq counter)
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
}
