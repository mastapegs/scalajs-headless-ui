package com.example.headless.components

import com.raquo.laminar.api.L._

final class Toggle(val label: String, initialValue: Boolean = false) {

  private val onVar: Var[Boolean] = Var(initialValue)

  val isOn: Signal[Boolean] = onVar.signal

  def toggle(): Unit =
    onVar.update(!_)

  def setOn(): Unit  = onVar.set(true)
  def setOff(): Unit = onVar.set(false)
}
