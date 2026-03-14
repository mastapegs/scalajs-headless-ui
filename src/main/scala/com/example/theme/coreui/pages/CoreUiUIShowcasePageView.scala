package com.example.theme.coreui.pages

import com.example.headless.components._
import com.example.headless.pages.UIShowcasePage
import com.raquo.laminar.api.L._

object CoreUiUIShowcasePageView {

  def render(page: UIShowcasePage): HtmlElement = div(
    cls("container-lg"),
    h1(cls("mb-2"), page.title),
    p(cls("text-body-secondary mb-4"), page.description),
    renderSection("Tabs", renderTabs(page.tabs)),
    renderSection("Accordion", renderAccordion(page.accordion)),
    renderSection("Toggle / Switch", renderToggles(page.toggleDarkMode, page.toggleNotifications)),
    renderSection("Progress", renderProgress(page.progress)),
    renderSection("Tags Input", renderTagsInput(page.tagsInput)),
    renderSection("Tooltip", renderTooltip(page.tooltip))
  )

  private def renderSection(title: String, content: HtmlElement): HtmlElement = div(
    cls("card mb-4"),
    div(
      cls("card-header"),
      h5(cls("card-title mb-0"), title)
    ),
    div(cls("card-body"), content)
  )

  private def renderTabs(tabs: Tabs): HtmlElement = div(
    htmlTag("ul")(
      cls("nav nav-tabs"),
      tabs.tabs.zipWithIndex.map { case (tab, idx) =>
        htmlTag("li")(
          cls("nav-item"),
          a(
            cls("nav-link"),
            cls <-- tabs.isSelected(idx).map(if (_) "active" else ""),
            href("#"),
            tab.label,
            onClick.preventDefault --> { _ => tabs.select(idx) }
          )
        )
      }
    ),
    div(
      cls("tab-content p-3 border border-top-0 rounded-bottom"),
      child.text <-- tabs.selectedTab.map(_.content)
    )
  )

  private def renderAccordion(accordion: Accordion): HtmlElement = div(
    cls("accordion"),
    accordion.items.map { item =>
      div(
        cls("accordion-item"),
        h2(
          cls("accordion-header"),
          button(
            cls("accordion-button"),
            cls <-- accordion.isOpen(item.key).map(if (_) "" else "collapsed"),
            tpe := "button",
            item.title,
            onClick --> { _ => accordion.toggle(item.key) }
          )
        ),
        div(
          cls("accordion-collapse collapse"),
          cls <-- accordion.isOpen(item.key).map(if (_) "show" else ""),
          div(cls("accordion-body"), item.content)
        )
      )
    }
  )

  private def renderToggles(darkMode: Toggle, notifications: Toggle): HtmlElement = div(
    cls("d-flex flex-column gap-3"),
    renderSingleToggle(darkMode),
    renderSingleToggle(notifications)
  )

  private def renderSingleToggle(toggle: Toggle): HtmlElement = div(
    cls("form-check form-switch"),
    input(
      cls("form-check-input"),
      tpe    := "checkbox",
      role   := "switch",
      idAttr := s"toggle-${toggle.label.toLowerCase.replace(" ", "-")}",
      controlled(
        checked <-- toggle.isOn,
        onChange.mapToChecked --> { _ => toggle.toggle() }
      )
    ),
    label(
      cls("form-check-label"),
      forId := s"toggle-${toggle.label.toLowerCase.replace(" ", "-")}",
      toggle.label,
      span(
        cls("badge ms-2"),
        cls <-- toggle.isOn.map(if (_) "text-bg-success" else "text-bg-secondary"),
        child.text <-- toggle.isOn.map(if (_) "ON" else "OFF")
      )
    )
  )

  private def renderProgress(progress: Progress): HtmlElement = div(
    div(
      cls("d-flex justify-content-between mb-2"),
      span(progress.label),
      span(
        cls("text-body-secondary"),
        child.text <-- progress.percentage.map(p => s"$p%")
      )
    ),
    div(
      cls("progress mb-3"),
      div(
        cls("progress-bar"),
        role := "progressbar",
        aria.valueNow <-- progress.value.map(_.toDouble),
        aria.label := progress.label,
        width <-- progress.percentage.map(p => s"$p%"),
        child.text <-- progress.percentage.map(p => s"$p%")
      )
    ),
    div(
      cls("btn-group btn-group-sm"),
      button(cls("btn btn-primary"), "+10%", onClick --> { _ => progress.increment(10) }),
      button(cls("btn btn-outline-secondary"), "Reset", onClick --> { _ => progress.reset() })
    )
  )

  private def renderTagsInput(tagsInput: TagsInput): HtmlElement = div(
    div(
      cls("d-flex flex-wrap gap-2 align-items-center p-2 border rounded"),
      minHeight("44px"),
      children <-- tagsInput.tags.map(_.map { tag =>
        span(
          cls("badge text-bg-primary d-inline-flex align-items-center gap-1"),
          fontSize("13px"),
          padding("6px 10px"),
          tag,
          button(
            cls("btn-close btn-close-white"),
            fontSize("10px"),
            onClick --> { _ => tagsInput.removeTag(tag) }
          )
        )
      }),
      input(
        cls("form-control border-0 shadow-none"),
        flexGrow(1),
        minWidth("120px"),
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
    small(
      cls("text-body-secondary mt-1 d-block"),
      child.text <-- tagsInput.tagCount.map(c => s"$c tag(s) added")
    )
  )

  private def renderTooltip(tooltip: Tooltip): HtmlElement = div(
    cls("d-flex align-items-center gap-3"),
    div(
      position.relative,
      display.inlineBlock,
      button(
        cls("btn btn-primary"),
        "Hover me",
        onMouseEnter --> { _ => tooltip.show() },
        onMouseLeave --> { _ => tooltip.hide() }
      ),
      div(
        cls("tooltip bs-tooltip-top"),
        position.absolute,
        bottom("calc(100% + 4px)"),
        left("50%"),
        transform  := "translateX(-50%)",
        transition := "opacity 0.2s",
        opacity <-- tooltip.isVisible.map(if (_) "1" else "0"),
        display.block,
        div(cls("tooltip-inner"), tooltip.text)
      )
    ),
    span(
      cls("text-body-secondary"),
      child.text <-- tooltip.isVisible.map(if (_) "Tooltip visible" else "Hover the button to see the tooltip")
    )
  )
}
