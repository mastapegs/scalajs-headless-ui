package com.example.theme.coreui.pages

import com.example.headless.components.Counter
import com.example.headless.pages.DashboardPage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object CoreUiDashboardPageView {
  def render(page: DashboardPage, counter: Counter, theme: Theme): HtmlElement = div(
    cls("container-lg"),
    h1(cls("mb-3"), page.title),
    p(page.description),
    div(
      cls("mt-4"),
      theme.counter(counter)
    )
  )
}
