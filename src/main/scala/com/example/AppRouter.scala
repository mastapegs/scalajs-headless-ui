package com.example

import com.example.headless.pages.{DashboardPage, FetchPage, MetricsPage, SettingsPage, UIShowcasePage}
import com.example.theme.Theme
import com.raquo.laminar.api.L._
import com.raquo.waypoint._

object AppRouter {

  private val dashboardRoute = Route.static(
    Page.Dashboard,
    root / endOfSegments,
    basePath = Router.localFragmentBasePath
  )

  private val metricsRoute = Route.static(
    Page.Metrics,
    root / "metrics" / endOfSegments,
    basePath = Router.localFragmentBasePath
  )

  private val settingsRoute = Route.static(
    Page.Settings,
    root / "settings" / endOfSegments,
    basePath = Router.localFragmentBasePath
  )

  private val fetchRoute = Route.static(
    Page.Fetch,
    root / "fetch" / endOfSegments,
    basePath = Router.localFragmentBasePath
  )

  private val uiShowcaseRoute = Route.static(
    Page.UIShowcase,
    root / "ui-showcase" / endOfSegments,
    basePath = Router.localFragmentBasePath
  )

  val router = new Router[Page](
    routes = List(dashboardRoute, metricsRoute, settingsRoute, fetchRoute, uiShowcaseRoute),
    serializePage = Page.serialize _,
    deserializePage = Page.deserialize _,
    getPageTitle = page => s"${Page.label(page)} | UI Template"
  )

  private val dashboardPage  = new DashboardPage()
  private val metricsPage    = new MetricsPage()
  private val settingsPage   = new SettingsPage()
  private val fetchPage      = new FetchPage()
  private val uiShowcasePage = new UIShowcasePage()

  private def pageContent(page: Page, theme: Theme): HtmlElement =
    page match {
      case Page.Dashboard  => theme.dashboardPage(dashboardPage)
      case Page.Metrics    => theme.metricsPage(metricsPage)
      case Page.Settings   => theme.settingsPage(settingsPage)
      case Page.Fetch      => theme.fetchPage(fetchPage)
      case Page.UIShowcase => theme.uiShowcasePage(uiShowcasePage)
    }

  def pageContentSignal(theme: Theme): Signal[HtmlElement] =
    router.currentPageSignal.map(pageContent(_, theme))
}
