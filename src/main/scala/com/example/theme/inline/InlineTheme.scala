package com.example.theme.inline

import com.example.headless.{Counter, DashboardPage, MetricsPage, SettingsPage, Sidebar, TopBar}
import com.example.theme.Theme
import com.example.theme.inline.components._
import com.example.theme.inline.pages._
import com.raquo.laminar.api.L._

object InlineTheme extends Theme {
  def counter(counter: Counter): HtmlElement = InlineCounterView.render(counter)
  def sidebar(sidebar: Sidebar): HtmlElement = InlineSidebarView.render(sidebar)
  def topbar(topBar: TopBar): HtmlElement    = InlineTopbarView.render(topBar)

  def dashboardPage(page: DashboardPage): HtmlElement =
    InlineDashboardPageView.render(page, this)
  def metricsPage(page: MetricsPage): HtmlElement =
    InlineMetricsPageView.render(page)
  def settingsPage(page: SettingsPage): HtmlElement =
    InlineSettingsPageView.render(page)

  def mainContent(content: Signal[HtmlElement]): HtmlElement =
    div(
      flexGrow(1),
      padding("32px"),
      overflowY.auto,
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
        display.flex,
        marginTop("56px"),
        height("calc(100vh - 56px)"),
        sidebarEl,
        mainContentEl
      )
    )
}
