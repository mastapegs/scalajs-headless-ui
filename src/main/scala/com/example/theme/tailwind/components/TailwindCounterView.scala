package com.example.theme.tailwind.components

import com.example.headless.components.Counter
import com.raquo.laminar.api.L._

/** Tailwind counter card with elevated surface, clear typographic hierarchy, and an indigo accent button.
  *
  * '''Design techniques:'''
  *   - '''Card surface''' (`bg-white rounded-xl shadow-sm border border-gray-200/60`) for a clean, elevated look
  *   - '''Hover shadow lift''' (`hover:shadow-md transition-shadow`) provides subtle interactivity feedback
  *   - '''Typographic hierarchy''' — small muted label, large bold value, medium button text
  *   - '''Indigo primary button''' with `focus:ring-2 focus:ring-offset-2` for accessible focus indication
  *   - '''`aria-live` region''' on the count ensures screen readers announce value changes
  */
object TailwindCounterView {
  def render(counter: Counter): HtmlElement = div(
    cls(
      "bg-white rounded-xl shadow-sm border border-gray-200/60 p-6 flex items-center gap-5 hover:shadow-md transition-shadow duration-200"
    ),
    div(
      cls("flex-1 min-w-0"),
      div(cls("text-sm font-medium text-gray-500 mb-1"), counter.label),
      div(
        cls("text-3xl font-bold text-gray-900 tabular-nums"),
        aria.live   := "polite",
        aria.atomic := true,
        child.text <-- counter.count.map(_.toString)
      )
    ),
    button(
      cls(
        "inline-flex items-center px-4 py-2 bg-indigo-600 hover:bg-indigo-700 active:bg-indigo-800 text-white text-sm font-medium rounded-lg transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
      ),
      aria.label := "Increment counter",
      "Increment",
      onClick --> { _ => counter.increment() }
    )
  )
}
