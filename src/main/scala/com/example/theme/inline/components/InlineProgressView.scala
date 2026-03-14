package com.example.theme.inline.components

import com.example.headless.components.Progress
import com.raquo.laminar.api.L._

object InlineProgressView {
  def render(progress: Progress): HtmlElement = div(
    display.flex,
    flexDirection.column,
    gap("12px"),
    div(
      display.flex,
      justifyContent.spaceBetween,
      span(fontSize("14px"), fontWeight("500"), progress.label),
      span(
        fontSize("14px"),
        color("#6c757d"),
        child.text <-- progress.percentage.map(p => s"$p%")
      )
    ),
    div(
      height("12px"),
      backgroundColor("#e9ecef"),
      borderRadius("6px"),
      overflow.hidden,
      div(
        height("100%"),
        backgroundColor("#2c3e50"),
        borderRadius("6px"),
        transition := "width 0.3s ease",
        width <-- progress.percentage.map(p => s"$p%"),
        role := "progressbar",
        aria.valueNow <-- progress.value.map(_.toDouble),
        aria.label := progress.label
      )
    ),
    div(
      display.flex,
      gap("8px"),
      button(
        padding("6px 12px"),
        border("1px solid #2c3e50"),
        borderRadius("4px"),
        backgroundColor("#2c3e50"),
        color("white"),
        cursor.pointer,
        fontSize("12px"),
        "+10%",
        onClick --> { _ => progress.increment(10) }
      ),
      button(
        padding("6px 12px"),
        border("1px solid #6c757d"),
        borderRadius("4px"),
        backgroundColor("white"),
        color("#6c757d"),
        cursor.pointer,
        fontSize("12px"),
        "Reset",
        onClick --> { _ => progress.reset() }
      )
    )
  )
}
