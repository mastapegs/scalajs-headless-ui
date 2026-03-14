package com.example.theme.inline.pages

import com.example.headless.pages.UIShowcasePage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object InlineUIShowcasePageView {

  def render(page: UIShowcasePage, theme: Theme): HtmlElement = div(
    h1(marginBottom("8px"), page.title),
    p(marginBottom("32px"), color("#6c757d"), page.description),
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
    renderSection("Tooltip", theme.tooltip(page.tooltip))
  )

  private def renderSection(title: String, content: HtmlElement): HtmlElement = div(
    marginBottom("32px"),
    h2(
      fontSize("18px"),
      fontWeight("600"),
      marginBottom("12px"),
      borderBottom("2px solid #2c3e50"),
      paddingBottom("8px"),
      title
    ),
    content
  )
}
