package com.example.headless.components

import com.raquo.laminar.api.L._

final class Tabs(val tabs: List[Tabs.TabDef], initialIndex: Int = 0) {

  private val selectedIndexVar: Var[Int] = Var(initialIndex)

  val selectedIndex: Signal[Int] = selectedIndexVar.signal

  val selectedTab: Signal[Tabs.TabDef] = selectedIndex.map(i => tabs(i.max(0).min(tabs.length - 1)))

  def select(index: Int): Unit =
    if (index >= 0 && index < tabs.length) selectedIndexVar.set(index)

  def selectNext(): Unit =
    selectedIndexVar.update(i => (i + 1) % tabs.length)

  def selectPrev(): Unit =
    selectedIndexVar.update(i => (i - 1 + tabs.length) % tabs.length)

  def isSelected(index: Int): Signal[Boolean] =
    selectedIndex.map(_ == index)
}

object Tabs {
  final case class TabDef(label: String, content: String)
}
