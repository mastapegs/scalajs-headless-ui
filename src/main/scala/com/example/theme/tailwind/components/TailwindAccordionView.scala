package com.example.theme.tailwind.components

import com.example.headless.components.Accordion
import com.raquo.laminar.api.L._

/** Tailwind accordion with card-style container, animated chevron rotation, and smooth content reveal.
  *
  * '''Design techniques:'''
  *   - '''Card wrapper''' (`rounded-xl border shadow-sm`) groups items into one cohesive surface
  *   - '''Divide utility''' (`divide-y divide-gray-100`) provides subtle separators between items
  *   - '''Chevron rotation''' via `rotate-180` with `transition-transform duration-200` for a smooth open/close
  *     indicator
  *   - '''Max-height animation''' (`transition-all duration-300`) reveals content with a sliding motion rather than an
  *     abrupt show/hide
  *   - '''Hover highlight''' (`hover:bg-gray-50`) gives tactile feedback on the clickable header row
  */
object TailwindAccordionView {
  def render(accordion: Accordion): HtmlElement = div(
    cls("border border-gray-200 rounded-xl shadow-md overflow-hidden divide-y divide-gray-200"),
    accordion.items.map { item =>
      div(
        cls("bg-white"),
        button(
          cls(
            "flex justify-between items-center w-full px-6 py-5 text-left text-sm font-semibold text-gray-800 hover:bg-gray-50 transition-colors duration-150 focus:outline-none focus:bg-gray-50"
          ),
          span(item.title),
          span(
            cls("text-gray-400 text-xs transition-transform duration-200"),
            cls <-- accordion.isOpen(item.key).map(if (_) "rotate-180" else ""),
            "\u25BC"
          ),
          onClick --> { _ => accordion.toggle(item.key) }
        ),
        div(
          cls("overflow-hidden transition-all duration-300 ease-in-out"),
          maxHeight <-- accordion.isOpen(item.key).map(if (_) "200px" else "0"),
          div(
            cls("px-6 py-5 text-sm text-gray-600 leading-relaxed bg-gray-50/50"),
            item.content
          )
        )
      )
    }
  )
}
