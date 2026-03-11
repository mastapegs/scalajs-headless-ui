package com.example.theme.inline.components

import com.example.headless.components.Counter
import com.raquo.laminar.api.L._

object InlineCounterView {
  def render(counter: Counter): HtmlElement = div(
    display.flex,
    alignItems.center,
    gap("16px"),
    padding("16px"),
    border("1px solid #bdc3c7"),
    borderRadius("8px"),
    backgroundColor("#f8f9fa"),
    span(
      fontSize("24px"),
      fontWeight("600"),
      child.text <-- counter.count.map(_.toString)
    ),
    button(
      padding("8px 16px"),
      border("1px solid #2c3e50"),
      borderRadius("4px"),
      backgroundColor("#2c3e50"),
      color("white"),
      cursor.pointer,
      fontSize("14px"),
      "Increment",
      onClick --> { _ => counter.increment() }
    )
  )
}
