package com.example.theme.coreui.components

import com.example.headless.components.Accordion
import com.raquo.laminar.api.L._

object CoreUiAccordionView {
  def render(accordion: Accordion): HtmlElement = div(
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
}
