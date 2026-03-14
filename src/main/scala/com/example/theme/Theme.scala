package com.example.theme

import com.example.AppRouter
import com.example.headless.components._
import com.example.headless.pages.{DashboardPage, FetchPage, MetricsPage, SettingsPage, UIShowcasePage}
import com.example.theme.coreui.CoreUiTheme
import com.example.theme.inline.InlineTheme
import com.example.theme.tailwind.TailwindTheme
import com.raquo.laminar.api.L._

trait Theme {
  def key: String

  def onActivate(): Unit   = ()
  def onDeactivate(): Unit = ()

  def counter(counter: Counter): HtmlElement
  def tabs(tabs: Tabs): HtmlElement
  def accordion(accordion: Accordion): HtmlElement
  def toggle(toggle: Toggle): HtmlElement
  def progress(progress: Progress): HtmlElement
  def tagsInput(tagsInput: TagsInput): HtmlElement
  def tooltip(tooltip: Tooltip): HtmlElement
  def card(card: Card): HtmlElement = div(card.title, card.content)

  protected def renderTopbar(topBar: TopBar, sidebar: Sidebar): HtmlElement

  final def topbar(topBar: TopBar, sidebar: Sidebar): HtmlElement =
    renderTopbar(topBar, sidebar).amend(aria.label := "Top bar")

  protected def renderSidebar(sidebar: Sidebar): HtmlElement

  final def sidebar(sidebar: Sidebar): HtmlElement =
    renderSidebar(sidebar).amend(aria.label := "Main navigation")

  def dashboardPage(page: DashboardPage): HtmlElement
  def metricsPage(page: MetricsPage): HtmlElement
  def settingsPage(page: SettingsPage): HtmlElement

  def uiShowcasePage(page: UIShowcasePage): HtmlElement =
    div(h1(page.title), p(page.description))

  protected def renderFetchPage(page: FetchPage): HtmlElement

  final def fetchPage(page: FetchPage): HtmlElement =
    renderFetchPage(page).amend(
      onMountCallback { ctx =>
        page.fetchPosts().addObserver(Observer.empty)(ctx.owner)
      }
    )

  protected def renderMainContent(content: Signal[HtmlElement]): Mod[HtmlElement]

  final def mainContent(content: Signal[HtmlElement]): HtmlElement =
    mainTag(aria.label := "Page content", renderMainContent(content))

  protected def renderAppLayout(
      topbar: HtmlElement,
      sidebar: HtmlElement,
      mainContent: HtmlElement,
      sidebarModel: Sidebar
  ): HtmlElement

  final def appLayout(topbar: TopBar, sidebar: Sidebar): HtmlElement =
    renderAppLayout(
      this.topbar(topbar, sidebar),
      this.sidebar(sidebar),
      this.mainContent(AppRouter.pageContentSignal(this)),
      sidebar
    )
}

object Theme {
  val all: List[Theme] = List(InlineTheme, CoreUiTheme, TailwindTheme)
  val default: Theme   = InlineTheme

  def forKey(key: String): Theme =
    all.find(_.key == key).getOrElse(default)
}
