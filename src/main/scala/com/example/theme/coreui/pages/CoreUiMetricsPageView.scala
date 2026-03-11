package com.example.theme.coreui.pages

import com.example.headless.pages.MetricsPage
import com.raquo.laminar.api.L._

object CoreUiMetricsPageView {
  def render(page: MetricsPage): HtmlElement = div(
    cls("container-lg"),
    h1(cls("mb-3"), page.title),
    p(page.description)
  )
}
