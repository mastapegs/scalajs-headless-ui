package com.example.theme.coreui.pages

import com.example.headless.pages.SettingsPage
import com.raquo.laminar.api.L._

object CoreUiSettingsPageView {
  def render(page: SettingsPage): HtmlElement = div(
    cls("container-lg"),
    h1(cls("mb-3"), page.title),
    p(page.description)
  )
}
