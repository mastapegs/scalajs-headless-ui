package com.example.renderers

import com.raquo.laminar.api.L._
import com.example.headless.TopBar

/** Trait for rendering a headless TopBar into an HtmlElement. */
trait TopBarRenderer {
  def render(topBar: TopBar): HtmlElement
}
