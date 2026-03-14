package com.example.theme.tailwind.components

import com.example.headless.components.Progress
import com.raquo.laminar.api.L._

/** Tailwind progress bar with gradient fill, smooth width animation, and consistent button styling.
  *
  * '''Design techniques:'''
  *   - '''Gradient fill''' (`bg-gradient-to-r from-indigo-500 to-indigo-600`) adds visual richness to the bar
  *   - '''Rounded track''' (`rounded-full`) on both track and fill for a polished, pill-shaped bar
  *   - '''Width transition''' (`transition-all duration-500 ease-out`) provides smooth, satisfying animation when the
  *     value changes
  *   - '''Button pair''' — solid primary (indigo) and outlined secondary (gray border) — follows standard Tailwind
  *     button conventions with focus rings
  *   - '''Tabular numbers''' (`tabular-nums`) on the percentage keeps the layout stable as digits change
  */
object TailwindProgressView {
  def render(progress: Progress): HtmlElement = div(
    cls("space-y-4"),
    div(
      cls("flex justify-between items-center"),
      span(cls("text-sm font-semibold text-gray-700"), progress.label),
      span(
        cls("text-sm font-medium text-gray-500 tabular-nums"),
        child.text <-- progress.percentage.map(p => s"$p%")
      )
    ),
    div(
      cls("w-full bg-gray-200 rounded-full h-2.5 overflow-hidden"),
      div(
        cls("bg-gradient-to-r from-indigo-500 to-indigo-600 h-2.5 rounded-full transition-all duration-500 ease-out"),
        width <-- progress.percentage.map(p => s"$p%"),
        role := "progressbar",
        aria.valueNow <-- progress.value.map(_.toDouble),
        aria.label := progress.label
      )
    ),
    div(
      cls("flex gap-3 pt-1"),
      button(
        cls(
          "inline-flex items-center px-3 py-1.5 bg-indigo-600 hover:bg-indigo-700 active:bg-indigo-800 text-white text-xs font-medium rounded-lg transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
        ),
        "+10%",
        onClick --> { _ => progress.increment(10) }
      ),
      button(
        cls(
          "inline-flex items-center px-3 py-1.5 bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 active:bg-gray-100 text-xs font-medium rounded-lg transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-offset-2"
        ),
        "Reset",
        onClick --> { _ => progress.reset() }
      )
    )
  )
}
