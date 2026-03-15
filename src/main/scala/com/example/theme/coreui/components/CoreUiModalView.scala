package com.example.theme.coreui.components

import com.example.headless.components.Modal
import com.raquo.laminar.api.L._

object CoreUiModalView {
  def render(modal: Modal[HtmlElement]): HtmlElement = div(
    // Trigger button
    button(
      cls("btn btn-primary"),
      s"Open ${modal.title}",
      onClick --> { _ => modal.open() }
    ),
    // Backdrop + dialog
    div(
      display <-- modal.isOpen.map(if (_) "block" else "none"),
      cls("modal-backdrop fade"),
      cls <-- modal.isOpen.map(if (_) "show" else ""),
      opacity <-- modal.isOpen.map(if (_) "0.5" else "0"),
      zIndex(1040)
    ),
    div(
      display <-- modal.isOpen.map(if (_) "block" else "none"),
      cls("modal fade"),
      cls <-- modal.isOpen.map(if (_) "show" else ""),
      tabIndex(-1),
      position.fixed,
      top("0"),
      left("0"),
      width("100%"),
      height("100%"),
      zIndex(1050),
      overflowY.auto,
      onClick --> { _ => modal.requestCloseOutside() },
      onKeyDown --> { ev =>
        if (ev.key == "Escape") modal.requestCloseEscape()
      },
      div(
        cls("modal-dialog modal-dialog-centered"),
        onClick.stopPropagation --> { _ => () },
        div(
          cls("modal-content"),
          // Header
          div(
            cls("modal-header"),
            h5(cls("modal-title"), modal.title),
            button(
              cls("btn-close"),
              aria.label := "Close",
              onClick --> { _ => modal.close() }
            )
          ),
          // Body
          div(
            cls("modal-body"),
            modal.content
          ),
          // Footer
          div(
            cls("modal-footer"),
            button(
              cls("btn btn-secondary"),
              "Close",
              onClick --> { _ => modal.close() }
            )
          )
        )
      ),
      role            := "dialog",
      aria.modal      := true,
      aria.labelledBy := s"modal-title-${modal.title.hashCode}"
    )
  )
}
