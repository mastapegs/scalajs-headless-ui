package com.example.headless.pages

import munit.FunSuite

class PagesSuite extends FunSuite {

  test("DashboardPage has correct title") {
    val page = new DashboardPage()
    assertEquals(page.title, "Dashboard")
  }

  test("DashboardPage has correct description") {
    val page = new DashboardPage()
    assertEquals(page.description, "Welcome to the dashboard. This is the main overview page.")
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
