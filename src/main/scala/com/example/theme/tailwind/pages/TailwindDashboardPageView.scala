package com.example.theme.tailwind.pages

import com.example.headless.pages.DashboardPage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object TailwindDashboardPageView {
  def render(page: DashboardPage, theme: Theme): HtmlElement = div(
    cls("max-w-5xl mx-auto"),
    h1(cls("text-2xl font-bold mb-3"), page.title),
    p(page.description),
    div(
      cls("mt-4 flex flex-col gap-4"),
      page.counters.map(theme.counter): _*
    )
  )
}
