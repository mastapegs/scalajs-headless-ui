package com.example.headless.components

import com.raquo.airstream.core.Signal
import com.raquo.airstream.ownership.ManualOwner
import com.raquo.laminar.api.L._
import munit.FunSuite

class TopBarSuite extends FunSuite {

  private def signalNow[A](signal: Signal[A]): A = {
    val owner = new ManualOwner
    var value = Option.empty[A]
    signal.foreach(v => value = Some(v))(owner)
    owner.killSubscriptions()
    value.get
  }

  private def makeTopBar(
      brandName: String                = "TestApp",
      currentRenderer: String          = "inline",
      onRendererChange: String => Unit = _ => ()
  ): TopBar =
    new TopBar(brandName, Val(currentRenderer), onRendererChange)

  test("brandName returns injected value") {
    val topBar = makeTopBar(brandName = "MyApp")
    assertEquals(topBar.brandName, "MyApp")
  }

  test("rendererOptions contains inline and coreui") {
    val topBar = makeTopBar()
    assertEquals(
      topBar.rendererOptions,
      List("inline" -> "Inline Styles", "coreui" -> "CoreUI")
    )
  }

  test("selectRenderer invokes callback with correct value") {
    var selected: Option[String] = None
    val topBar = makeTopBar(onRendererChange = v => selected = Some(v))
    topBar.selectRenderer("coreui")
    assertEquals(selected, Some("coreui"))
  }

  test("currentRenderer reflects injected signal") {
    val topBar = makeTopBar(currentRenderer = "coreui")
    assertEquals(signalNow(topBar.currentRenderer), "coreui")
  }
}
