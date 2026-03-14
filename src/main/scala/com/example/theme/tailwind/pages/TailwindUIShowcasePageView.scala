package com.example.theme.tailwind.pages

import com.example.headless.pages.UIShowcasePage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

object TailwindUIShowcasePageView {

  def render(page: UIShowcasePage, theme: Theme): HtmlElement = div(
    cls("max-w-5xl mx-auto"),
    h1(cls("text-2xl font-bold mb-2"), page.title),
    p(cls("text-gray-500 mb-8"), page.description),
    renderSection("Tabs", theme.tabs(page.tabs)),
    renderSection("Accordion", theme.accordion(page.accordion)),
    renderSection(
      "Toggle / Switch",
      div(
        cls("flex flex-col gap-4"),
        theme.toggle(page.toggleDarkMode),
        theme.toggle(page.toggleNotifications)
      )
    ),
    renderSection("Progress", theme.progress(page.progress)),
    renderSection("Tags Input", theme.tagsInput(page.tagsInput)),
    renderSection("Tooltip", theme.tooltip(page.tooltip))
  )

  private def renderSection(title: String, content: HtmlElement): HtmlElement = div(
    cls("bg-white rounded-lg shadow mb-6 overflow-hidden"),
    div(
      cls("px-6 py-4 border-b border-gray-200"),
      h3(cls("text-lg font-semibold text-gray-800"), title)
    ),
    div(cls("p-6"), content)
  )
}
