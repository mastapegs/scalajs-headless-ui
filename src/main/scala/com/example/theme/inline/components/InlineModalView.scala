package com.example.theme.inline.components

import com.example.headless.components.Modal
import com.raquo.laminar.api.L._
import com.raquo.laminar.codecs.StringAsIsCodec

object InlineModalView {
  def render(modal: Modal[HtmlElement]): HtmlElement = div(
    // Trigger button
    button(
      padding("8px 16px"),
      backgroundColor("#2c3e50"),
      color("white"),
      border("none"),
      borderRadius("6px"),
      cursor.pointer,
      fontSize("14px"),
      fontWeight("500"),
      s"Open ${modal.title}",
      onClick --> { _ => modal.open() }
    ),
    // Backdrop + dialog (hidden when closed)
    div(
      display <-- modal.isOpen.map(if (_) "flex" else "none"),
      position.fixed,
      top("0"),
      left("0"),
      width("100%"),
      height("100%"),
      backgroundColor("rgba(0, 0, 0, 0.5)"),
      justifyContent.center,
      alignItems.center,
      zIndex(1000),
      onClick --> { _ => modal.requestCloseOutside() },
      onKeyDown --> { ev =>
        if (ev.key == "Escape") modal.requestCloseEscape()
      },
      // Dialog panel
      div(
        backgroundColor("white"),
        borderRadius("12px"),
        padding("0"),
        minWidth("400px"),
        maxWidth("560px"),
        width("90%"),
        boxShadow := "0 20px 60px rgba(0, 0, 0, 0.3)",
        onClick.stopPropagation --> { _ => () },
        // Header
        div(
          display.flex,
          justifyContent.spaceBetween,
          alignItems.center,
          padding("16px 24px"),
          borderBottom := "1px solid #e9ecef",
          h3(margin("0"), fontSize("18px"), fontWeight("600"), color("#1a1a2e"), modal.title),
          button(
            background("none"),
            border("none"),
            fontSize("20px"),
            cursor.pointer,
            color("#6c757d"),
            padding("4px 8px"),
            borderRadius("4px"),
            "\u00d7",
            onClick --> { _ => modal.close() }
          )
        ),
        // Body
        div(
          padding("24px"),
          modal.content
        ),
        // Footer
        div(
          display.flex,
          justifyContent.flexEnd,
          gap("8px"),
          padding("16px 24px"),
          borderTop := "1px solid #e9ecef",
          button(
            padding("8px 16px"),
            backgroundColor("#e9ecef"),
            color("#495057"),
            border("none"),
            borderRadius("6px"),
            cursor.pointer,
            fontSize("14px"),
            "Close",
            onClick --> { _ => modal.close() }
          )
        ),
        role                                    := "dialog",
        htmlAttr("aria-modal", StringAsIsCodec) := "true",
        aria.labelledBy                         := s"modal-title-${modal.title.hashCode}"
      )
    )
  )
}
