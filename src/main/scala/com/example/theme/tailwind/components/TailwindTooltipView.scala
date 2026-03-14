package com.example.theme.tailwind.components

import com.example.headless.components.Tooltip
import com.raquo.laminar.api.L._

/** Tailwind tooltip with an indigo trigger button, floating dark popup, and fade-in animation.
  *
  * '''Design techniques:'''
  *   - '''Indigo trigger button''' consistent with the global button system (`bg-indigo-600 hover:bg-indigo-700`)
  *   - '''Floating popup''' positioned with `absolute bottom-full` and centered via `left-1/2 -translate-x-1/2`
  *   - '''Opacity transition''' (`transition-opacity duration-200`) fades the tooltip in/out rather than popping
  *     abruptly — a small detail that signals polish
  *   - '''Dark tooltip surface''' (`bg-gray-900`) with `rounded-lg` and generous padding for readability
  *   - '''`pointer-events-none`''' on the popup prevents it from interfering with hover detection on the trigger
  */
object TailwindTooltipView {
  def render(tooltip: Tooltip): HtmlElement = div(
    cls("flex items-center gap-4"),
    div(
      cls("relative inline-block"),
      button(
        cls(
          "inline-flex items-center px-5 py-2.5 bg-indigo-600 hover:bg-indigo-700 active:bg-indigo-800 text-white text-sm font-medium rounded-lg transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
        ),
        "Hover me",
        onMouseEnter --> { _ => tooltip.show() },
        onMouseLeave --> { _ => tooltip.hide() }
      ),
      div(
        cls(
          "absolute bottom-full left-1/2 -translate-x-1/2 mb-2 px-3 py-2 bg-gray-900 text-white text-xs rounded-lg whitespace-nowrap pointer-events-none transition-opacity duration-200 shadow-lg"
        ),
        opacity <-- tooltip.isVisible.map(if (_) "1" else "0"),
        tooltip.text
      )
    ),
    span(
      cls("text-sm text-gray-500 italic"),
      child.text <-- tooltip.isVisible.map(if (_) "Tooltip visible" else "Hover the button to see the tooltip")
    )
  )
}
