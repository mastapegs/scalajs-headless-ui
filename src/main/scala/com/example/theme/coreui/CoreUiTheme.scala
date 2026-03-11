package com.example.theme.coreui

import com.example.headless.components.{Counter, Sidebar, TopBar}
import com.example.headless.pages.{DashboardPage, MetricsPage, SettingsPage}
import com.example.theme.Theme
import com.example.theme.coreui.components._
import com.example.theme.coreui.pages._
import com.raquo.laminar.api.L._

object CoreUiTheme extends Theme {
  def counter(counter: Counter): HtmlElement = CoreUiCounterView.render(counter)
  def sidebar(sidebar: Sidebar): HtmlElement = CoreUiSidebarView.render(sidebar)
  def topbar(topBar: TopBar): HtmlElement    = CoreUiTopbarView.render(topBar)

  def dashboardPage(page: DashboardPage, counter: Counter): HtmlElement =
    CoreUiDashboardPageView.render(page, counter, this)
  def metricsPage(page: MetricsPage): HtmlElement =
    CoreUiMetricsPageView.render(page)
  def settingsPage(page: SettingsPage): HtmlElement =
    CoreUiSettingsPageView.render(page)

  def mainContent(content: Signal[HtmlElement]): HtmlElement =
    div(
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
