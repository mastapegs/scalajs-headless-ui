package com.example.theme.coreui.pages

import com.example.headless.pages.UIShowcasePage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object CoreUiUIShowcasePageView {

  def render(page: UIShowcasePage, theme: Theme): HtmlElement = div(
    cls("container-lg"),
    h1(cls("mb-2"), page.title),
    p(cls("text-body-secondary mb-4"), page.description),
    renderSection("Tabs", theme.tabs(page.tabs)),
    renderSection("Accordion", theme.accordion(page.accordion)),
    renderSection(
      "Toggle / Switch",
      div(
        display.flex,
        flexDirection.column,
        gap("16px"),
        theme.toggle(page.toggleDarkMode),
        theme.toggle(page.toggleNotifications)
      )
    ),
    renderSection("Progress", theme.progress(page.progress)),
    renderSection("Tags Input", theme.tagsInput(page.tagsInput)),
    renderSection("Tooltip", theme.tooltip(page.tooltip), overflow.visible)
  )

  private def renderSection(
      title: String,
      content: HtmlElement,
      extraMod: Mod[HtmlElement] = emptyMod
  ): HtmlElement = div(
    cls("card mb-4"),
    extraMod,
    div(
      cls("card-header"),
      h5(cls("card-title mb-0"), title)
    ),
    div(cls("card-body"), content)
  )
}
