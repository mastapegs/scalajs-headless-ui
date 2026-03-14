package com.example.headless.pages

import com.example.headless.components.Counter

/** Headless dashboard page component: holds page-specific state and dependencies.
  *
  * Demonstrates headless page composition — the page owns multiple headless components, each with their own
  * independent state and behavior.
  */
final class DashboardPage {

  val title: String = "Dashboard"
  val description: String =
    "Welcome to the dashboard. This is the main overview page."

  val counters: List[Counter] = List(
    new Counter(label = "Primary", initialValue = 0),
    new Counter(label = "Secondary", initialValue = 100)
  )
}
