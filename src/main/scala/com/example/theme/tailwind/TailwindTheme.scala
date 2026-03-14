package com.example.theme.tailwind

import com.example.headless.components._
import com.example.headless.pages.{DashboardPage, FetchPage, MetricsPage, SettingsPage, UIShowcasePage}
import com.example.theme.Theme
import com.example.theme.tailwind.components._
import com.example.theme.tailwind.pages._
import com.raquo.laminar.api.L._
import org.scalajs.dom

/** Tailwind CSS v4 theme implementation.
  *
  * Loads the Tailwind CSS browser script from the jsDelivr CDN at activation time and removes it on deactivation. All
  * styling is expressed through Tailwind utility classes applied via Laminar's `cls(...)` modifier.
  *
  * ==Design System==
  *
  * The theme follows a consistent design language across every component and page:
  *
  *   - '''Color palette''' — Indigo (`indigo-500`/`indigo-600`) as the primary accent, Slate (`slate-800`/`slate-900`)
  *     for dark surfaces (top bar, sidebar), and neutral Grays for text and borders.
  *   - '''Card surfaces''' — `bg-white rounded-xl shadow-sm border border-gray-200/60` with `hover:shadow-md
  *     transition-shadow` for interactive lift.
  *   - '''Typography hierarchy''' — `text-3xl font-bold text-gray-900` for hero values, `text-sm font-medium
  *     text-gray-500` for labels, `leading-relaxed` for body text.
  *   - '''Button system''' — Primary buttons use `bg-indigo-600 hover:bg-indigo-700` with `focus:ring-2
  *     focus:ring-indigo-500 focus:ring-offset-2` for accessible focus indication. Secondary buttons use
  *     `bg-white border border-gray-300`.
  *   - '''Transitions''' — `transition-colors duration-150` on buttons and links, `transition-all duration-200` on
  *     layout changes, `transition-shadow` on cards for a polished, responsive feel.
  *   - '''Spacing''' — Consistent use of Tailwind's spacing scale (`p-5`, `p-6`, `gap-3`, `gap-5`, `space-y-3`) to
  *     maintain visual rhythm throughout the interface.
  *   - '''Layout''' — `bg-gray-50` main content background creates contrast against white cards, a classic dashboard
  *     pattern.
  *
  * ==Accessibility==
  *
  * Focus rings (`focus:ring-2 focus:ring-offset-2`), ARIA attributes (`aria-label`, `aria-live`, `role="switch"`), and
  * keyboard-navigable controls are applied consistently across all components.
  */
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

  def counter(counter: Counter): HtmlElement                                = TailwindCounterView.render(counter)
  def tabs(tabs: Tabs): HtmlElement                                         = TailwindTabsView.render(tabs)
  def accordion(accordion: Accordion): HtmlElement                          = TailwindAccordionView.render(accordion)
  def toggle(toggle: Toggle): HtmlElement                                   = TailwindToggleView.render(toggle)
  def progress(progress: Progress): HtmlElement                             = TailwindProgressView.render(progress)
  def tagsInput(tagsInput: TagsInput): HtmlElement                          = TailwindTagsInputView.render(tagsInput)
  def tooltip(tooltip: Tooltip): HtmlElement                                = TailwindTooltipView.render(tooltip)
  protected def renderSidebar(sidebar: Sidebar): HtmlElement                = TailwindSidebarView.render(sidebar)
  protected def renderTopbar(topBar: TopBar, sidebar: Sidebar): HtmlElement = TailwindTopbarView.render(topBar)

  def dashboardPage(page: DashboardPage): HtmlElement =
    TailwindDashboardPageView.render(page, this)
  def metricsPage(page: MetricsPage): HtmlElement =
    TailwindMetricsPageView.render(page)
  def settingsPage(page: SettingsPage): HtmlElement =
    TailwindSettingsPageView.render(page)
  override def uiShowcasePage(page: UIShowcasePage): HtmlElement =
    TailwindUIShowcasePageView.render(page, this)
  protected def renderFetchPage(page: FetchPage): HtmlElement =
    TailwindFetchPageView.render(page)

  protected def renderMainContent(content: Signal[HtmlElement]): Mod[HtmlElement] =
    Seq(
      cls("flex-grow overflow-y-auto p-8 bg-gray-50"),
      child <-- content
    )

  def renderAppLayout(
      topbarEl: HtmlElement,
      sidebarEl: HtmlElement,
      mainContentEl: HtmlElement,
      sidebarModel: Sidebar
  ): HtmlElement =
    div(
      cls("min-h-screen bg-gray-50"),
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
