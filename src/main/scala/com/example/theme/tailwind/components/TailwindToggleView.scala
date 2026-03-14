package com.example.theme.tailwind.components

import com.example.headless.components.Toggle
import com.raquo.laminar.api.L._

/** Tailwind toggle / switch with a pill-shaped track, animated knob, and status badge.
  *
  * '''Design techniques:'''
  *   - '''Pill track''' (`h-6 w-11 rounded-full`) mimics native iOS/Android toggle switches
  *   - '''Sliding knob''' uses `translate-x-*` with `transition-transform duration-200` for a smooth slide
  *   - '''Color transition''' (`transition-colors`) on the track background for an animated on/off color change
  *   - '''Focus ring''' (`focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2`) for keyboard accessibility
  *   - '''Status badge''' uses contextual colors (`bg-emerald-100 text-emerald-700` for ON, `bg-gray-100 text-gray-500`
  *     for OFF) as a secondary visual confirmation
  */
object TailwindToggleView {
  def render(toggle: Toggle): HtmlElement = div(
    cls("flex items-center gap-4 py-2"),
    button(
      cls(
        "relative inline-flex h-6 w-11 flex-shrink-0 items-center rounded-full transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
      ),
      cls <-- toggle.isOn.map(if (_) "bg-indigo-600" else "bg-gray-300"),
      role := "switch",
      aria.checked <-- toggle.isOn.map(_.toString),
      aria.label := toggle.label,
      span(
        cls(
          "inline-block h-4 w-4 rounded-full bg-white shadow-sm ring-0 transition-transform duration-200 ease-in-out"
        ),
        cls <-- toggle.isOn.map(if (_) "translate-x-6" else "translate-x-1")
      ),
      onClick --> { _ => toggle.toggle() }
    ),
    span(cls("text-sm font-medium text-gray-700 select-none"), toggle.label),
    span(
      cls("text-xs font-medium px-2.5 py-0.5 rounded-full transition-colors duration-200"),
      cls <-- toggle.isOn.map(
        if (_) "bg-emerald-100 text-emerald-700"
        else "bg-gray-100 text-gray-500"
      ),
      child.text <-- toggle.isOn.map(if (_) "ON" else "OFF")
    )
  )
}
