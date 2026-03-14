package com.example.theme.tailwind.components

import com.example.headless.components.Progress
import com.raquo.laminar.api.L._

object TailwindProgressView {
  def render(progress: Progress): HtmlElement = div(
    div(
      cls("flex justify-between mb-2"),
      span(cls("text-sm font-medium text-gray-700"), progress.label),
      span(
        cls("text-sm text-gray-500"),
        child.text <-- progress.percentage.map(p => s"$p%")
      )
    ),
    div(
      cls("w-full bg-gray-200 rounded-full h-3 mb-3"),
      div(
        cls("bg-blue-600 h-3 rounded-full transition-all duration-300"),
        width <-- progress.percentage.map(p => s"$p%"),
        role := "progressbar",
        aria.valueNow <-- progress.value.map(_.toDouble),
        aria.label := progress.label
      )
    ),
    div(
      cls("flex gap-2"),
      button(
        cls("px-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white text-xs rounded transition-colors"),
        "+10%",
        onClick --> { _ => progress.increment(10) }
      ),
      button(
        cls("px-3 py-1.5 border border-gray-300 text-gray-600 hover:bg-gray-50 text-xs rounded transition-colors"),
        "Reset",
        onClick --> { _ => progress.reset() }
      )
    )
  )
}
