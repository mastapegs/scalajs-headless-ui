package com.example

import com.raquo.laminar.api.L._
import org.scalajs.dom

object App {

  def main(args: Array[String]): Unit = {
    val container = dom.document.querySelector("#appContainer")
    render(container, appElement())
  }

  private def topBar(): HtmlElement =
    div(
      idAttr("top-bar"),
      span("UI Template")
    )

  private def sidebar(): HtmlElement =
    div(
      idAttr("sidebar"),
      p("Dashboard"),
      p("Settings"),
      p("About")
    )

  private def mainContent(): HtmlElement =
    div(
      idAttr("main-content"),
      h1("Welcome"),
      p("This is the main content area of the application.")
    )

  private def appElement(): HtmlElement =
    div(
      topBar(),
      div(
        idAttr("layout"),
        sidebar(),
        mainContent()
      )
    )
}
