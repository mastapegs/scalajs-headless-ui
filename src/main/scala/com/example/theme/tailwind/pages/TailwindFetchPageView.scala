package com.example.theme.tailwind.pages

import com.example.headless.pages.{FetchPage, FetchState, TableData}
import com.raquo.laminar.api.L._

/** Tailwind fetch page with loading spinner, error alert, and a striped data table.
  *
  * '''Design techniques:'''
  *   - '''Loading spinner''' — `animate-spin` on a rounded border element for a native-feeling loading indicator
  *   - '''Error alert''' — `bg-red-50 border-l-4 border-red-500 text-red-700` uses a left-border accent pattern (common
  *     in professional alert UIs) instead of a full red background
  *   - '''Table styling''' — `divide-y` for row separators, `bg-gray-50` on the header, and `hover:bg-gray-50` on rows
  *     for scan-friendly data reading
  *   - '''Card wrapper''' (`rounded-xl shadow-sm border overflow-hidden`) groups the table into a card surface
  *   - '''Uppercase tracking-wider headers''' follow the conventional Tailwind table-header pattern
  */
object TailwindFetchPageView {
  def render(page: FetchPage): HtmlElement = div(
    cls("max-w-5xl mx-auto"),
    h1(cls("text-2xl font-bold text-gray-900 mb-2"), page.title),
    p(cls("text-gray-500 mb-8"), page.description),
    div(
      child <-- page.state.map {
        case FetchState.Loading =>
          div(
            cls("flex justify-center py-12"),
            div(
              cls("animate-spin rounded-full h-10 w-10 border-4 border-gray-200 border-t-indigo-600")
            )
          )
        case FetchState.Error(msg) =>
          div(
            cls("bg-red-50 border-l-4 border-red-500 text-red-700 px-5 py-4 rounded-r-lg"),
            div(cls("font-semibold text-sm"), "Error"),
            div(cls("text-sm mt-1"), msg)
          )
        case FetchState.Success(_, tableData) =>
          renderTable(tableData)
      }
    )
  )

  private def renderTable(td: TableData): HtmlElement =
    div(
      cls("bg-white rounded-xl shadow-sm border border-gray-200/60 overflow-hidden"),
      div(
        cls("overflow-x-auto"),
        table(
          cls("min-w-full divide-y divide-gray-200"),
          thead(
            cls("bg-gray-50"),
            tr(
              td.headers.map(h =>
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
            td.rows.map(row =>
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
