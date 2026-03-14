package com.example.theme.coreui.components

import com.example.headless.components.Counter
import com.raquo.laminar.api.L._

object CoreUiCounterView {
  def render(counter: Counter): HtmlElement = div(
    cls("card"),
    div(
      cls("card-body d-flex align-items-center gap-3"),
      span(
        cls("text-body-secondary fw-medium"),
        counter.label
      ),
      span(
        cls("fs-4 fw-semibold"),
        aria.live   := "polite",
        aria.atomic := true,
        child.text <-- counter.count.map(_.toString)
      ),
      button(
        cls("btn btn-primary"),
        aria.label := "Increment counter",
        "Increment",
        onClick --> { _ => counter.increment() }
      )
    )
  )
}
