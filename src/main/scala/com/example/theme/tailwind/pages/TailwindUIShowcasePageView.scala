package com.example.theme.tailwind.pages

import com.example.headless.pages.UIShowcasePage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

/** Tailwind UI showcase page that presents every headless component inside a consistent card layout.
  *
  * '''Design techniques:'''
  *   - '''Section cards''' — each component demo is wrapped in a `bg-white rounded-xl shadow-sm border` card with a
  *     subtle header bar (`border-b bg-gray-50/50`) creating a two-tone card look
  *   - '''Consistent vertical rhythm''' — `space-y-6` between sections and `p-6` inside each card keeps spacing uniform
  *   - '''Overflow visible''' on the Tooltip section so the tooltip popup can extend above the card boundary
  *   - '''Page header''' uses `text-2xl font-bold text-gray-900` with a muted `text-gray-500` description, matching the
  *     global page-heading convention
  */
object TailwindUIShowcasePageView {

  def render(page: UIShowcasePage, theme: Theme): HtmlElement = div(
    cls("max-w-5xl mx-auto"),
    h1(cls("text-2xl font-bold text-gray-900 mb-2"), page.title),
    p(cls("text-gray-500 mb-10"), page.description),
    div(
      cls("space-y-8"),
      renderSection("Tabs", theme.tabs(page.tabs)),
      renderSection("Accordion", theme.accordion(page.accordion)),
      renderSection(
        "Toggle / Switch",
        div(
          cls("flex flex-col gap-6"),
          theme.toggle(page.toggleDarkMode),
          theme.toggle(page.toggleNotifications)
        )
      ),
      renderSection("Progress", theme.progress(page.progress)),
      renderSection("Tags Input", theme.tagsInput(page.tagsInput)),
      renderSection("Tooltip", theme.tooltip(page.tooltip), overflow.visible)
    )
  )

  private def renderSection(
      title: String,
      content: HtmlElement,
      extraMod: Mod[HtmlElement] = emptyMod
  ): HtmlElement = div(
    cls("bg-white rounded-xl shadow-sm border border-gray-200/60 overflow-hidden"),
    extraMod,
    div(
      cls("px-7 py-5 border-b border-gray-100 bg-gray-50/50"),
      h3(cls("text-base font-semibold text-gray-800"), title)
    ),
    div(cls("p-7"), content)
  )
}
