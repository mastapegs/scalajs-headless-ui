package com.example.theme.inline.pages

import com.example.headless.MetricsPage
import com.raquo.laminar.api.L._

object InlineMetricsPageView {
  def render(page: MetricsPage): HtmlElement = div(
    h1(marginBottom("16px"), page.title),
    p(page.description)
  )
}
