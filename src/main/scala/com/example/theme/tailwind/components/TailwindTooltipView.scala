package com.example.theme.tailwind.components

import com.example.headless.components.Tooltip
import com.raquo.laminar.api.L._

object TailwindTooltipView {
  def render(tooltip: Tooltip): HtmlElement = div(
    cls("flex items-center gap-4"),
    div(
      cls("relative inline-block"),
      button(
        cls("px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded transition-colors text-sm"),
        "Hover me",
        onMouseEnter --> { _ => tooltip.show() },
        onMouseLeave --> { _ => tooltip.hide() }
      ),
      div(
        cls(
          "absolute bottom-full left-1/2 -translate-x-1/2 mb-2 px-3 py-2 bg-gray-900 text-white text-xs rounded whitespace-nowrap pointer-events-none transition-opacity"
        ),
        opacity <-- tooltip.isVisible.map(if (_) "1" else "0"),
        tooltip.text
      )
    ),
    span(
      cls("text-sm text-gray-500"),
      child.text <-- tooltip.isVisible.map(if (_) "Tooltip visible" else "Hover the button to see the tooltip")
    )
  )
}
