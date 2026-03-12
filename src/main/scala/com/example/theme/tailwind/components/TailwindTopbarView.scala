package com.example.theme.tailwind.components

import com.example.headless.components.TopBar
import com.raquo.laminar.api.L._

object TailwindTopbarView {
  def render(topBar: TopBar): HtmlElement = headerTag(
    cls("fixed top-0 left-0 right-0 h-14 bg-slate-800 text-white flex items-center px-4 z-50"),
    span(cls("text-lg font-semibold"), topBar.brandName),
    div(
      cls("ml-auto flex items-center gap-2"),
      span(cls("text-sm text-slate-300"), "Renderer:"),
      select(
        cls("bg-slate-700 text-white rounded px-2 py-1 text-sm"),
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
}
