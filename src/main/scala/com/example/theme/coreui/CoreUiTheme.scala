package com.example.theme.coreui

import com.example.headless.components.{Counter, Sidebar, TopBar}
import com.example.headless.pages.{DashboardPage, MetricsPage, SettingsPage}
import com.example.theme.Theme
import com.example.theme.coreui.components._
import com.example.theme.coreui.pages._
import com.raquo.laminar.api.L._
import org.scalajs.dom

object CoreUiTheme extends Theme {
  val key: String = "coreui"

  private val stylesheetId  = "coreui-stylesheet"
  private val stylesheetUrl = "https://unpkg.com/@coreui/coreui@5.3.1/dist/css/coreui.min.css"

  override def onActivate(): Unit = {
    val existing = dom.document.getElementById(stylesheetId)
    if (existing == null) {
      val link = dom.document.createElement("link")
      link.setAttribute("id", stylesheetId)
      link.setAttribute("rel", "stylesheet")
      link.setAttribute("href", stylesheetUrl)
      dom.document.head.appendChild(link)
    }
  }

  override def onDeactivate(): Unit = {
    val existing = dom.document.getElementById(stylesheetId)
    if (existing != null) existing.parentNode.removeChild(existing)
  }

  def counter(counter: Counter): HtmlElement                 = CoreUiCounterView.render(counter)
  protected def renderSidebar(sidebar: Sidebar): HtmlElement = CoreUiSidebarView.render(sidebar)
  protected def renderTopbar(topBar: TopBar): HtmlElement    = CoreUiTopbarView.render(topBar)

  def dashboardPage(page: DashboardPage, counter: Counter): HtmlElement =
    CoreUiDashboardPageView.render(page, counter, this)
  def metricsPage(page: MetricsPage): HtmlElement =
    CoreUiMetricsPageView.render(page)
  def settingsPage(page: SettingsPage): HtmlElement =
    CoreUiSettingsPageView.render(page)

  protected def renderMainContent(content: Signal[HtmlElement]): Mod[HtmlElement] =
    Seq(
      cls("flex-grow-1 overflow-auto p-4"),
      child <-- content
    )

  def appLayout(
      topbarEl: HtmlElement,
      sidebarEl: HtmlElement,
      mainContentEl: HtmlElement
  ): HtmlElement =
    div(
      topbarEl,
      div(
        cls("d-flex"),
        marginTop("56px"),
        height("calc(100vh - 56px)"),
        sidebarEl,
        mainContentEl
      )
    )
}
