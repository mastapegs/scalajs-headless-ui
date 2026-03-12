package com.example.theme.tailwind.components

import com.example.Page
import com.example.headless.components.Sidebar
import com.raquo.laminar.api.L._

object TailwindSidebarView {
  def render(sidebar: Sidebar): HtmlElement = htmlTag("nav")(
    cls("bg-slate-800 text-white transition-all duration-300 flex-shrink-0 overflow-hidden"),
    width <-- sidebar.isCollapsed.map(if (_) "64px" else "220px"),
    height("100%"),
    div(
      cls("flex flex-col h-full"),
      button(
        cls("px-4 py-3 text-slate-300 hover:text-white text-sm w-full text-left"),
        aria.expanded <-- sidebar.isCollapsed.map(c => !c),
        aria.label <-- sidebar.isCollapsed.map(if (_) "Expand sidebar" else "Collapse sidebar"),
        child.text <-- sidebar.isCollapsed.map(if (_) "\u25B6" else "\u25C0"),
        onClick --> { _ => sidebar.toggleCollapse() }
      ),
      ul(
        cls("list-none p-0 m-0"),
        sidebar.pages.map { page =>
          li(
            a(
              cls("flex items-center gap-3 px-4 py-2 mx-2 rounded cursor-pointer hover:bg-slate-700"),
              cls <-- sidebar.isActive(page).map(if (_) "bg-slate-700" else ""),
              href("#"),
              aria.current <-- sidebar.isActive(page).map(if (_) "page" else ""),
              span(
                cls("text-lg"),
                aria.hidden := true,
                iconFor(page)
              ),
              span(
                cls <-- sidebar.isCollapsed.map(if (_) "hidden" else ""),
                Page.label(page)
              ),
              onClick.preventDefault --> { _ => sidebar.navigateTo(page) }
            )
          )
        }
      )
    )
  )

  private def iconFor(page: Page): String = page match {
    case Page.Dashboard => "\u2302"
    case Page.Metrics   => "\u2261"
    case Page.Settings  => "\u2699"
  }
}
