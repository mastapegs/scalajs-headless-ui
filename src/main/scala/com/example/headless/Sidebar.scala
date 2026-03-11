package com.example.headless

import com.example.Page
import com.raquo.laminar.api.L._

/** Headless sidebar component: pure state and logic, no rendering. */
final class Sidebar(
    val pages: List[Page],
    val currentPage: Signal[Page],
    private val onNavigate: Page => Unit
) {

  private val collapsedVar: Var[Boolean] = Var(false)
  private val mobileOpenVar: Var[Boolean] = Var(false)

  val isCollapsed: Signal[Boolean] = collapsedVar.signal
  val isMobileOpen: Signal[Boolean] = mobileOpenVar.signal

  def toggleCollapse(): Unit =
    collapsedVar.update(!_)

  def toggleMobile(): Unit =
    mobileOpenVar.update(!_)

  def closeMobile(): Unit =
    mobileOpenVar.set(false)

  def navigateTo(page: Page): Unit = {
    mobileOpenVar.set(false)
    onNavigate(page)
  }

  def isActive(page: Page): Signal[Boolean] =
    currentPage.map(_ == page)
}
