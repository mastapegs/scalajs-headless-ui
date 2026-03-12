package com.example.theme.tailwind.pages

import com.example.headless.pages.MetricsPage
import com.raquo.laminar.api.L._

object TailwindMetricsPageView {
  def render(page: MetricsPage): HtmlElement = div(
    cls("max-w-5xl mx-auto"),
    h1(cls("text-2xl font-bold mb-3"), page.title),
    p(page.description)
  )
}
