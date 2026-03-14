package com.example.theme.coreui.pages

import com.example.headless.components.Card
import com.example.headless.pages.UIShowcasePage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object CoreUiUIShowcasePageView {

  def render(page: UIShowcasePage, theme: Theme): HtmlElement = div(
    cls("container-lg"),
    h1(cls("mb-2"), page.title),
    p(cls("text-body-secondary mb-4"), page.description),
    theme.card(new Card("Tabs"), theme.tabs(page.tabs)),
    theme.card(new Card("Accordion"), theme.accordion(page.accordion)),
    theme.card(
      new Card("Toggle / Switch"),
      div(
        display.flex,
        flexDirection.column,
        gap("16px"),
        theme.toggle(page.toggleDarkMode),
        theme.toggle(page.toggleNotifications)
      )
    ),
    theme.card(new Card("Progress"), theme.progress(page.progress)),
    theme.card(new Card("Tags Input"), theme.tagsInput(page.tagsInput)),
    theme.card(new Card("Tooltip"), theme.tooltip(page.tooltip)).amend(overflow.visible)
  )
}
