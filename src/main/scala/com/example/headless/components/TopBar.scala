package com.example.headless.components

import com.raquo.laminar.api.L._

/** Headless top bar component — manages brand display and renderer selection. */
final class TopBar(
    val brandName: String,
    val currentRenderer: Signal[String],
    private val onRendererChange: String => Unit
) {
  val rendererOptions: List[(String, String)] = List(
    "inline" -> "Inline Styles",
    "coreui" -> "CoreUI"
  )

  def selectRenderer(value: String): Unit = onRendererChange(value)
}
