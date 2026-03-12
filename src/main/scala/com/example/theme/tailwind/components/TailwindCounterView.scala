package com.example.theme.tailwind.components

import com.example.headless.components.Counter
import com.raquo.laminar.api.L._

object TailwindCounterView {
  def render(counter: Counter): HtmlElement = div(
    cls("bg-white rounded-lg shadow p-6 flex items-center gap-4"),
    span(
      cls("text-xl font-semibold"),
      aria.live   := "polite",
      aria.atomic := true,
      child.text <-- counter.count.map(_.toString)
    ),
    button(
      cls("bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded"),
      aria.label := "Increment counter",
      "Increment",
      onClick --> { _ => counter.increment() }
    )
  )
}
