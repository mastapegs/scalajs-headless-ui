package com.example.theme.coreui.components

import com.example.headless.components.Tooltip
import com.raquo.laminar.api.L._

object CoreUiTooltipView {
  def render(tooltip: Tooltip): HtmlElement = div(
    display.flex,
    alignItems.center,
    gap("16px"),
    div(
      position.relative,
      display.inlineBlock,
      button(
        cls("btn btn-primary"),
        "Hover me",
        onMouseEnter --> { _ => tooltip.show() },
        onMouseLeave --> { _ => tooltip.hide() }
      ),
      div(
        position.absolute,
        bottom("calc(100% + 8px)"),
        left("50%"),
        transform := "translateX(-50%)",
        padding("6px 12px"),
        backgroundColor("var(--cui-dark, #212631)"),
        color("white"),
        borderRadius("4px"),
        fontSize("12px"),
        whiteSpace.nowrap,
        pointerEvents := "none",
        transition    := "opacity 0.2s",
        zIndex(10),
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
