package com.example

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
}
