package com.example.headless.components

import com.raquo.laminar.api.L._

final class Tooltip(val text: String, val placement: String = "top") {

  private val visibleVar: Var[Boolean] = Var(false)

  val isVisible: Signal[Boolean] = visibleVar.signal

  def show(): Unit = visibleVar.set(true)
  def hide(): Unit = visibleVar.set(false)
}
