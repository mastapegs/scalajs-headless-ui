package com.example.theme.coreui.components

import com.example.Page
import com.example.headless.components.Sidebar
import com.raquo.laminar.api.L._

object CoreUiSidebarView {
  def render(sidebar: Sidebar): HtmlElement = htmlTag("nav")(
    cls("sidebar sidebar-dark sidebar-fixed border-end"),
    // sidebar-fixed: visible on desktop, hidden on mobile by default.
    // Toggling adds "sidebar-narrow show":
    //   desktop  → sidebar-narrow collapses to icon-only width
    //   mobile   → show slides the sidebar in as an overlay
    cls <-- sidebar.isCollapsed.map(if (_) "sidebar-narrow show" else ""),
    // sidebar-header
    div(
      cls("sidebar-header"),
      div(cls("sidebar-brand"), "Menu")
    ),
    // sidebar-nav
    ul(
      cls("sidebar-nav"),
      sidebar.pages.map { page =>
        li(
          cls("nav-item"),
          a(
            cls("nav-link"),
            cls <-- sidebar.isActive(page).map(if (_) "active" else ""),
            href("#"),
            aria.current <-- sidebar.isActive(page).map(if (_) "page" else ""),
            span(
              cls("nav-icon"),
              aria.hidden := true,
              iconFor(page)
            ),
            span(Page.label(page)),
            onClick.preventDefault --> { _ => sidebar.navigateTo(page) }
          )
        )
      }
    ),
    // sidebar-footer with CoreUI's sidebar-toggler
    div(
      cls("sidebar-footer border-top d-flex"),
      button(
        cls("sidebar-toggler"),
        typ := "button",
        aria.expanded <-- sidebar.isCollapsed.map(c => !c),
        aria.label <-- sidebar.isCollapsed.map(if (_) "Expand sidebar" else "Collapse sidebar"),
        onClick --> { _ => sidebar.toggleCollapse() }
      )
    )
  )

  private def iconFor(page: Page): String = page match {
    case Page.Dashboard  => "\u2302"
    case Page.Metrics    => "\u2261"
    case Page.Settings   => "\u2699"
    case Page.Fetch      => "\u21e9"
    case Page.UIShowcase => "\u2726"
  }
}
