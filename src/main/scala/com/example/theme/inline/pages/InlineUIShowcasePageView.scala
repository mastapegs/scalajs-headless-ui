package com.example.theme.inline.pages

import com.example.headless.components._
import com.example.headless.pages.UIShowcasePage
import com.raquo.laminar.api.L._

object InlineUIShowcasePageView {

  def render(page: UIShowcasePage): HtmlElement = div(
    h1(marginBottom("8px"), page.title),
    p(marginBottom("32px"), color("#6c757d"), page.description),
    renderSection("Tabs", renderTabs(page.tabs)),
    renderSection("Accordion", renderAccordion(page.accordion)),
    renderSection("Toggle / Switch", renderToggles(page.toggleDarkMode, page.toggleNotifications)),
    renderSection("Progress", renderProgress(page.progress)),
    renderSection("Tags Input", renderTagsInput(page.tagsInput)),
    renderSection("Tooltip", renderTooltip(page.tooltip))
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

  private def renderTabs(tabs: Tabs): HtmlElement = div(
    div(
      display.flex,
      borderBottom("1px solid #dee2e6"),
      marginBottom("16px"),
      tabs.tabs.zipWithIndex.map { case (tab, idx) =>
        button(
          padding("10px 20px"),
          border("none"),
          backgroundColor("transparent"),
          cursor.pointer,
          fontSize("14px"),
          fontWeight("500"),
          borderBottom <-- tabs.isSelected(idx).map(if (_) "3px solid #2c3e50" else "3px solid transparent"),
          color <-- tabs.isSelected(idx).map(if (_) "#2c3e50" else "#6c757d"),
          tab.label,
          onClick --> { _ => tabs.select(idx) }
        )
      }
    ),
    div(
      padding("16px"),
      backgroundColor("#f8f9fa"),
      borderRadius("4px"),
      child.text <-- tabs.selectedTab.map(_.content)
    )
  )

  private def renderAccordion(accordion: Accordion): HtmlElement = div(
    border("1px solid #dee2e6"),
    borderRadius("8px"),
    overflow.hidden,
    accordion.items.zipWithIndex.map { case (item, idx) =>
      div(
        if (idx > 0) borderTop("1px solid #dee2e6") else emptyMod,
        button(
          display.flex,
          justifyContent.spaceBetween,
          alignItems.center,
          width("100%"),
          padding("12px 16px"),
          border("none"),
          backgroundColor("#f8f9fa"),
          cursor.pointer,
          fontSize("14px"),
          fontWeight("500"),
          span(item.title),
          span(
            transition := "transform 0.2s",
            transform <-- accordion.isOpen(item.key).map(if (_) "rotate(180deg)" else "rotate(0)"),
            "\u25BC"
          ),
          onClick --> { _ => accordion.toggle(item.key) }
        ),
        div(
          overflow.hidden,
          maxHeight <-- accordion.isOpen(item.key).map(if (_) "200px" else "0"),
          transition := "max-height 0.3s ease",
          div(
            padding("12px 16px"),
            fontSize("14px"),
            color("#495057"),
            item.content
          )
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
      backgroundColor <-- toggle.isOn.map(if (_) "#2c3e50" else "#ccc"),
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
      fontSize("12px"),
      color("#6c757d"),
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
      backgroundColor("#e9ecef"),
      borderRadius("6px"),
      overflow.hidden,
      div(
        height("100%"),
        backgroundColor("#2c3e50"),
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
      button(
        padding("6px 12px"),
        border("1px solid #2c3e50"),
        borderRadius("4px"),
        backgroundColor("#2c3e50"),
        color("white"),
        cursor.pointer,
        fontSize("12px"),
        "+10%",
        onClick --> { _ => progress.increment(10) }
      ),
      button(
        padding("6px 12px"),
        border("1px solid #6c757d"),
        borderRadius("4px"),
        backgroundColor("white"),
        color("#6c757d"),
        cursor.pointer,
        fontSize("12px"),
        "Reset",
        onClick --> { _ => progress.reset() }
      )
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
          backgroundColor("#2c3e50"),
          color("white"),
          borderRadius("16px"),
          fontSize("13px"),
          tag,
          button(
            border("none"),
            backgroundColor("transparent"),
            color("white"),
            cursor.pointer,
            fontSize("14px"),
            padding("0 2px"),
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
        padding("10px 20px"),
        border("1px solid #2c3e50"),
        borderRadius("4px"),
        backgroundColor("#2c3e50"),
        color("white"),
        cursor.pointer,
        fontSize("14px"),
        "Hover me",
        onMouseEnter --> { _ => tooltip.show() },
        onMouseLeave --> { _ => tooltip.hide() }
      ),
      div(
        position.absolute,
        bottom("calc(100% + 8px)"),
        left("50%"),
        transform := "translateX(-50%)",
        padding("8px 12px"),
        backgroundColor("#333"),
        color("white"),
        borderRadius("4px"),
        fontSize("12px"),
        whiteSpace.nowrap,
        pointerEvents := "none",
        transition    := "opacity 0.2s",
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
