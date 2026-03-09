package com.example.theme

import com.raquo.laminar.api.L._
import com.example.headless.Sidebar
import com.example.headless.TopBar
import com.example.headless.Counter

trait Theme {
  def counter(counter: Counter): HtmlElement
  def topbar(topBar: TopBar): HtmlElement
  def sidebar(sidebar: Sidebar): HtmlElement
}
