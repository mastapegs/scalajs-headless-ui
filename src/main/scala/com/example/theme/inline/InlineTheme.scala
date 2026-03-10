package com.example.theme.inline

import com.raquo.laminar.api.L._

import com.example.headless.Counter
import com.example.headless.Sidebar
import com.example.headless.TopBar
import com.example.theme.Theme
import com.example.theme.inline.components.InlineCounterView
import com.example.theme.inline.components.InlineSidebarView
import com.example.theme.inline.components.InlineTopbarView

object InlineTheme extends Theme {
  def counter(counter: Counter): HtmlElement = InlineCounterView.render(counter)
  def sidebar(sidebar: Sidebar): HtmlElement = InlineSidebarView.render(sidebar)
  def topbar(topBar: TopBar): HtmlElement = InlineTopbarView.render(topBar)
}
