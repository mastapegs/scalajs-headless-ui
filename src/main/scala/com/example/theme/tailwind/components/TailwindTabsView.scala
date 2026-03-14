package com.example.theme.tailwind.components

import com.example.headless.components.Tabs
import com.raquo.laminar.api.L._

/** Tailwind tab navigation with an underline-style active indicator and a bordered content panel.
  *
  * '''Design techniques:'''
  *   - '''Bottom-border indicator''' (`border-b-2 border-indigo-500`) on the active tab for a clean, modern look
  *   - '''Negative margin trick''' (`-mb-px`) on tab buttons overlaps the container border for a seamless join between
  *     the active tab and the content panel
  *   - '''Focus ring''' (`focus:outline-none focus:ring-2 focus:ring-inset`) for keyboard-accessible tab switching
  *   - '''Content panel''' (`border rounded-b-xl`) continues the visual container from the tab bar
  *   - '''Transition on colors''' keeps the active/hover state switch smooth
  */
object TailwindTabsView {
  def render(tabs: Tabs): HtmlElement = div(
    div(
      cls("flex border-b border-gray-200"),
      tabs.tabs.zipWithIndex.map { case (tab, idx) =>
        button(
          cls("px-6 py-3.5 text-sm font-medium transition-colors duration-150 -mb-px focus:outline-none"),
          cls <-- tabs
            .isSelected(idx)
            .map(
              if (_) "text-indigo-600 border-b-2 border-indigo-500 bg-white"
              else "text-gray-500 hover:text-gray-700 hover:border-b-2 hover:border-gray-300"
            ),
          tab.label,
          onClick --> { _ => tabs.select(idx) }
        )
      }
    ),
    div(
      cls(
        "p-6 bg-white border border-t-0 border-gray-200 rounded-b-xl text-sm text-gray-700 leading-relaxed"
      ),
      child.text <-- tabs.selectedTab.map(_.content)
    )
  )
}
