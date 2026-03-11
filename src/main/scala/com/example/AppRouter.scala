package com.example

import com.example.headless.components.Counter
import com.example.headless.pages.{DashboardPage, MetricsPage, SettingsPage}
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

  val router = new Router[Page](
    routes = List(dashboardRoute, metricsRoute, settingsRoute),
    serializePage = Page.serialize _,
    deserializePage = Page.deserialize _,
    getPageTitle = page => s"${Page.label(page)} | UI Template"
  )

  private val counter       = new Counter()
  private val dashboardPage = new DashboardPage()
  private val metricsPage   = new MetricsPage()
  private val settingsPage  = new SettingsPage()

  private def pageContent(page: Page, theme: Theme): HtmlElement =
    page match {
      case Page.Dashboard => theme.dashboardPage(dashboardPage, counter)
      case Page.Metrics   => theme.metricsPage(metricsPage)
      case Page.Settings  => theme.settingsPage(settingsPage)
    }

  def pageContentSignal(theme: Theme): Signal[HtmlElement] =
    router.currentPageSignal.map(pageContent(_, theme))
}
