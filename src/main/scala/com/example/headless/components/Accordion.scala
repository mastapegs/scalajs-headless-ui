package com.example.headless.components

import com.raquo.laminar.api.L._

final class Accordion(val items: List[Accordion.ItemDef], val allowMultiple: Boolean = false) {

  private val openKeysVar: Var[Set[String]] = Var(Set.empty)

  val openKeys: Signal[Set[String]] = openKeysVar.signal

  def toggle(key: String): Unit =
    openKeysVar.update { keys =>
      if (keys.contains(key)) keys - key
      else if (allowMultiple) keys + key
      else Set(key)
    }

  def isOpen(key: String): Signal[Boolean] =
    openKeys.map(_.contains(key))
}

object Accordion {
  final case class ItemDef(key: String, title: String, content: String)
}
