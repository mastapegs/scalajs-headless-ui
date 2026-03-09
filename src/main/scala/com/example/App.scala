package com.example

import com.raquo.laminar.api.L._
import org.scalajs.dom

object App {

  def main(args: Array[String]): Unit = {
    val container = dom.document.querySelector("#appContainer")
    injectGlobalStyles()
    render(container, appElement())
  }

  private def injectGlobalStyles(): Unit = {
    val styleEl = dom.document.createElement("style")
    styleEl.textContent =
      """* { box-sizing: border-box; margin: 0; padding: 0; }
        |body {
        |  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
        |  color: #333;
        |}""".stripMargin
    dom.document.head.appendChild(styleEl)
  }

  private def topBar(): HtmlElement =
    div(
      position.fixed,
      top("0"),
      left("0"),
      width("100%"),
      height("56px"),
      backgroundColor("#2c3e50"),
      color("white"),
      display.flex,
      alignItems.center,
      padding("0 24px"),
      fontSize("18px"),
      fontWeight("600"),
      zIndex(100),
      span("UI Template")
    )

  private def sidebarItem(label: String): HtmlElement =
    p(
      cls("sidebar-item"),
      padding("8px 12px"),
      marginBottom("4px"),
      borderRadius("4px"),
      cursor.pointer,
      label
    )

  private def sidebar(): HtmlElement = {
    val hoverStyleEl = dom.document.createElement("style")
    hoverStyleEl.textContent = ".sidebar-item:hover { background: #d5dbdb; }"
    dom.document.head.appendChild(hoverStyleEl)

    div(
      width("220px"),
      flexShrink("0"),
      backgroundColor("#ecf0f1"),
      padding("24px 16px"),
      borderRight("1px solid #bdc3c7"),
      sidebarItem("Dashboard"),
      sidebarItem("Settings"),
      sidebarItem("About")
    )
  }

  private def mainContent(): HtmlElement =
    div(
      flexGrow(1),
      padding("32px"),
      overflowY.auto,
      h1(marginBottom("16px"), "Welcome"),
      p("This is the main content area of the application.")
    )

  private def appElement(): HtmlElement =
    div(
      topBar(),
      div(
        display.flex,
        marginTop("56px"),
        height("calc(100vh - 56px)"),
        sidebar(),
        mainContent()
      )
    )
}
