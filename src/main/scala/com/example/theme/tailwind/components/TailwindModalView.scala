package com.example.theme.tailwind.components

import com.example.headless.components.Modal
import com.raquo.laminar.api.L._

/** Tailwind modal dialog with backdrop overlay, centered panel, and smooth transitions.
  *
  * '''Design techniques:'''
  *   - '''Backdrop''' uses `bg-black/50` with `fixed inset-0` for a semi-transparent overlay
  *   - '''Dialog panel''' uses `bg-white rounded-2xl shadow-2xl` with max-width constraint for a floating card feel
  *   - '''Header''' has a `border-b` separator with a close button using `hover:bg-gray-100 rounded-full` for a subtle
  *     hover affordance
  *   - '''Footer''' uses `border-t bg-gray-50/50` for a two-tone card footer
  *   - '''Focus ring''' on the trigger button (`focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2`)
  *   - '''Transitions''' on visibility (`transition-opacity duration-200`) for smooth open/close
  */
object TailwindModalView {
  def render(modal: Modal[HtmlElement]): HtmlElement = div(
    // Trigger button
    button(
      cls(
        "inline-flex items-center px-4 py-2 text-sm font-medium text-white bg-indigo-600 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 transition-colors duration-150"
      ),
      s"Open ${modal.title}",
      onClick --> { _ => modal.open() }
    ),
    // Backdrop + dialog
    div(
      cls("fixed inset-0 z-50 flex items-center justify-center"),
      display <-- modal.isOpen.map(if (_) "flex" else "none"),
      onClick --> { _ => modal.requestCloseOutside() },
      onKeyDown --> { ev =>
        if (ev.key == "Escape") modal.requestCloseEscape()
      },
      // Backdrop
      div(cls("absolute inset-0 bg-black/50 transition-opacity duration-200")),
      // Dialog panel
      div(
        cls(
          "relative bg-white rounded-2xl shadow-2xl w-full max-w-lg mx-4 overflow-hidden"
        ),
        onClick.stopPropagation --> { _ => () },
        // Header
        div(
          cls("flex items-center justify-between px-6 py-4 border-b border-gray-200"),
          h3(cls("text-lg font-semibold text-gray-900"), modal.title),
          button(
            cls(
              "p-1.5 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-full transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            ),
            aria.label := "Close",
            // X icon (SVG)
            htmlTag("svg")(
              cls("w-5 h-5"),
              htmlAttr("xmlns", StringAsIsCodec)   := "http://www.w3.org/2000/svg",
              htmlAttr("viewBox", StringAsIsCodec) := "0 0 20 20",
              htmlAttr("fill", StringAsIsCodec)    := "currentColor",
              htmlTag("path")(
                htmlAttr("fill-rule", StringAsIsCodec) := "evenodd",
                htmlAttr(
                  "d",
                  StringAsIsCodec
                ) := "M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z",
                htmlAttr("clip-rule", StringAsIsCodec) := "evenodd"
              )
            ),
            onClick --> { _ => modal.close() }
          )
        ),
        // Body
        div(
          cls("px-6 py-5"),
          modal.content
        ),
        // Footer
        div(
          cls("flex justify-end gap-3 px-6 py-4 border-t border-gray-200 bg-gray-50/50"),
          button(
            cls(
              "px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 transition-colors duration-150"
            ),
            "Close",
            onClick --> { _ => modal.close() }
          )
        ),
        role            := "dialog",
        aria.modal      := true,
        aria.labelledBy := s"modal-title-${modal.title.hashCode}"
      )
    )
  )
}
