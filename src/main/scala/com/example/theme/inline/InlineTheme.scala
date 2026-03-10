package com.example.theme.inline

import com.example.headless.{Counter, Sidebar, TopBar}
import com.example.theme.Theme
import com.example.theme.inline.components.{InlineCounterView, InlineSidebarView, InlineTopbarView}
import com.raquo.laminar.api.L._

object InlineTheme extends Theme {
  def counter(counter: Counter): HtmlElement = InlineCounterView.render(counter)
  def sidebar(sidebar: Sidebar): HtmlElement = InlineSidebarView.render(sidebar)
  def topbar(topBar: TopBar): HtmlElement    = InlineTopbarView.render(topBar)
}
