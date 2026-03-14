package com.example.theme.tailwind.pages

import com.example.headless.pages.MetricsPage
import com.raquo.laminar.api.L._

/** Tailwind metrics page with consistent heading and description styling.
  *
  * '''Design techniques:'''
  *   - '''Card wrapper''' (`bg-white rounded-xl shadow-sm border`) elevates the content from the gray background
  *   - '''Consistent typography''' matches the heading/description pattern used across all pages
  */
object TailwindMetricsPageView {
  def render(page: MetricsPage): HtmlElement = div(
    cls("max-w-5xl mx-auto"),
    h1(cls("text-2xl font-bold text-gray-900 mb-2"), page.title),
    p(cls("text-gray-500 mb-8"), page.description),
    div(
      cls("bg-white rounded-xl shadow-sm border border-gray-200/60 p-7"),
      p(cls("text-gray-600 text-sm leading-relaxed"), "Metrics content will appear here.")
    )
  )
}
