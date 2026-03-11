package com.example.theme.coreui

import com.example.Page
import com.example.headless.{Counter, Sidebar, TopBar}
import com.example.theme.Theme
import com.raquo.laminar.api.L._
import org.scalajs.dom

object CoreUiTheme extends Theme {

  private def injectMobileStyles(): Unit = {
    val exists = dom.document.querySelector("[data-coreui-mobile-styles]") != null
    if (!exists) {
      val el = dom.document.createElement("style")
      el.setAttribute("data-coreui-mobile-styles", "")
      el.textContent =
        """@media (min-width: 769px) {
          |  .coreui-hamburger { display: none !important; }
          |  .coreui-sidebar-backdrop { display: none !important; }
          |}
          |@media (max-width: 768px) {
          |  .coreui-sidebar-mobile { display: none !important; }
          |  .coreui-sidebar-mobile.mobile-open { display: flex !important; position: fixed !important; top: 56px !important; left: 0 !important; bottom: 0 !important; z-index: 99 !important; width: 220px !important; }
          |  .coreui-sidebar-backdrop { position: fixed; top: 56px; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.4); z-index: 98; }
          |  .coreui-sidebar-backdrop.hidden { display: none !important; }
          |}
          |""".stripMargin
      dom.document.head.appendChild(el)
    }
  }
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
  def sidebar(sidebar: Sidebar): HtmlElement = {
    injectMobileStyles()
    div(
      // Backdrop for mobile overlay
      div(
        cls("coreui-sidebar-backdrop"),
        cls <-- sidebar.isMobileOpen.map(if (_) "" else "hidden"),
        onClick --> { _ => sidebar.closeMobile() }
      ),
      htmlTag("nav")(
        cls("sidebar sidebar-narrow-unfoldable coreui-sidebar-mobile"),
        cls <-- sidebar.isCollapsed.map(if (_) "sidebar-narrow" else ""),
        cls <-- sidebar.isMobileOpen.map(if (_) "mobile-open" else ""),
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
    )
  }
  def topbar(topBar: TopBar): HtmlElement = headerTag(
    cls("header header-sticky-top"),
    div(
      cls("container-fluid"),
      button(
        cls("coreui-hamburger btn btn-ghost-secondary"),
        fontSize("20px"),
        "\u2630",
        onClick --> { _ => topBar.toggleSidebar() }
      ),
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
