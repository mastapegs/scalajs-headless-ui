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
    theme.card(Card(span("Tabs"), theme.tabs(page.tabs))),
    theme.card(Card(span("Accordion"), theme.accordion(page.accordion))),
    theme.card(
      Card(
        span("Toggle / Switch"),
        div(
          display.flex,
          flexDirection.column,
          gap("16px"),
          theme.toggle(page.toggleDarkMode),
          theme.toggle(page.toggleNotifications)
        )
      )
    ),
    theme.card(Card(span("Progress"), theme.progress(page.progress))),
    theme.card(Card(span("Tags Input"), theme.tagsInput(page.tagsInput))),
    theme.card(Card(span("Tooltip"), theme.tooltip(page.tooltip))).amend(overflow.visible)
  )
}
