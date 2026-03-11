package com.example.headless.components

import com.example.Page
import com.raquo.airstream.core.Signal
import com.raquo.airstream.ownership.ManualOwner
import com.raquo.laminar.api.L._
import munit.FunSuite

class SidebarSuite extends FunSuite {

  private def signalNow[A](signal: Signal[A]): A = {
    val owner = new ManualOwner
    var value = Option.empty[A]
    signal.foreach(v => value = Some(v))(owner)
    owner.killSubscriptions()
    value.get
  }

  private def makeSidebar(
      pages: List[Page] = Page.all,
      currentPage: Page = Page.Dashboard,
      onNavigate: Page => Unit = _ => ()
  ): Sidebar =
    new Sidebar(pages, Val(currentPage), onNavigate)

  test("initial collapsed state is false") {
    val sidebar = makeSidebar()
    assertEquals(signalNow(sidebar.isCollapsed), false)
  }

  test("toggleCollapse flips collapsed state") {
    val sidebar = makeSidebar()
    sidebar.toggleCollapse()
    assertEquals(signalNow(sidebar.isCollapsed), true)
  }

  test("double toggle returns to original state") {
    val sidebar = makeSidebar()
    sidebar.toggleCollapse()
    sidebar.toggleCollapse()
    assertEquals(signalNow(sidebar.isCollapsed), false)
  }

  test("navigateTo invokes callback with correct page") {
    var navigated: Option[Page] = None
    val sidebar                 = makeSidebar(onNavigate = p => navigated = Some(p))
    sidebar.navigateTo(Page.Metrics)
    assertEquals(navigated, Some(Page.Metrics))
  }

  test("isActive returns true for current page") {
    val sidebar = makeSidebar(currentPage = Page.Dashboard)
    assertEquals(signalNow(sidebar.isActive(Page.Dashboard)), true)
  }

  test("isActive returns false for non-current page") {
    val sidebar = makeSidebar(currentPage = Page.Dashboard)
    assertEquals(signalNow(sidebar.isActive(Page.Metrics)), false)
  }

  test("isActive is true only for the current page across all pages") {
    val sidebar = makeSidebar(currentPage = Page.Metrics)
    Page.all.foreach { page =>
      val expected = page == Page.Metrics
      assertEquals(signalNow(sidebar.isActive(page)), expected, s"isActive(${Page.label(page)})")
    }
  }

  test("pages returns the injected page list") {
    val sidebar = makeSidebar(pages = List(Page.Dashboard, Page.Settings))
    assertEquals(sidebar.pages, List(Page.Dashboard, Page.Settings))
  }
}
