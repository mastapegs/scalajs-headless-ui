package com.example.theme.inline.pages

import com.example.headless.pages.{FetchPage, FetchState, TableData}
import com.raquo.laminar.api.L._

object InlineFetchPageView {
  def render(page: FetchPage): HtmlElement = div(
    h1(marginBottom("16px"), page.title),
    p(page.description),
    div(
      marginTop("24px"),
      child <-- page.state.map {
        case FetchState.Loading => p("Loading...")
        case FetchState.Error(msg) =>
          p(color("red"), s"Error: $msg")
        case FetchState.Success(_, tableData) =>
          renderTable(tableData)
      }
    )
  )

  private def renderTable(td: TableData): HtmlElement =
    table(
      width("100%"),
      borderCollapse("collapse"),
      thead(
        tr(
          td.headers.map(h =>
            th(
              padding("8px 12px"),
              borderBottom("2px solid #ccc"),
              textAlign.left,
              fontWeight.bold,
              h
            )
          )
        )
      ),
      tbody(
        td.rows.map(row =>
          tr(
            row.map(cell =>
              htmlTag("td")(
                padding("8px 12px"),
                borderBottom("1px solid #eee"),
                cell
              )
            )
          )
        )
      )
    )
}
