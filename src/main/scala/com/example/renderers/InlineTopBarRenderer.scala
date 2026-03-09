package com.example.renderers

import com.raquo.laminar.api.L._
import com.example.headless.TopBar

object InlineTopBarRenderer extends TopBarRenderer {

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

  private def rendererToggle(topBar: TopBar): HtmlElement =
    div(
      display.flex,
      alignItems.center,
      gap("8px"),
      label("Renderer:"),
      select(
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
}
