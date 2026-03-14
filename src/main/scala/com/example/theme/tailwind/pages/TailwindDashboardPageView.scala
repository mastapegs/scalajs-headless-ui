package com.example.theme.tailwind.pages

import com.example.headless.pages.DashboardPage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

/** Tailwind dashboard page with a clear heading hierarchy and a vertical stack of counter cards.
  *
  * '''Design techniques:'''
  *   - '''Max-width container''' (`max-w-5xl mx-auto`) keeps content readable on wide screens
  *   - '''Page heading''' uses `text-2xl font-bold text-gray-900` for clear visual hierarchy
  *   - '''Muted description''' (`text-gray-500`) distinguishes helper text from the title
  *   - '''Vertical card stack''' (`flex flex-col gap-4`) provides consistent spacing between counters
  */
object TailwindDashboardPageView {
  def render(page: DashboardPage, theme: Theme): HtmlElement = div(
    cls("max-w-5xl mx-auto"),
    h1(cls("text-2xl font-bold text-gray-900 mb-2"), page.title),
    p(cls("text-gray-500 mb-8"), page.description),
    div(
      cls("flex flex-col gap-5"),
      page.counters.map(c => theme.counter(c))
    )
  )
}
