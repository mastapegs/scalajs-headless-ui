package com.example.theme.coreui.components

import com.example.headless.components.Toggle
import com.raquo.laminar.api.L._

object CoreUiToggleView {
  def render(toggle: Toggle): HtmlElement = div(
    display.flex,
    alignItems.center,
    gap("12px"),
    button(
      width("48px"),
      height("26px"),
      borderRadius("13px"),
      border("none"),
      cursor.pointer,
      position.relative,
      transition := "background-color 0.2s",
      backgroundColor <-- toggle.isOn.map(if (_) "var(--cui-primary, #321fdb)" else "#ccc"),
      role := "switch",
      aria.checked <-- toggle.isOn.map(_.toString),
      aria.label := toggle.label,
      span(
        position.absolute,
        top("3px"),
        width("20px"),
        height("20px"),
        borderRadius("50%"),
        backgroundColor("white"),
        transition := "left 0.2s",
        left <-- toggle.isOn.map(if (_) "25px" else "3px")
      ),
      onClick --> { _ => toggle.toggle() }
    ),
    span(fontSize("14px"), fontWeight("500"), toggle.label),
    span(
      display.inlineFlex,
      padding("2px 8px"),
      borderRadius("12px"),
      fontSize("12px"),
      backgroundColor <-- toggle.isOn.map(if (_) "#d4edda" else "#e9ecef"),
      color <-- toggle.isOn.map(if (_) "#155724" else "#6c757d"),
      child.text <-- toggle.isOn.map(if (_) "ON" else "OFF")
    )
  )
}
