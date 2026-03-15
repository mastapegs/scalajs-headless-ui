package com.example.theme.tailwind.pages

import com.example.headless.components.PageContainer
import com.example.headless.pages.SettingsPage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

/** Tailwind settings page with consistent heading and description styling.
  *
  * '''Design techniques:'''
  *   - '''Card wrapper''' (`bg-white rounded-xl shadow-sm border`) elevates the content from the gray background
  *   - '''Consistent typography''' matches the heading/description pattern used across all pages
  */
object TailwindSettingsPageView {
  def render(page: SettingsPage, theme: Theme): HtmlElement = theme.pageContainer(
    PageContainer(
      page.title,
      page.description,
      div(
        cls("bg-white rounded-xl shadow-md border border-gray-200 p-7"),
        p(cls("text-gray-600 text-sm leading-relaxed"), "Settings content will appear here.")
      )
    )
  )
}
