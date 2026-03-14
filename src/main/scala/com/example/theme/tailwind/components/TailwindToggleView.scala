package com.example.theme.tailwind.components

import com.example.headless.components.Toggle
import com.raquo.laminar.api.L._

object TailwindToggleView {
  def render(toggle: Toggle): HtmlElement = div(
    cls("flex items-center gap-3"),
    button(
      cls("relative inline-flex h-6 w-11 items-center rounded-full transition-colors"),
      cls <-- toggle.isOn.map(if (_) "bg-blue-600" else "bg-gray-300"),
      role := "switch",
      aria.checked <-- toggle.isOn.map(_.toString),
      aria.label := toggle.label,
      span(
        cls("inline-block h-4 w-4 rounded-full bg-white transition-transform"),
        cls <-- toggle.isOn.map(if (_) "translate-x-6" else "translate-x-1")
      ),
      onClick --> { _ => toggle.toggle() }
    ),
    span(cls("text-sm font-medium text-gray-700"), toggle.label),
    span(
      cls("text-xs px-2 py-0.5 rounded-full"),
      cls <-- toggle.isOn.map(if (_) "bg-green-100 text-green-700" else "bg-gray-100 text-gray-500"),
      child.text <-- toggle.isOn.map(if (_) "ON" else "OFF")
    )
  )
}
