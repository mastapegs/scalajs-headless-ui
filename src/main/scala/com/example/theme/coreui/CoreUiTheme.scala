package com.example.theme.coreui

import com.raquo.laminar.api.L._

import com.example.headless.Counter
import com.example.headless.Sidebar
import com.example.headless.TopBar
import com.example.Page
import com.example.theme.Theme

object CoreUiTheme extends Theme {
  def counter(counter: Counter): HtmlElement = div(
    cls("card"),
    div(
      cls("card-body d-flex align-items-center gap-3"),
      span(
        cls("fs-4 fw-semibold"),
        child.text <-- counter.count.map(_.toString)
      ),
      button(
        cls("btn btn-primary"),
        "Increment",
        onClick --> { _ => counter.increment() }
      )
    )
  )
  def sidebar(sidebar: Sidebar): HtmlElement = htmlTag("nav")(
    cls("sidebar sidebar-narrow-unfoldable"),
    cls <-- sidebar.isCollapsed.map(if (_) "sidebar-narrow" else ""),
    // Override CoreUI's fixed positioning so the sidebar participates in
    // the parent flex layout instead of floating over content.
    position.relative,
    height("100%"),
    flexShrink("0"),
    // CoreUI sidebar structure
    div(
      cls("sidebar-header"),
      // Toggle button
      button(
        cls("btn btn-sm btn-ghost-secondary w-100"),
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
            span(
              cls("nav-icon"),
              iconFor(page)
            ),
            span(Page.label(page)),
            onClick.preventDefault --> { _ => sidebar.navigateTo(page) }
          )
        )
      }
    )
  )
  def topbar(topBar: TopBar): HtmlElement = headerTag(
    cls("header header-sticky-top"),
    div(
      cls("container-fluid"),
      a(cls("header-brand"), topBar.brandName),
      ul(
        cls("header-nav ms-auto"),
        li(
          cls("nav-item d-flex align-items-center gap-2"),
          span(cls("nav-link"), "Renderer:"),
          select(
            cls("form-select form-select-sm"),
            width("160px"),
            topBar.rendererOptions.map { case (v, lbl) =>
              option(value(v), lbl)
            },
            controlled(
              value <-- topBar.currentRenderer,
              onChange.mapToValue --> { v =>
                topBar.selectRenderer(v)
              }
            )
          )
        )
      )
    )
  )

  private def iconFor(page: Page): String = page match {
    case Page.Dashboard => "\u2302"
    case Page.Metrics   => "\u2261"
    case Page.Settings  => "\u2699"
  }
}
