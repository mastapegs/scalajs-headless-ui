package com.example.theme.inline

import com.example.headless.components.{Counter, Sidebar, TopBar}
import com.example.headless.pages.{DashboardPage, MetricsPage, SettingsPage}
import com.example.theme.Theme
import com.example.theme.inline.components._
import com.example.theme.inline.pages._
import com.raquo.laminar.api.L._

object InlineTheme extends Theme {
  val key: String = "inline"

  def counter(counter: Counter): HtmlElement                 = InlineCounterView.render(counter)
  protected def renderSidebar(sidebar: Sidebar): HtmlElement = InlineSidebarView.render(sidebar)
  protected def renderTopbar(topBar: TopBar): HtmlElement    = InlineTopbarView.render(topBar)

  def dashboardPage(page: DashboardPage, counter: Counter): HtmlElement =
    InlineDashboardPageView.render(page, counter, this)
  def metricsPage(page: MetricsPage): HtmlElement =
    InlineMetricsPageView.render(page)
  def settingsPage(page: SettingsPage): HtmlElement =
    InlineSettingsPageView.render(page)

  protected def renderMainContent(content: Signal[HtmlElement]): Mod[HtmlElement] =
    Seq(
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
