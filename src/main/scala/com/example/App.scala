package com.example

import com.raquo.laminar.api.L._
import com.example.headless.{Counter, Sidebar}
import com.example.renderers._
import org.scalajs.dom

object App {

  private val router = AppRouter.router

  private val rendererVar: Var[SidebarRenderer] = Var(InlineSidebarRenderer)
  private val counterRendererVar: Var[CounterRenderer] = Var(InlineCounterRenderer)

  def main(args: Array[String]): Unit = {
    val container = dom.document.querySelector("#appContainer")
    render(container, appElement())
  }

  private val counter = new Counter()

  private val sidebar = new Sidebar(
    pages = Page.all,
    currentPage = router.currentPageSignal,
    onNavigate = page => router.pushState(page)
  )

  private def rendererToggle(): HtmlElement =
    div(
      display.flex,
      alignItems.center,
      gap("8px"),
      label("Renderer:"),
      select(
        option(value("inline"), "Inline Styles"),
        option(value("coreui"), "CoreUI"),
        inContext { el =>
          onChange.mapTo(el.ref.value) --> { v =>
            val r: SidebarRenderer = v match {
              case "coreui" => CoreUiSidebarRenderer
              case _        => InlineSidebarRenderer
            }
            val cr: CounterRenderer = v match {
              case "coreui" => CoreUiCounterRenderer
              case _        => InlineCounterRenderer
            }
            rendererVar.set(r)
            counterRendererVar.set(cr)
          }
        }
      )
    )

  private def topBar(): HtmlElement =
    headerTag(
      display.flex,
      alignItems.center,
      justifyContent.spaceBetween,
      position.fixed,
      top("0"),
      left("0"),
      width("100%"),
      height("56px"),
      backgroundColor("#2c3e50"),
      color("white"),
      padding("0 24px"),
      zIndex(100),
      span(fontSize("18px"), fontWeight("600"), "UI Template"),
      rendererToggle()
    )

  private def pageContent(page: Page): HtmlElement = page match {
    case Page.Dashboard =>
      div(
        h1(marginBottom("16px"), "Dashboard"),
        p("Welcome to the dashboard. This is the main overview page."),
        div(
          marginTop("24px"),
          child <-- counterRendererVar.signal.map(_.render(counter))
        )
      )
    case Page.Metrics =>
      div(
        h1(marginBottom("16px"), "Metrics"),
        p("Charts and metrics would be displayed here.")
      )
    case Page.Settings =>
      div(
        h1(marginBottom("16px"), "Settings"),
        p("Application settings and preferences.")
      )
  }

  private def mainContent(): HtmlElement =
    div(
      flexGrow(1),
      padding("32px"),
      overflowY.auto,
      child <-- router.currentPageSignal.map(pageContent)
    )

  private def appElement(): HtmlElement =
    div(
      topBar(),
      div(
        display.flex,
        marginTop("56px"),
        height("calc(100vh - 56px)"),
        child <-- rendererVar.signal.map(_.render(sidebar)),
        mainContent()
      )
    )
}
