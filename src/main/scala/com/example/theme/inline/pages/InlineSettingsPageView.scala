package com.example.theme.inline.pages

import com.example.headless.components.PageContainer
import com.example.headless.pages.SettingsPage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object InlineSettingsPageView {
  def render(page: SettingsPage, theme: Theme): HtmlElement = theme.pageContainer(
    PageContainer(page.title, page.description, emptyNode)
  )
}
