package com.example.theme.inline.pages

import com.example.headless.components.Counter
import com.example.headless.pages.DashboardPage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object InlineDashboardPageView {
  def render(page: DashboardPage, counter: Counter, theme: Theme): HtmlElement = div(
    h1(marginBottom("16px"), page.title),
    p(page.description),
    div(
      marginTop("24px"),
      theme.counter(counter)
    )
  )
}
