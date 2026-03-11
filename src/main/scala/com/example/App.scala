package com.example

import com.example.headless.{Counter, DashboardPage, MetricsPage, SettingsPage, Sidebar, TopBar}
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

  private val dashboardPage = new DashboardPage(counter)
  private val metricsPage = new MetricsPage()
  private val settingsPage = new SettingsPage()

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

  private def pageContent(page: Page, currentTheme: Theme): HtmlElement =
    page match {
      case Page.Dashboard => currentTheme.dashboardPage(dashboardPage)
      case Page.Metrics   => currentTheme.metricsPage(metricsPage)
      case Page.Settings  => currentTheme.settingsPage(settingsPage)
    }

  private def appElement(): HtmlElement =
    div(
      child <-- theme.signal.map { currentTheme =>
        val pageSignal =
          router.currentPageSignal.map(pageContent(_, currentTheme))
        currentTheme.appLayout(
          topbar = currentTheme.topbar(topBar),
          sidebar = currentTheme.sidebar(sidebar),
          mainContent = currentTheme.mainContent(pageSignal)
        )
      }
    )
}
