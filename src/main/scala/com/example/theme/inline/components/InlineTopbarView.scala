package com.example.theme.inline.components

import com.example.headless.components.TopBar
import com.raquo.laminar.api.L._

object InlineTopbarView {
  private def rendererToggle(topBar: TopBar): HtmlElement =
    div(
      display.flex,
      alignItems.center,
      gap("8px"),
      label(forId := "renderer-select", "Renderer:"),
      select(
        idAttr := "renderer-select",
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

  def render(topBar: TopBar): HtmlElement =
    headerTag(
      display.flex,
      alignItems.center,
      justifyContent.spaceBetween,
      position.fixed,
      top("0"),
      left("0"),
      width("100%"),
      height("56px"),
      backgroundColor("#2c3e50"),
      color("white"),
      padding("0 24px"),
      zIndex(100),
      span(fontSize("18px"), fontWeight("600"), topBar.brandName),
      rendererToggle(topBar)
    )
}
