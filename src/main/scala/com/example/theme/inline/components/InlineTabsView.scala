package com.example.theme.inline.components

import com.example.headless.components.Tabs
import com.raquo.laminar.api.L._

object InlineTabsView {
  def render(tabs: Tabs): HtmlElement = div(
    div(
      display.flex,
      borderBottom("1px solid #dee2e6"),
      marginBottom("16px"),
      tabs.tabs.zipWithIndex.map { case (tab, idx) =>
        button(
          padding("10px 20px"),
          border("none"),
          backgroundColor("transparent"),
          cursor.pointer,
          fontSize("14px"),
          fontWeight("500"),
          borderBottom <-- tabs.isSelected(idx).map(if (_) "3px solid #2c3e50" else "3px solid transparent"),
          color <-- tabs.isSelected(idx).map(if (_) "#2c3e50" else "#6c757d"),
          tab.label,
          onClick --> { _ => tabs.select(idx) }
        )
      }
    ),
    div(
      padding("16px"),
      backgroundColor("#f8f9fa"),
      borderRadius("4px"),
      child.text <-- tabs.selectedTab.map(_.content)
    )
  )
}
