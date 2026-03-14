package com.example.theme.coreui

import com.example.headless.components.{Counter, Sidebar, TopBar}
import com.example.headless.pages.{DashboardPage, FetchPage, MetricsPage, SettingsPage, UIShowcasePage}
import com.example.theme.Theme
import com.example.theme.coreui.components._
import com.example.theme.coreui.pages._
import com.raquo.laminar.api.L._
import org.scalajs.dom

object CoreUiTheme extends Theme {
  val key: String = "coreui"

  private val stylesheetId  = "coreui-stylesheet"
  private val stylesheetUrl = "https://cdn.jsdelivr.net/npm/@coreui/coreui@5.3.1/dist/css/coreui.min.css"
  private val scriptId      = "coreui-script"
  private val scriptUrl     = "https://cdn.jsdelivr.net/npm/@coreui/coreui@5.3.1/dist/js/coreui.bundle.min.js"

  override def onActivate(): Unit = {
    if (dom.document.getElementById(stylesheetId) == null) {
      val link = dom.document.createElement("link")
      link.setAttribute("id", stylesheetId)
      link.setAttribute("rel", "stylesheet")
      link.setAttribute("href", stylesheetUrl)
      dom.document.head.appendChild(link)
    }
    if (dom.document.getElementById(scriptId) == null) {
      val script = dom.document.createElement("script")
      script.setAttribute("id", scriptId)
      script.setAttribute("src", scriptUrl)
      dom.document.body.appendChild(script)
    }
  }

  override def onDeactivate(): Unit = {
    val css = dom.document.getElementById(stylesheetId)
    if (css != null) css.parentNode.removeChild(css)
    val js = dom.document.getElementById(scriptId)
    if (js != null) js.parentNode.removeChild(js)
  }

  def counter(counter: Counter): HtmlElement                 = CoreUiCounterView.render(counter)
  protected def renderSidebar(sidebar: Sidebar): HtmlElement = CoreUiSidebarView.render(sidebar)
  protected def renderTopbar(topBar: TopBar): HtmlElement    = CoreUiTopbarView.render(topBar)

  def dashboardPage(page: DashboardPage): HtmlElement =
    CoreUiDashboardPageView.render(page, this)
  def metricsPage(page: MetricsPage): HtmlElement =
    CoreUiMetricsPageView.render(page)
  def settingsPage(page: SettingsPage): HtmlElement =
    CoreUiSettingsPageView.render(page)
  override def uiShowcasePage(page: UIShowcasePage): HtmlElement =
    CoreUiUIShowcasePageView.render(page)
  protected def renderFetchPage(page: FetchPage): HtmlElement =
    CoreUiFetchPageView.render(page)

  protected def renderMainContent(content: Signal[HtmlElement]): Mod[HtmlElement] =
    Seq(
      cls("flex-grow-1 overflow-auto p-4"),
      child <-- content
    )

  def renderAppLayout(
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
