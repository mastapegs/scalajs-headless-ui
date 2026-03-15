package com.example.theme.tailwind.components

import com.example.headless.components.Table
import com.raquo.laminar.api.L._

/** Tailwind table with card wrapper, striped rows, and hover effects.
  *
  * '''Design techniques:'''
  *   - '''Card wrapper''' (`rounded-xl shadow-md border overflow-hidden`) groups the table into a card surface
  *   - '''Table styling''' — `divide-y` for row separators, `bg-gray-50` on the header, and `hover:bg-gray-50` on rows
  *     for scan-friendly data reading
  *   - '''Uppercase tracking-wider headers''' follow the conventional Tailwind table-header pattern
  */
object TailwindTableView {
  def render(t: Table): HtmlElement =
    div(
      cls("bg-white rounded-xl shadow-md border border-gray-200 overflow-hidden"),
      div(
        cls("overflow-x-auto"),
        table(
          cls("min-w-full divide-y divide-gray-200"),
          t.caption.map(c => htmlTag("caption")(cls("px-6 py-3 text-left text-sm font-semibold text-gray-700"), c)),
          thead(
            cls("bg-gray-50"),
            tr(
              t.headers.map(h =>
                th(
                  cls(
                    "px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider"
                  ),
                  h
                )
              )
            )
          ),
          tbody(
            cls("bg-white divide-y divide-gray-100"),
            t.rows.map(row =>
              tr(
                cls("hover:bg-gray-50 transition-colors duration-100"),
                row.map(cell => htmlTag("td")(cls("px-6 py-4 whitespace-nowrap text-sm text-gray-700"), cell))
              )
            )
          )
        )
      )
    )
}
