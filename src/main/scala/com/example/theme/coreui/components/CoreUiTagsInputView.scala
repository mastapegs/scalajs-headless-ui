package com.example.theme.coreui.components

import com.example.headless.components.TagsInput
import com.raquo.laminar.api.L._

object CoreUiTagsInputView {
  def render(tagsInput: TagsInput): HtmlElement = div(
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
}
