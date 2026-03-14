package com.example.theme.coreui.components

import com.example.Page
import com.example.headless.components.Sidebar
import com.raquo.laminar.api.L._

object CoreUiSidebarView {
  def render(sidebar: Sidebar): HtmlElement = htmlTag("nav")(
    cls("sidebar sidebar-narrow-unfoldable show"),
    cls <-- sidebar.isCollapsed.map(if (_) "sidebar-narrow" else ""),
    // Override CoreUI's fixed positioning and mobile-hiding so the sidebar
    // participates in the parent flex layout instead of floating over content.
    // CoreUI hides the sidebar on mobile via negative margin; `show` + margin
    // override ensures it stays visible at all viewport widths.
    position.relative,
    marginLeft("0"),
    height("100%"),
    flexShrink("0"),
    // CoreUI sidebar structure
    div(
      cls("sidebar-header"),
      // Toggle button
      button(
        cls("btn btn-sm btn-ghost-secondary w-100"),
        aria.expanded <-- sidebar.isCollapsed.map(c => !c),
        aria.label <-- sidebar.isCollapsed.map(if (_) "Expand sidebar" else "Collapse sidebar"),
        child.text <-- sidebar.isCollapsed.map(if (_) "\u25B6" else "\u25C0"),
        onClick --> { _ => sidebar.toggleCollapse() }
      )
    ),
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
