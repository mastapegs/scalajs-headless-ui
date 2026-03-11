package com.example.headless.components

import com.example.Page
import com.raquo.laminar.api.L._
import munit.FunSuite

class SidebarSuite extends FunSuite {

  private def makeSidebar(
      pages: List[Page] = Page.all,
      currentPage: Page = Page.Dashboard,
      onNavigate: Page => Unit = _ => ()
  ): Sidebar =
    new Sidebar(pages, Val(currentPage), onNavigate)

  test("initial collapsed state is false") {
    val sidebar = makeSidebar()
    assertEquals(sidebar.isCollapsed.now(), false)
  }

  test("toggleCollapse flips collapsed state") {
    val sidebar = makeSidebar()
    sidebar.toggleCollapse()
    assertEquals(sidebar.isCollapsed.now(), true)
  }

  test("double toggle returns to original state") {
    val sidebar = makeSidebar()
    sidebar.toggleCollapse()
    sidebar.toggleCollapse()
    assertEquals(sidebar.isCollapsed.now(), false)
  }

  test("navigateTo invokes callback with correct page") {
    var navigated: Option[Page] = None
    val sidebar = makeSidebar(onNavigate = p => navigated = Some(p))
    sidebar.navigateTo(Page.Metrics)
    assertEquals(navigated, Some(Page.Metrics))
  }

  test("isActive returns true for current page") {
    val sidebar = makeSidebar(currentPage = Page.Dashboard)
    assertEquals(sidebar.isActive(Page.Dashboard).now(), true)
  }

  test("isActive returns false for non-current page") {
    val sidebar = makeSidebar(currentPage = Page.Dashboard)
    assertEquals(sidebar.isActive(Page.Metrics).now(), false)
  }

  test("pages returns the injected page list") {
    val sidebar = makeSidebar(pages = List(Page.Dashboard, Page.Settings))
    assertEquals(sidebar.pages, List(Page.Dashboard, Page.Settings))
  }
}
