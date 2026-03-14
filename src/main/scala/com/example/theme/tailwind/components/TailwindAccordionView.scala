package com.example.theme.tailwind.components

import com.example.headless.components.Accordion
import com.raquo.laminar.api.L._

object TailwindAccordionView {
  def render(accordion: Accordion): HtmlElement = div(
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
}
