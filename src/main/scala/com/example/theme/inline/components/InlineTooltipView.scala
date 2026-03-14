package com.example.theme.inline.components

import com.example.headless.components.Tooltip
import com.raquo.laminar.api.L._

object InlineTooltipView {
  def render(tooltip: Tooltip): HtmlElement = div(
    display.flex,
    alignItems.center,
    gap("16px"),
    div(
      position.relative,
      display.inlineBlock,
      button(
        padding("10px 20px"),
        border("1px solid #2c3e50"),
        borderRadius("4px"),
        backgroundColor("#2c3e50"),
        color("white"),
        cursor.pointer,
        fontSize("14px"),
        "Hover me",
        onMouseEnter --> { _ => tooltip.show() },
        onMouseLeave --> { _ => tooltip.hide() }
      ),
      div(
        position.absolute,
        bottom("calc(100% + 8px)"),
        left("50%"),
        transform := "translateX(-50%)",
        padding("8px 12px"),
        backgroundColor("#333"),
        color("white"),
        borderRadius("4px"),
        fontSize("12px"),
        whiteSpace.nowrap,
        pointerEvents := "none",
        transition    := "opacity 0.2s",
        opacity <-- tooltip.isVisible.map(if (_) "1" else "0"),
        tooltip.text
      )
    ),
    span(
      fontSize("14px"),
      color("#6c757d"),
      child.text <-- tooltip.isVisible.map(if (_) "Tooltip visible" else "Hover the button to see the tooltip")
    )
  )
}
