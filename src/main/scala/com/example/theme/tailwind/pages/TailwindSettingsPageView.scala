package com.example.theme.tailwind.pages

import com.example.headless.pages.SettingsPage
import com.raquo.laminar.api.L._

/** Tailwind settings page with consistent heading and description styling.
  *
  * '''Design techniques:'''
  *   - '''Card wrapper''' (`bg-white rounded-xl shadow-sm border`) elevates the content from the gray background
  *   - '''Consistent typography''' matches the heading/description pattern used across all pages
  */
object TailwindSettingsPageView {
  def render(page: SettingsPage): HtmlElement = div(
    cls("max-w-5xl mx-auto"),
    h1(cls("text-2xl font-bold text-gray-900 mb-1"), page.title),
    p(cls("text-gray-500 mb-6"), page.description),
    div(
      cls("bg-white rounded-xl shadow-sm border border-gray-200/60 p-6"),
      p(cls("text-gray-600 text-sm leading-relaxed"), "Settings content will appear here.")
    )
  )
}
