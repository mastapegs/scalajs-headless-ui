package com.example.theme.tailwind.components

import com.example.headless.components.TagsInput
import com.raquo.laminar.api.L._

object TailwindTagsInputView {
  def render(tagsInput: TagsInput): HtmlElement = div(
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
}
