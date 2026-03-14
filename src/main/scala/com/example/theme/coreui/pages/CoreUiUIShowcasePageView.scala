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
    renderSection("Tooltip", renderTooltip(page.tooltip), overflow.visible)
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
        // CoreUI's .collapse requires JS to animate, but we manage state ourselves.
        // Use inline overflow/maxHeight for reliable show/hide without depending on
        // CoreUI's JS collapse plugin conflicting with our headless state.
        div(
          overflow.hidden,
          maxHeight <-- accordion.isOpen(item.key).map(if (_) "500px" else "0"),
          transition := "max-height 0.3s ease",
          div(cls("accordion-body"), item.content)
        )
      )
    }
  )

  private def renderToggles(darkMode: Toggle, notifications: Toggle): HtmlElement = div(
    display.flex,
    flexDirection.column,
    gap("16px"),
    renderSingleToggle(darkMode),
    renderSingleToggle(notifications)
  )

  private def renderSingleToggle(toggle: Toggle): HtmlElement = div(
    display.flex,
    alignItems.center,
    gap("12px"),
    button(
      width("48px"),
      height("26px"),
      borderRadius("13px"),
      border("none"),
      cursor.pointer,
      position.relative,
      transition := "background-color 0.2s",
      backgroundColor <-- toggle.isOn.map(if (_) "var(--cui-primary, #321fdb)" else "#ccc"),
      role := "switch",
      aria.checked <-- toggle.isOn.map(_.toString),
      aria.label := toggle.label,
      span(
        position.absolute,
        top("3px"),
        width("20px"),
        height("20px"),
        borderRadius("50%"),
        backgroundColor("white"),
        transition := "left 0.2s",
        left <-- toggle.isOn.map(if (_) "25px" else "3px")
      ),
      onClick --> { _ => toggle.toggle() }
    ),
    span(fontSize("14px"), fontWeight("500"), toggle.label),
    span(
      display.inlineFlex,
      padding("2px 8px"),
      borderRadius("12px"),
      fontSize("12px"),
      backgroundColor <-- toggle.isOn.map(if (_) "#d4edda" else "#e9ecef"),
      color <-- toggle.isOn.map(if (_) "#155724" else "#6c757d"),
      child.text <-- toggle.isOn.map(if (_) "ON" else "OFF")
    )
  )

  private def renderProgress(progress: Progress): HtmlElement = div(
    display.flex,
    flexDirection.column,
    gap("12px"),
    div(
      display.flex,
      justifyContent.spaceBetween,
      span(fontSize("14px"), fontWeight("500"), progress.label),
      span(
        fontSize("14px"),
        color("#6c757d"),
        child.text <-- progress.percentage.map(p => s"$p%")
      )
    ),
    div(
      height("12px"),
      backgroundColor("#ebedef"),
      borderRadius("6px"),
      overflow.hidden,
      div(
        height("100%"),
        backgroundColor("var(--cui-primary, #321fdb)"),
        borderRadius("6px"),
        transition := "width 0.3s ease",
        width <-- progress.percentage.map(p => s"$p%"),
        role := "progressbar",
        aria.valueNow <-- progress.value.map(_.toDouble),
        aria.label := progress.label
      )
    ),
    div(
      display.flex,
      gap("8px"),
      button(cls("btn btn-sm btn-primary"), "+10%", onClick --> { _ => progress.increment(10) }),
      button(cls("btn btn-sm btn-outline-secondary"), "Reset", onClick --> { _ => progress.reset() })
    )
  )

  private def renderTagsInput(tagsInput: TagsInput): HtmlElement = div(
    div(
      display.flex,
      flexWrap.wrap,
      gap("8px"),
      alignItems.center,
      padding("8px"),
      border("1px solid #dee2e6"),
      borderRadius("8px"),
      minHeight("44px"),
      children <-- tagsInput.tags.map(_.map { tag =>
        span(
          display.inlineFlex,
          alignItems.center,
          gap("4px"),
          padding("4px 10px"),
          backgroundColor("var(--cui-primary, #321fdb)"),
          color("white"),
          borderRadius("16px"),
          fontSize("13px"),
          tag,
          button(
            tpe := "button",
            border("none"),
            backgroundColor("transparent"),
            color("white"),
            cursor.pointer,
            fontSize("14px"),
            padding("0 2px"),
            aria.label := "Remove",
            "\u00D7",
            onClick --> { _ => tagsInput.removeTag(tag) }
          )
        )
      }),
      input(
        border("none"),
        outline("none"),
        fontSize("14px"),
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
    div(
      marginTop("8px"),
      fontSize("12px"),
      color("#6c757d"),
      child.text <-- tagsInput.tagCount.map(c => s"$c tag(s) added")
    )
  )

  private def renderTooltip(tooltip: Tooltip): HtmlElement = div(
    display.flex,
    alignItems.center,
    gap("16px"),
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
        position.absolute,
        bottom("calc(100% + 8px)"),
        left("50%"),
        transform := "translateX(-50%)",
        padding("6px 12px"),
        backgroundColor("var(--cui-dark, #212631)"),
        color("white"),
        borderRadius("4px"),
        fontSize("12px"),
        whiteSpace.nowrap,
        pointerEvents := "none",
        transition    := "opacity 0.2s",
        zIndex(10),
        opacity <-- tooltip.isVisible.map(if (_) "1" else "0"),
        tooltip.text
      )
    ),
    span(
      fontSize("14px"),
      color("#6c757d"),
      child.text <-- tooltip.isVisible.map(if (_) "Tooltip visible" else "Hover the button to see the tooltip")
    )
  )
}
