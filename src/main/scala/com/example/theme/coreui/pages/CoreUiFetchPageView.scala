package com.example.theme.coreui.pages

import com.example.headless.components.PageContainer
import com.example.headless.pages.{FetchPage, FetchState, TableData}
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object CoreUiFetchPageView {
  def render(page: FetchPage, theme: Theme): HtmlElement = theme.pageContainer(
    PageContainer(
      page.title,
      page.description,
      div(
        cls("mt-4"),
        child <-- page.state.map {
          case FetchState.Loading =>
            div(cls("text-center"), div(cls("spinner-border"), role("status")))
          case FetchState.Error(msg) =>
            div(cls("alert alert-danger"), s"Error: $msg")
          case FetchState.Success(_, tableData) =>
            renderTable(tableData)
        }
      )
    )
  )

  private def renderTable(td: TableData): HtmlElement =
    div(
      cls("table-responsive"),
      table(
        cls("table table-striped table-hover"),
        thead(
          tr(td.headers.map(h => th(cls("text-nowrap"), h)))
        ),
        tbody(
          td.rows.map(row => tr(row.map(cell => htmlTag("td")(cell))))
        )
      )
    )
}
