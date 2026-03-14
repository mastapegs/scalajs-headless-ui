package com.example.headless.components

import com.example.headless.SignalHelpers
import munit.FunSuite

class TabsSuite extends FunSuite with SignalHelpers {

  private val sampleTabs = List(
    Tabs.TabDef("Tab A", "Content A"),
    Tabs.TabDef("Tab B", "Content B"),
    Tabs.TabDef("Tab C", "Content C")
  )

  test("initial selected index is 0 by default") {
    val tabs = new Tabs(sampleTabs)
    assertEquals(signalNow(tabs.selectedIndex), 0)
  }

  test("custom initial index is respected") {
    val tabs = new Tabs(sampleTabs, initialIndex = 2)
    assertEquals(signalNow(tabs.selectedIndex), 2)
  }

  test("select changes selected index") {
    val tabs = new Tabs(sampleTabs)
    tabs.select(1)
    assertEquals(signalNow(tabs.selectedIndex), 1)
  }

  test("select ignores out-of-bounds index") {
    val tabs = new Tabs(sampleTabs)
    tabs.select(5)
    assertEquals(signalNow(tabs.selectedIndex), 0)
  }

  test("selectNext wraps around") {
    val tabs = new Tabs(sampleTabs, initialIndex = 2)
    tabs.selectNext()
    assertEquals(signalNow(tabs.selectedIndex), 0)
  }

  test("selectPrev wraps around") {
    val tabs = new Tabs(sampleTabs)
    tabs.selectPrev()
    assertEquals(signalNow(tabs.selectedIndex), 2)
  }

  test("selectedTab returns correct tab definition") {
    val tabs = new Tabs(sampleTabs)
    tabs.select(1)
    assertEquals(signalNow(tabs.selectedTab), Tabs.TabDef("Tab B", "Content B"))
  }

  test("isSelected returns correct signal") {
    val tabs = new Tabs(sampleTabs)
    tabs.select(1)
    assertEquals(signalNow(tabs.isSelected(0)), false)
    assertEquals(signalNow(tabs.isSelected(1)), true)
    assertEquals(signalNow(tabs.isSelected(2)), false)
  }
}
