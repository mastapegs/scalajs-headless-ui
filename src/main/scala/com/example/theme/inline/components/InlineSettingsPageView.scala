package com.example.theme.inline.components

import com.example.headless.SettingsPage
import com.raquo.laminar.api.L._

object InlineSettingsPageView {
  def render(page: SettingsPage): HtmlElement = div(
    h1(marginBottom("16px"), page.title),
    p(page.description)
  )
}
