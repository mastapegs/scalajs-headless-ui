package com.example.theme.coreui.components

import com.example.headless.components.Tabs
import com.raquo.laminar.api.L._

object CoreUiTabsView {
  def render(tabs: Tabs): HtmlElement = div(
    htmlTag("ul")(
      cls("nav nav-tabs"),
      tabs.tabs.zipWithIndex.map { case (tab, idx) =>
        htmlTag("li")(
          cls("nav-item"),
          a(
            cls("nav-link"),
            cls <-- tabs.isSelected(idx).map(if (_) "active" else ""),
            href("#"),
            tab.label,
            onClick.preventDefault --> { _ => tabs.select(idx) }
          )
        )
      }
    ),
    div(
      cls("tab-content p-3 border border-top-0 rounded-bottom"),
      child.text <-- tabs.selectedTab.map(_.content)
    )
  )
}
