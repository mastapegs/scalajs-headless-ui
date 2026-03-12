package com.example.theme.tailwind.pages

import com.example.headless.pages.{FetchPage, FetchState, TableData}
import com.raquo.laminar.api.L._

object TailwindFetchPageView {
  def render(page: FetchPage): HtmlElement = div(
    cls("max-w-5xl mx-auto"),
    h1(cls("text-2xl font-bold mb-3"), page.title),
    p(page.description),
    div(
      cls("mt-6"),
      child <-- page.state.map {
        case FetchState.Loading =>
          div(
            cls("flex justify-center"),
            div(cls("animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"))
          )
        case FetchState.Error(msg) =>
          div(cls("bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded"), s"Error: $msg")
        case FetchState.Success(_, tableData) =>
          renderTable(tableData)
      }
    )
  )

  private def renderTable(td: TableData): HtmlElement =
    div(
      cls("overflow-x-auto"),
      table(
        cls("min-w-full divide-y divide-gray-200"),
        thead(
          cls("bg-gray-50"),
          tr(
            td.headers.map(h =>
              th(cls("px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"), h)
            )
          )
        ),
        tbody(
          cls("bg-white divide-y divide-gray-200"),
          td.rows.map(row =>
            tr(
              row.map(cell => htmlTag("td")(cls("px-6 py-4 whitespace-nowrap text-sm text-gray-900"), cell))
            )
          )
        )
      )
    )
}
