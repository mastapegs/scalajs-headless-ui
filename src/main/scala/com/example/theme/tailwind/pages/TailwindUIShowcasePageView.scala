package com.example.theme.tailwind.pages

import com.example.headless.components._
import com.example.headless.pages.UIShowcasePage
import com.raquo.laminar.api.L._

object TailwindUIShowcasePageView {

  def render(page: UIShowcasePage): HtmlElement = div(
    cls("max-w-5xl mx-auto"),
    h1(cls("text-2xl font-bold mb-2"), page.title),
    p(cls("text-gray-500 mb-8"), page.description),
    renderSection("Tabs", renderTabs(page.tabs)),
    renderSection("Accordion", renderAccordion(page.accordion)),
    renderSection("Toggle / Switch", renderToggles(page.toggleDarkMode, page.toggleNotifications)),
    renderSection("Progress", renderProgress(page.progress)),
    renderSection("Tags Input", renderTagsInput(page.tagsInput)),
    renderSection("Tooltip", renderTooltip(page.tooltip))
  )

  private def renderSection(title: String, content: HtmlElement): HtmlElement = div(
    cls("bg-white rounded-lg shadow mb-6 overflow-hidden"),
    div(
      cls("px-6 py-4 border-b border-gray-200"),
      h3(cls("text-lg font-semibold text-gray-800"), title)
    ),
    div(cls("p-6"), content)
  )

  private def renderTabs(tabs: Tabs): HtmlElement = div(
    div(
      cls("flex border-b border-gray-200 mb-4"),
      tabs.tabs.zipWithIndex.map { case (tab, idx) =>
        button(
          cls("px-5 py-2.5 text-sm font-medium transition-colors"),
          cls <-- tabs
            .isSelected(idx)
            .map(
              if (_) "text-blue-600 border-b-2 border-blue-600"
              else "text-gray-500 hover:text-gray-700"
            ),
          tab.label,
          onClick --> { _ => tabs.select(idx) }
        )
      }
    ),
    div(
      cls("p-4 bg-gray-50 rounded text-gray-700 text-sm"),
      child.text <-- tabs.selectedTab.map(_.content)
    )
  )

  private def renderAccordion(accordion: Accordion): HtmlElement = div(
    cls("border border-gray-200 rounded-lg divide-y divide-gray-200"),
    accordion.items.map { item =>
      div(
        button(
          cls(
            "flex justify-between items-center w-full px-4 py-3 text-left text-sm font-medium text-gray-700 hover:bg-gray-50 transition-colors"
          ),
          span(item.title),
          span(
            cls("transition-transform duration-200"),
            cls <-- accordion.isOpen(item.key).map(if (_) "rotate-180" else ""),
            "\u25BC"
          ),
          onClick --> { _ => accordion.toggle(item.key) }
        ),
        div(
          cls("overflow-hidden transition-all duration-300"),
          maxHeight <-- accordion.isOpen(item.key).map(if (_) "200px" else "0"),
          div(
            cls("px-4 py-3 text-sm text-gray-600"),
            item.content
          )
        )
      )
    }
  )

  private def renderToggles(darkMode: Toggle, notifications: Toggle): HtmlElement = div(
    cls("flex flex-col gap-4"),
    renderSingleToggle(darkMode),
    renderSingleToggle(notifications)
  )

  private def renderSingleToggle(toggle: Toggle): HtmlElement = div(
    cls("flex items-center gap-3"),
    button(
      cls("relative inline-flex h-6 w-11 items-center rounded-full transition-colors"),
      cls <-- toggle.isOn.map(if (_) "bg-blue-600" else "bg-gray-300"),
      role := "switch",
      aria.checked <-- toggle.isOn.map(_.toString),
      aria.label := toggle.label,
      span(
        cls("inline-block h-4 w-4 rounded-full bg-white transition-transform"),
        cls <-- toggle.isOn.map(if (_) "translate-x-6" else "translate-x-1")
      ),
      onClick --> { _ => toggle.toggle() }
    ),
    span(cls("text-sm font-medium text-gray-700"), toggle.label),
    span(
      cls("text-xs px-2 py-0.5 rounded-full"),
      cls <-- toggle.isOn.map(if (_) "bg-green-100 text-green-700" else "bg-gray-100 text-gray-500"),
      child.text <-- toggle.isOn.map(if (_) "ON" else "OFF")
    )
  )

  private def renderProgress(progress: Progress): HtmlElement = div(
    div(
      cls("flex justify-between mb-2"),
      span(cls("text-sm font-medium text-gray-700"), progress.label),
      span(
        cls("text-sm text-gray-500"),
        child.text <-- progress.percentage.map(p => s"$p%")
      )
    ),
    div(
      cls("w-full bg-gray-200 rounded-full h-3 mb-3"),
      div(
        cls("bg-blue-600 h-3 rounded-full transition-all duration-300"),
        width <-- progress.percentage.map(p => s"$p%"),
        role := "progressbar",
        aria.valueNow <-- progress.value.map(_.toDouble),
        aria.label := progress.label
      )
    ),
    div(
      cls("flex gap-2"),
      button(
        cls("px-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white text-xs rounded transition-colors"),
        "+10%",
        onClick --> { _ => progress.increment(10) }
      ),
      button(
        cls("px-3 py-1.5 border border-gray-300 text-gray-600 hover:bg-gray-50 text-xs rounded transition-colors"),
        "Reset",
        onClick --> { _ => progress.reset() }
      )
    )
  )

  private def renderTagsInput(tagsInput: TagsInput): HtmlElement = div(
    div(
      cls("flex flex-wrap gap-2 items-center p-2 border border-gray-300 rounded-lg min-h-[44px]"),
      children <-- tagsInput.tags.map(_.map { tag =>
        span(
          cls("inline-flex items-center gap-1 px-3 py-1 bg-blue-600 text-white rounded-full text-sm"),
          tag,
          button(
            cls("hover:text-blue-200 text-white font-bold"),
            "\u00D7",
            onClick --> { _ => tagsInput.removeTag(tag) }
          )
        )
      }),
      input(
        cls("flex-grow min-w-[120px] border-0 outline-none text-sm"),
        placeholder := "Type and press Enter...",
        controlled(
          value <-- tagsInput.input,
          onInput.mapToValue --> { v => tagsInput.setInput(v) }
        ),
        onKeyDown --> { ev =>
          if (ev.key == "Enter") {
            ev.preventDefault()
            val inputEl = ev.target.asInstanceOf[org.scalajs.dom.html.Input]
            tagsInput.addTag(inputEl.value)
          } else if (ev.key == "Backspace" && ev.target.asInstanceOf[org.scalajs.dom.html.Input].value.isEmpty) {
            tagsInput.removeLastTag()
          }
        }
      )
    ),
    p(
      cls("text-xs text-gray-500 mt-1"),
      child.text <-- tagsInput.tagCount.map(c => s"$c tag(s) added")
    )
  )

  private def renderTooltip(tooltip: Tooltip): HtmlElement = div(
    cls("flex items-center gap-4"),
    div(
      cls("relative inline-block"),
      button(
        cls("px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded transition-colors text-sm"),
        "Hover me",
        onMouseEnter --> { _ => tooltip.show() },
        onMouseLeave --> { _ => tooltip.hide() }
      ),
      div(
        cls(
          "absolute bottom-full left-1/2 -translate-x-1/2 mb-2 px-3 py-2 bg-gray-900 text-white text-xs rounded whitespace-nowrap pointer-events-none transition-opacity"
        ),
        opacity <-- tooltip.isVisible.map(if (_) "1" else "0"),
        tooltip.text
      )
    ),
    span(
      cls("text-sm text-gray-500"),
      child.text <-- tooltip.isVisible.map(if (_) "Tooltip visible" else "Hover the button to see the tooltip")
    )
  )
}
