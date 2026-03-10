package com.example

import com.example.headless.{Counter, Sidebar, TopBar}
import com.example.theme.Theme
import com.example.theme.coreui.CoreUiTheme
import com.example.theme.inline.InlineTheme
import com.raquo.laminar.api.L._
import org.scalajs.dom

object App {

  private val router = AppRouter.router

  private val coreUiStylesheetId = "coreui-stylesheet"
  private val coreUiStylesheetUrl =
    "https://unpkg.com/@coreui/coreui@5.3.1/dist/css/coreui.min.css"

  private def setCoreUiStylesheet(enabled: Boolean): Unit = {
    val existing = dom.document.getElementById(coreUiStylesheetId)
    if (enabled && existing == null) {
      val link = dom.document.createElement("link")
      link.setAttribute("id", coreUiStylesheetId)
      link.setAttribute("rel", "stylesheet")
      link.setAttribute("href", coreUiStylesheetUrl)
      dom.document.head.appendChild(link)
    } else if (!enabled && existing != null) {
      existing.parentNode.removeChild(existing)
    }
  }

  private val rendererKeyVar: Var[String] = Var("inline")

  private val theme: Var[Theme] = Var(InlineTheme)

  def main(args: Array[String]): Unit = {
    val container = dom.document.querySelector("#appContainer")
    render(container, appElement())
  }

  private val counter = new Counter()

  private val sidebar = new Sidebar(
    pages = Page.all,
    currentPage = router.currentPageSignal,
    onNavigate = page => router.pushState(page)
  )

  private val topBar = new TopBar(
    brandName = "UI Template",
    currentRenderer = rendererKeyVar.signal,
    onRendererChange = { v =>
      rendererKeyVar.set(v)
      setCoreUiStylesheet(v == "coreui")
      theme.set(if (v == "coreui") CoreUiTheme else InlineTheme)
    }
  )

  private def pageContent(page: Page): HtmlElement = page match {
    case Page.Dashboard =>
      div(
        h1(marginBottom("16px"), "Dashboard"),
        p("Welcome to the dashboard. This is the main overview page."),
        div(
          marginTop("24px"),
          child <-- theme.signal.map(_.counter(counter))
        )
      )
    case Page.Metrics =>
      div(
        h1(marginBottom("16px"), "Metrics"),
        p("Charts and metrics would be displayed here.")
      )
    case Page.Settings =>
      div(
        h1(marginBottom("16px"), "Settings"),
        p("Application settings and preferences.")
      )
  }

  private def mainContent(): HtmlElement =
    div(
      flexGrow(1),
      padding("32px"),
      overflowY.auto,
      child <-- router.currentPageSignal.map(pageContent)
    )

  private def appElement(): HtmlElement =
    div(
      child <-- theme.signal.map(_.topbar(topBar)),
      div(
        display.flex,
        marginTop("56px"),
        height("calc(100vh - 56px)"),
        child <-- theme.signal.map(_.sidebar(sidebar)),
        mainContent()
      )
    )
}
