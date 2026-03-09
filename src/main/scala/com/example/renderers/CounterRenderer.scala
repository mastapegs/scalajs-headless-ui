package com.example.renderers

import com.raquo.laminar.api.L._
import com.example.headless.Counter

/** Trait for rendering a headless Counter into an HtmlElement. */
trait CounterRenderer {
  def render(counter: Counter): HtmlElement
}
