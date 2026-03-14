package com.example.theme.coreui.components

import com.example.headless.components.TopBar
import com.raquo.laminar.api.L._

object CoreUiTopbarView {
  def render(topBar: TopBar, onToggleSidebar: () => Unit = () => ()): HtmlElement = headerTag(
    cls("header header-sticky-top"),
    div(
      cls("container-fluid"),
      button(
        cls("header-toggler d-lg-none me-2"),
        typ        := "button",
        aria.label := "Toggle sidebar",
        onClick --> { _ => onToggleSidebar() },
        span(fontSize("1.5rem"), "\u2630")
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
            aria.label := "Select renderer",
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
}
