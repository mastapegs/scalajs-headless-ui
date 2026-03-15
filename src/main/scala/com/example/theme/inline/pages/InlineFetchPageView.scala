package com.example.theme.inline.pages

import com.example.headless.components.PageContainer
import com.example.headless.pages.{FetchPage, FetchState}
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object InlineFetchPageView {
  def render(page: FetchPage, theme: Theme): HtmlElement = theme.pageContainer(
    PageContainer(
      page.title,
      page.description,
      div(
        marginTop("24px"),
        child <-- page.state.map {
          case FetchState.Loading => p("Loading...")
          case FetchState.Error(msg) =>
            p(color("red"), s"Error: $msg")
          case FetchState.Success(tables) =>
            div(tables.map(t => div(marginBottom("24px"), theme.table(t))): _*)
        }
      )
    )
  )
}
