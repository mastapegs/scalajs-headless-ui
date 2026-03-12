package com.example.theme

import com.example.headless.components.{Counter, Sidebar, TopBar}
import com.example.headless.pages.{DashboardPage, MetricsPage, SettingsPage}
import com.example.theme.coreui.CoreUiTheme
import com.example.theme.inline.InlineTheme
import com.example.theme.tailwind.TailwindTheme
import com.raquo.laminar.api.L._

trait Theme {
  def key: String

  def onActivate(): Unit   = ()
  def onDeactivate(): Unit = ()

  def counter(counter: Counter): HtmlElement

  protected def renderTopbar(topBar: TopBar): HtmlElement

  final def topbar(topBar: TopBar): HtmlElement =
    renderTopbar(topBar).amend(aria.label := "Top bar")

  protected def renderSidebar(sidebar: Sidebar): HtmlElement

  final def sidebar(sidebar: Sidebar): HtmlElement =
    renderSidebar(sidebar).amend(aria.label := "Main navigation")

  def dashboardPage(page: DashboardPage): HtmlElement
  def metricsPage(page: MetricsPage): HtmlElement
  def settingsPage(page: SettingsPage): HtmlElement

  protected def renderMainContent(content: Signal[HtmlElement]): Mod[HtmlElement]

  final def mainContent(content: Signal[HtmlElement]): HtmlElement =
    mainTag(aria.label := "Page content", renderMainContent(content))

  def appLayout(
      topbar: HtmlElement,
      sidebar: HtmlElement,
      mainContent: HtmlElement
  ): HtmlElement
}

object Theme {
  val all: List[Theme] = List(InlineTheme, CoreUiTheme, TailwindTheme)
  val default: Theme   = InlineTheme

  def forKey(key: String): Theme =
    all.find(_.key == key).getOrElse(default)
}
