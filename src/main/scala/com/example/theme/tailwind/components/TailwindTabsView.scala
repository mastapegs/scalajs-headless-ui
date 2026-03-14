package com.example.theme.tailwind.components

import com.example.headless.components.Tabs
import com.raquo.laminar.api.L._

object TailwindTabsView {
  def render(tabs: Tabs): HtmlElement = div(
    div(
      cls("flex border-b border-gray-200 mb-4"),
      tabs.tabs.zipWithIndex.map { case (tab, idx) =>
        button(
          cls("px-5 py-2.5 text-sm font-medium transition-colors"),
          cls <-- tabs
            .isSelected(idx)
            .map(
              if (_) "text-blue-600 border-b-2 border-blue-600"
              else "text-gray-500 hover:text-gray-700"
            ),
          tab.label,
          onClick --> { _ => tabs.select(idx) }
        )
      }
    ),
    div(
      cls("p-4 bg-gray-50 rounded text-gray-700 text-sm"),
      child.text <-- tabs.selectedTab.map(_.content)
    )
  )
}
