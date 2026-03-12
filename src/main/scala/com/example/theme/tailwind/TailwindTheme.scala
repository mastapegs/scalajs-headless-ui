package com.example.theme.tailwind

import com.example.headless.components.{Counter, Sidebar, TopBar}
import com.example.headless.pages.{DashboardPage, MetricsPage, SettingsPage}
import com.example.theme.Theme
import com.example.theme.tailwind.components._
import com.example.theme.tailwind.pages._
import com.raquo.laminar.api.L._
import org.scalajs.dom

object TailwindTheme extends Theme {
  val key: String = "tailwind"

  private val scriptId  = "tailwind-script"
  private val scriptUrl = "https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"

  override def onActivate(): Unit = {
    val existing = dom.document.getElementById(scriptId)
    if (existing == null) {
      val script = dom.document.createElement("script")
      script.setAttribute("id", scriptId)
      script.setAttribute("src", scriptUrl)
      dom.document.head.appendChild(script)
    }
  }

  override def onDeactivate(): Unit = {
    val existing = dom.document.getElementById(scriptId)
    if (existing != null) existing.parentNode.removeChild(existing)
  }

  def counter(counter: Counter): HtmlElement                 = TailwindCounterView.render(counter)
  protected def renderSidebar(sidebar: Sidebar): HtmlElement = TailwindSidebarView.render(sidebar)
  protected def renderTopbar(topBar: TopBar): HtmlElement    = TailwindTopbarView.render(topBar)

  def dashboardPage(page: DashboardPage, counter: Counter): HtmlElement =
    TailwindDashboardPageView.render(page, counter, this)
  def metricsPage(page: MetricsPage): HtmlElement =
    TailwindMetricsPageView.render(page)
  def settingsPage(page: SettingsPage): HtmlElement =
    TailwindSettingsPageView.render(page)

  protected def renderMainContent(content: Signal[HtmlElement]): Mod[HtmlElement] =
    Seq(
      cls("flex-grow overflow-y-auto p-8"),
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
        cls("flex"),
        marginTop("56px"),
        height("calc(100vh - 56px)"),
        sidebarEl,
        mainContentEl
      )
    )
}
