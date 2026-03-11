package com.example.headless.components

import com.example.Page
import com.raquo.laminar.api.L._

/** Headless sidebar component: pure state and logic, no rendering. */
final class Sidebar(
    val pages: List[Page],
    val currentPage: Signal[Page],
    private val onNavigate: Page => Unit
) {

  private val collapsedVar: Var[Boolean] = Var(false)

  val isCollapsed: Signal[Boolean] = collapsedVar.signal

  def toggleCollapse(): Unit =
    collapsedVar.update(!_)

  def navigateTo(page: Page): Unit =
    onNavigate(page)

  def isActive(page: Page): Signal[Boolean] =
    currentPage.map(_ == page)
}
