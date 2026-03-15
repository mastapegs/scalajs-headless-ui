package com.example.theme.inline

import com.example.headless.components._
import com.example.headless.pages.{DashboardPage, FetchPage, MetricsPage, SettingsPage, UIShowcasePage}
import com.example.theme.Theme
import com.example.theme.inline.components._
import com.example.theme.inline.pages._
import com.raquo.laminar.api.L._

object InlineTheme extends Theme {
  val key: String = "inline"

  override def card(card: Card[HtmlElement, HtmlElement]): HtmlElement = InlineCardView.render(card)
  override def pageContainer(container: PageContainer[HtmlElement]): HtmlElement =
    InlinePageContainerView.render(container)
  def counter(counter: Counter): HtmlElement                                = InlineCounterView.render(counter)
  def tabs(tabs: Tabs): HtmlElement                                         = InlineTabsView.render(tabs)
  def accordion(accordion: Accordion): HtmlElement                          = InlineAccordionView.render(accordion)
  def toggle(toggle: Toggle): HtmlElement                                   = InlineToggleView.render(toggle)
  def progress(progress: Progress): HtmlElement                             = InlineProgressView.render(progress)
  def tagsInput(tagsInput: TagsInput): HtmlElement                          = InlineTagsInputView.render(tagsInput)
  def tooltip(tooltip: Tooltip): HtmlElement                                = InlineTooltipView.render(tooltip)
  protected def renderSidebar(sidebar: Sidebar): HtmlElement                = InlineSidebarView.render(sidebar)
  protected def renderTopbar(topBar: TopBar, sidebar: Sidebar): HtmlElement = InlineTopbarView.render(topBar)

  def dashboardPage(page: DashboardPage): HtmlElement =
    InlineDashboardPageView.render(page, this)
  def metricsPage(page: MetricsPage): HtmlElement =
    InlineMetricsPageView.render(page, this)
  def settingsPage(page: SettingsPage): HtmlElement =
    InlineSettingsPageView.render(page, this)
  override def uiShowcasePage(page: UIShowcasePage): HtmlElement =
    InlineUIShowcasePageView.render(page, this)
  protected def renderFetchPage(page: FetchPage): HtmlElement =
    InlineFetchPageView.render(page, this)

  protected def renderMainContent(content: Signal[HtmlElement]): Mod[HtmlElement] =
    Seq(
      flexGrow(1),
      padding("32px"),
      overflowY.auto,
      child <-- content
    )

  def renderAppLayout(
      topbarEl: HtmlElement,
      sidebarEl: HtmlElement,
      mainContentEl: HtmlElement,
      sidebarModel: Sidebar
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
