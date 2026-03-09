package com.example.renderers

import com.raquo.laminar.api.L._
import com.example.headless.Sidebar

/** Trait for rendering a headless Sidebar into an HtmlElement. */
trait SidebarRenderer {
  def render(sidebar: Sidebar): HtmlElement
}
