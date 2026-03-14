package com.example.headless.components

import com.example.headless.SignalHelpers
import com.raquo.laminar.api.L._
import munit.FunSuite

class TopBarSuite extends FunSuite with SignalHelpers {

  private def makeTopBar(
      brandName: String = "TestApp",
      currentRenderer: String = "inline",
      onRendererChange: String => Unit = _ => ()
  ): TopBar =
    new TopBar(brandName, Val(currentRenderer), onRendererChange)

  test("brandName returns injected value") {
    val topBar = makeTopBar(brandName = "MyApp")
    assertEquals(topBar.brandName, "MyApp")
  }

  test("rendererOptions contains inline, coreui, and tailwind") {
    val topBar = makeTopBar()
    assertEquals(
      topBar.rendererOptions,
      List("inline" -> "Inline Styles", "coreui" -> "CoreUI", "tailwind" -> "Tailwind")
    )
  }

  test("selectRenderer invokes callback with correct value") {
    var selected: Option[String] = None
    val topBar                   = makeTopBar(onRendererChange = v => selected = Some(v))
    topBar.selectRenderer("coreui")
    assertEquals(selected, Some("coreui"))
  }

  test("currentRenderer reflects injected signal") {
    val topBar = makeTopBar(currentRenderer = "coreui")
    assertEquals(signalNow(topBar.currentRenderer), "coreui")
  }
}
