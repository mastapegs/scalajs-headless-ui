package com.example.theme.inline.components

import com.example.headless.components.Accordion
import com.raquo.laminar.api.L._

object InlineAccordionView {
  def render(accordion: Accordion): HtmlElement = div(
    border("1px solid #dee2e6"),
    borderRadius("8px"),
    overflow.hidden,
    accordion.items.zipWithIndex.map { case (item, idx) =>
      div(
        if (idx > 0) borderTop("1px solid #dee2e6") else emptyMod,
        button(
          display.flex,
          justifyContent.spaceBetween,
          alignItems.center,
          width("100%"),
          padding("12px 16px"),
          border("none"),
          backgroundColor("#f8f9fa"),
          cursor.pointer,
          fontSize("14px"),
          fontWeight("500"),
          span(item.title),
          span(
            transition := "transform 0.2s",
            transform <-- accordion.isOpen(item.key).map(if (_) "rotate(180deg)" else "rotate(0)"),
            "\u25BC"
          ),
          onClick --> { _ => accordion.toggle(item.key) }
        ),
        div(
          overflow.hidden,
          maxHeight <-- accordion.isOpen(item.key).map(if (_) "200px" else "0"),
          transition := "max-height 0.3s ease",
          div(
            padding("12px 16px"),
            fontSize("14px"),
            color("#495057"),
            item.content
          )
        )
      )
    }
  )
}
