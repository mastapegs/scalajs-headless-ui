package com.example.theme.tailwind.components

import com.example.headless.components.TagsInput
import com.raquo.laminar.api.L._

/** Tailwind tags input with pill-shaped badges, inline editing, and a polished focus-within ring.
  *
  * '''Design techniques:'''
  *   - '''Focus-within ring''' (`focus-within:ring-2 focus-within:ring-indigo-500 focus-within:border-indigo-500`)
  *     highlights the entire container when the inner `&lt;input&gt;` receives focus — a pattern common in professional
  *     form design
  *   - '''Pill tags''' (`rounded-full bg-indigo-600`) with a hover-darkened remove button provide clear, touch-friendly
  *     tag management
  *   - '''Inline flex layout''' keeps tags and the input on the same visual line, wrapping naturally
  *   - '''Minimum input width''' (`min-w-[120px]`) prevents the input from collapsing when many tags are present
  *   - '''Helper text''' below the container shows the current tag count in a subtle muted style
  */
object TailwindTagsInputView {
  def render(tagsInput: TagsInput): HtmlElement = div(
    div(
      cls(
        "flex flex-wrap gap-2.5 items-center p-3 border border-gray-300 rounded-xl min-h-[48px] bg-white transition-all duration-200 focus-within:ring-2 focus-within:ring-indigo-500 focus-within:border-indigo-500"
      ),
      children <-- tagsInput.tags.map(_.map { tag =>
        span(
          cls(
            "inline-flex items-center gap-1 px-3 py-1 bg-indigo-600 text-white rounded-full text-sm font-medium"
          ),
          tag,
          button(
            cls(
              "ml-0.5 hover:bg-indigo-700 rounded-full w-4 h-4 inline-flex items-center justify-center text-indigo-200 hover:text-white transition-colors duration-150"
            ),
            aria.label := s"Remove tag $tag",
            "\u00D7",
            onClick --> { _ => tagsInput.removeTag(tag) }
          )
        )
      }),
      input(
        cls("flex-grow min-w-[120px] border-0 outline-none text-sm text-gray-700 placeholder-gray-400 bg-transparent"),
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
      cls("text-xs text-gray-400 mt-2 ml-1"),
      child.text <-- tagsInput.tagCount.map(c => s"$c tag(s) added")
    )
  )
}
