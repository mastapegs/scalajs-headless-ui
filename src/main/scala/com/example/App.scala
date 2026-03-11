package com.example

import com.example.headless.components.{Sidebar, TopBar}
import com.example.theme.Theme
import com.raquo.laminar.api.L._
import org.scalajs.dom

object App {

  private val router = AppRouter.router

  private val rendererKeyVar: Var[String] = Var("inline")

  private val theme: Var[Theme] = Var(Theme.default)

  def main(args: Array[String]): Unit = {
    val container = dom.document.querySelector("#appContainer")
    render(container, appElement())
  }

  private val sidebar = new Sidebar(
    pages = Page.all,
    currentPage = router.currentPageSignal,
    onNavigate = page => router.pushState(page)
  )

  private val topBar = new TopBar(
    brandName = "UI Template",
    currentRenderer = rendererKeyVar.signal,
    onRendererChange = { key =>
      val old  = theme.now()
      val next = Theme.forKey(key)
      old.onDeactivate()
      next.onActivate()
      rendererKeyVar.set(key)
      theme.set(next)
    }
  )

  private def appElement(): HtmlElement =
    div(
      child <-- theme.signal.map { currentTheme =>
        currentTheme.appLayout(
          topbar = currentTheme.topbar(topBar),
          sidebar = currentTheme.sidebar(sidebar),
          mainContent = currentTheme.mainContent(AppRouter.pageContentSignal(currentTheme))
        )
      }
    )
}
