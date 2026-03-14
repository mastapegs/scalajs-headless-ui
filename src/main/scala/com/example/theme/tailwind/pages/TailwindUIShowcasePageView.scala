package com.example.theme.tailwind.pages

import com.example.headless.components.Card
import com.example.headless.pages.UIShowcasePage
import com.example.theme.Theme
import com.raquo.laminar.api.L._

/** Tailwind UI showcase page that presents every headless component inside a consistent card layout.
  *
  * '''Design techniques:'''
  *   - '''Section cards''' — each component demo is wrapped via `theme.card()` which renders a `bg-white rounded-xl
  *     shadow-md border` card with a subtle header bar (`border-b bg-gray-50/50`) creating a two-tone card look
  *   - '''Flex-col gap layout''' — uses `flex flex-col gap-8` instead of `space-y-*` to avoid Tailwind v4 margin
  *     collapsing issues with the `space-y` utility
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
      cls("flex flex-col gap-8"),
      theme.card(Card(span("Tabs"), theme.tabs(page.tabs))),
      theme.card(Card(span("Accordion"), theme.accordion(page.accordion))),
      theme.card(
        Card(
          span("Toggle / Switch"),
          div(
            cls("flex flex-col gap-6"),
            theme.toggle(page.toggleDarkMode),
            theme.toggle(page.toggleNotifications)
          )
        )
      ),
      theme.card(Card(span("Progress"), theme.progress(page.progress))),
      theme.card(Card(span("Tags Input"), theme.tagsInput(page.tagsInput))),
      theme.card(Card(span("Tooltip"), theme.tooltip(page.tooltip))).amend(overflow.visible)
    )
  )
}
