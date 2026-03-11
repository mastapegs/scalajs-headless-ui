package com.example.theme

import com.example.headless.components.{Counter, Sidebar, TopBar}
import com.example.headless.pages.{DashboardPage, MetricsPage, SettingsPage}
import com.raquo.laminar.api.L._

trait Theme {
  def counter(counter: Counter): HtmlElement
  def topbar(topBar: TopBar): HtmlElement
  def sidebar(sidebar: Sidebar): HtmlElement
  def dashboardPage(page: DashboardPage): HtmlElement
  def metricsPage(page: MetricsPage): HtmlElement
  def settingsPage(page: SettingsPage): HtmlElement
  def mainContent(content: Signal[HtmlElement]): HtmlElement
  def appLayout(
      topbar: HtmlElement,
      sidebar: HtmlElement,
      mainContent: HtmlElement
  ): HtmlElement
}
