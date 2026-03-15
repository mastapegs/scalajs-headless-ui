package com.example.theme.inline.pages

import com.example.headless.components.PageContainer
import com.example.headless.pages.MetricsPage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object InlineMetricsPageView {
  def render(page: MetricsPage, theme: Theme): HtmlElement = theme.pageContainer(
    PageContainer(page.title, page.description, div())
  )
}
