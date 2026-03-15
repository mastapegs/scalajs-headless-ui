package com.example.theme.tailwind.pages

import com.example.headless.components.PageContainer
import com.example.headless.pages.DashboardPage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

/** Tailwind dashboard page with a clear heading hierarchy and a vertical stack of counter cards.
  *
  * '''Design techniques:'''
  *   - '''Vertical card stack''' (`flex flex-col gap-4`) provides consistent spacing between counters
  */
object TailwindDashboardPageView {
  def render(page: DashboardPage, theme: Theme): HtmlElement = theme.pageContainer(
    PageContainer(
      page.title,
      page.description,
      div(
        cls("flex flex-col gap-6"),
        page.counters.map(c => theme.counter(c))
      )
    )
  )
}
