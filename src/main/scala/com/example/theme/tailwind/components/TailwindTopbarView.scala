package com.example.theme.tailwind.components

import com.example.headless.components.TopBar
import com.raquo.laminar.api.L._

/** Tailwind top bar with a dark indigo gradient, mobile hamburger menu, and a polished renderer selector.
  *
  * '''Design techniques:'''
  *   - '''Gradient background''' (`bg-gradient-to-r from-slate-800 to-slate-900`) adds depth without an extra border
  *   - '''Shadow''' (`shadow-lg`) gives visual separation from the content area below
  *   - '''Mobile hamburger''' — a `lg:hidden` toggle button opens the sidebar overlay on small screens
  *   - '''Custom select styling''' with rounded corners, subtle background, and focus ring for accessibility
  *   - '''Consistent height''' (`h-14` / 56 px) anchors the layout and aligns with the sidebar
  */
object TailwindTopbarView {
  def render(topBar: TopBar, onToggleSidebar: () => Unit = () => ()): HtmlElement = headerTag(
    cls(
      "fixed top-0 left-0 right-0 h-14 bg-gradient-to-r from-slate-800 to-slate-900 text-white flex items-center px-4 sm:px-5 z-50 shadow-lg"
    ),
    // Mobile hamburger button (hidden on lg+)
    button(
      cls(
        "lg:hidden mr-3 p-1.5 rounded-lg text-slate-300 hover:text-white hover:bg-white/10 transition-colors duration-200"
      ),
      aria.label := "Toggle sidebar",
      span(cls("text-xl"), "\u2630"),
      onClick --> { _ => onToggleSidebar() }
    ),
    div(
      cls("flex items-center gap-3"),
      // Brand icon circle
      div(
        cls("w-8 h-8 rounded-lg bg-indigo-500 flex items-center justify-center text-white font-bold text-sm"),
        "UI"
      ),
      span(cls("text-lg font-semibold tracking-tight"), topBar.brandName)
    ),
    div(
      cls("ml-auto flex items-center gap-3"),
      span(cls("text-sm text-slate-400 hidden sm:inline"), "Renderer:"),
      select(
        cls(
          "bg-slate-700/80 text-white rounded-lg px-3 py-1.5 text-sm border border-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent cursor-pointer"
        ),
        aria.label := "Select renderer",
        topBar.rendererOptions.map { case (v, lbl) =>
          option(value(v), lbl)
        },
        controlled(
          value <-- topBar.currentRenderer,
          onChange.mapToValue --> { v =>
            topBar.selectRenderer(v)
          }
        )
      )
    )
  )
}
