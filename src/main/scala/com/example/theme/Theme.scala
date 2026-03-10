package com.example.theme

import com.example.headless.{Counter, Sidebar, TopBar}
import com.raquo.laminar.api.L._

trait Theme {
  def counter(counter: Counter): HtmlElement
  def topbar(topBar: TopBar): HtmlElement
  def sidebar(sidebar: Sidebar): HtmlElement
}
