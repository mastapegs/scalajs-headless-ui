package com.example.theme.inline.pages

import com.example.headless.components.PageContainer
import com.example.headless.pages.DashboardPage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object InlineDashboardPageView {
  def render(page: DashboardPage, theme: Theme): HtmlElement = theme.pageContainer(
    PageContainer(
      page.title,
      page.description,
      div(
        marginTop("24px"),
        display.flex,
        flexDirection.column,
        gap("16px"),
        page.counters.map(c => theme.counter(c))
      )
    )
  )
}
