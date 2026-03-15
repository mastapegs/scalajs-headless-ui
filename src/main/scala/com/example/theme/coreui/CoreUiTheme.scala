package com.example.theme.coreui

import com.example.headless.components._
import com.example.headless.pages.{DashboardPage, FetchPage, MetricsPage, SettingsPage, UIShowcasePage}
import com.example.theme.Theme
import com.example.theme.coreui.components._
import com.example.theme.coreui.pages._
import com.raquo.laminar.api.L._
import org.scalajs.dom

object CoreUiTheme extends Theme {
  val key: String = "coreui"

  private val stylesheetId  = "coreui-stylesheet"
  private val stylesheetUrl = "https://unpkg.com/@coreui/coreui@5.3.1/dist/css/coreui.min.css"
  private val helperStyleId = "coreui-helper-styles"

  // NOTE: CoreUI JS bundle is intentionally NOT loaded. Our headless components
  // manage all interactive state — CoreUI is used for CSS layout/styling only.
  // Loading CoreUI's JS would conflict with Laminar's reactive DOM management.

  override def onActivate(): Unit = {
    if (dom.document.getElementById(stylesheetId) == null) {
      val link = dom.document.createElement("link")
      link.setAttribute("id", stylesheetId)
      link.setAttribute("rel", "stylesheet")
      link.setAttribute("href", stylesheetUrl)
      dom.document.head.appendChild(link)
    }
    // Inject helper CSS for the wrapper ↔ sidebar relationship.
    // CoreUI's JS normally manages --cui-sidebar-occupy-start; without JS
    // we set the wrapper padding explicitly and handle mobile overrides.
    if (dom.document.getElementById(helperStyleId) == null) {
      val style = dom.document.createElement("style")
      style.setAttribute("id", helperStyleId)
      style.textContent = """|.wrapper {
           |  padding-left: var(--cui-sidebar-occupy-start, 0);
           |}
           |""".stripMargin
      dom.document.head.appendChild(style)
    }
  }

  override def onDeactivate(): Unit = {
    val existing = dom.document.getElementById(stylesheetId)
    if (existing != null) existing.parentNode.removeChild(existing)
    val helper = dom.document.getElementById(helperStyleId)
    if (helper != null) helper.parentNode.removeChild(helper)
  }

  override def card(card: Card[HtmlElement, HtmlElement]): HtmlElement = CoreUiCardView.render(card)
  override def pageContainer(container: PageContainer[HtmlElement]): HtmlElement =
    CoreUiPageContainerView.render(container)
  def counter(counter: Counter): HtmlElement                 = CoreUiCounterView.render(counter)
  def tabs(tabs: Tabs): HtmlElement                          = CoreUiTabsView.render(tabs)
  def accordion(accordion: Accordion): HtmlElement           = CoreUiAccordionView.render(accordion)
  def toggle(toggle: Toggle): HtmlElement                    = CoreUiToggleView.render(toggle)
  def progress(progress: Progress): HtmlElement              = CoreUiProgressView.render(progress)
  def tagsInput(tagsInput: TagsInput): HtmlElement           = CoreUiTagsInputView.render(tagsInput)
  def tooltip(tooltip: Tooltip): HtmlElement                 = CoreUiTooltipView.render(tooltip)
  protected def renderSidebar(sidebar: Sidebar): HtmlElement = CoreUiSidebarView.render(sidebar)
  protected def renderTopbar(topBar: TopBar, sidebar: Sidebar): HtmlElement =
    CoreUiTopbarView.render(topBar, () => sidebar.toggleCollapse())

  def dashboardPage(page: DashboardPage): HtmlElement =
    CoreUiDashboardPageView.render(page, this)
  def metricsPage(page: MetricsPage): HtmlElement =
    CoreUiMetricsPageView.render(page, this)
  def settingsPage(page: SettingsPage): HtmlElement =
    CoreUiSettingsPageView.render(page, this)
  override def uiShowcasePage(page: UIShowcasePage): HtmlElement =
    CoreUiUIShowcasePageView.render(page, this)
  protected def renderFetchPage(page: FetchPage): HtmlElement =
    CoreUiFetchPageView.render(page, this)

  protected def renderMainContent(content: Signal[HtmlElement]): Mod[HtmlElement] =
    Seq(
      cls("flex-grow-1 overflow-auto p-4"),
      child <-- content
    )

  // CoreUI layout: sidebar is a fixed-position sibling to .wrapper.
  // The .wrapper uses padding-left driven by --cui-sidebar-occupy-start
  // to offset its content beside the sidebar.
  def renderAppLayout(
      topbarEl: HtmlElement,
      sidebarEl: HtmlElement,
      mainContentEl: HtmlElement,
      sidebarModel: Sidebar
  ): HtmlElement =
    div(
      sidebarEl,
      // Backdrop overlay shown when sidebar is open on mobile.
      // pointer-events:none when hidden so it doesn't block clicks on content.
      div(
        cls("sidebar-backdrop fade"),
        cls <-- sidebarModel.isCollapsed.map(if (_) "show" else ""),
        pointerEvents <-- sidebarModel.isCollapsed.map(if (_) "auto" else "none"),
        onClick --> { _ => sidebarModel.toggleCollapse() }
      ),
      div(
        cls("wrapper d-flex flex-column min-vh-100"),
        topbarEl,
        div(
          cls("body flex-grow-1 overflow-auto"),
          mainContentEl
        )
      )
    )
}
