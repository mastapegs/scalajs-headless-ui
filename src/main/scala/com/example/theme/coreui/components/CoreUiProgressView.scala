package com.example.theme.coreui.components

import com.example.headless.components.Progress
import com.raquo.laminar.api.L._

object CoreUiProgressView {
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
      backgroundColor("#ebedef"),
      borderRadius("6px"),
      overflow.hidden,
      div(
        height("100%"),
        backgroundColor("var(--cui-primary, #321fdb)"),
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
      button(cls("btn btn-sm btn-primary"), "+10%", onClick --> { _ => progress.increment(10) }),
      button(cls("btn btn-sm btn-outline-secondary"), "Reset", onClick --> { _ => progress.reset() })
    )
  )
}
