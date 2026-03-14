package com.example.theme.tailwind.components

import com.example.Page
import com.example.headless.components.Sidebar
import com.raquo.laminar.api.L._

/** Tailwind sidebar with a dark slate palette, smooth collapse animation, and polished navigation items.
  *
  * '''Design techniques:'''
  *   - '''Smooth width transition''' (`transition-all duration-300 ease-in-out`) for collapse/expand
  *   - '''Gradient background''' (`bg-gradient-to-b from-slate-800 to-slate-900`) adds subtle depth
  *   - '''Active indicator''' uses a left-border accent (`border-l-2 border-indigo-400`) and highlighted background to
  *     clearly signal the current page
  *   - '''Hover states''' with `hover:bg-white/10` use semi-transparent white for consistent dark-mode hovers
  *   - '''Icon sizing''' with `text-lg w-6 text-center` ensures uniform icon alignment when collapsed
  *   - '''Overflow hidden''' prevents text wrapping artifacts during the collapse transition
  */
object TailwindSidebarView {
  def render(sidebar: Sidebar): HtmlElement = htmlTag("nav")(
    cls(
      "bg-gradient-to-b from-slate-800 to-slate-900 text-white transition-all duration-300 ease-in-out flex-shrink-0"
    ),
    cls("overflow-hidden border-r border-slate-700/50"),
    width <-- sidebar.isCollapsed.map(if (_) "64px" else "240px"),
    height("100%"),
    div(
      cls("flex flex-col h-full"),
      // Toggle button
      div(
        cls("px-3 py-4 border-b border-slate-700/50"),
        button(
          cls(
            "w-full flex items-center justify-center p-2 rounded-lg text-slate-400 hover:text-white hover:bg-white/10 transition-colors duration-200"
          ),
          aria.expanded <-- sidebar.isCollapsed.map(c => !c),
          aria.label <-- sidebar.isCollapsed.map(if (_) "Expand sidebar" else "Collapse sidebar"),
          // Hamburger-style icon
          span(
            cls("text-lg"),
            child.text <-- sidebar.isCollapsed.map(if (_) "\u25B6" else "\u25C0")
          ),
          onClick --> { _ => sidebar.toggleCollapse() }
        )
      ),
      // Navigation items
      ul(
        cls("list-none p-0 m-0 mt-2 flex-1"),
        sidebar.pages.map { page =>
          li(
            cls("mx-2 mb-1"),
            a(
              cls(
                "flex items-center gap-3 px-3 py-2.5 rounded-lg cursor-pointer transition-all duration-200 no-underline"
              ),
              cls <-- sidebar.isActive(page).combineWith(sidebar.isCollapsed).map {
                case (true, _) =>
                  "bg-indigo-500/20 text-white border-l-2 border-indigo-400"
                case (false, _) =>
                  "text-slate-300 hover:bg-white/10 hover:text-white border-l-2 border-transparent"
              },
              href("#"),
              aria.current <-- sidebar.isActive(page).map(if (_) "page" else ""),
              span(
                cls("text-lg w-6 text-center flex-shrink-0"),
                aria.hidden := true,
                iconFor(page)
              ),
              span(
                cls("whitespace-nowrap text-sm font-medium transition-opacity duration-200"),
                cls <-- sidebar.isCollapsed.map(if (_) "opacity-0 w-0 overflow-hidden" else "opacity-100"),
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
    case Page.Dashboard  => "\u2302"
    case Page.Metrics    => "\u2261"
    case Page.Settings   => "\u2699"
    case Page.Fetch      => "\u21e9"
    case Page.UIShowcase => "\u2726"
  }
}
