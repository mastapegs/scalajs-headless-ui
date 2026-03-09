package com.example.theme.inline

import com.raquo.laminar.api.L._
import org.scalajs.dom

import com.example.headless.Counter
import com.example.headless.Sidebar
import com.example.headless.TopBar
import com.example.Page
import com.example.theme.Theme

object InlineTheme extends Theme {
  def counter(counter: Counter): HtmlElement = div(
    display.flex,
    alignItems.center,
    gap("16px"),
    padding("16px"),
    border("1px solid #bdc3c7"),
    borderRadius("8px"),
    backgroundColor("#f8f9fa"),

    span(
      fontSize("24px"),
      fontWeight("600"),
      child.text <-- counter.count.map(_.toString)
    ),

    button(
      padding("8px 16px"),
      border("1px solid #2c3e50"),
      borderRadius("4px"),
      backgroundColor("#2c3e50"),
      color("white"),
      cursor.pointer,
      fontSize("14px"),
      "Increment",
      onClick --> { _ => counter.increment() }
    )
  )
  def sidebar(sidebar: Sidebar): HtmlElement = {
    injectHoverStyles()

    htmlTag("nav")(
      display.flex,
      flexDirection.column,
      width <-- sidebar.isCollapsed.map(if (_) "60px" else "220px"),
      transition := "width 0.2s ease",
      flexShrink("0"),
      backgroundColor("#ecf0f1"),
      padding("16px 8px"),
      borderRight("1px solid #bdc3c7"),
      overflowX.hidden,

      // Collapse toggle button
      button(
        display.flex,
        alignItems.center,
        justifyContent.center,
        width("100%"),
        padding("8px"),
        marginBottom("16px"),
        border("1px solid #bdc3c7"),
        borderRadius("4px"),
        backgroundColor("white"),
        cursor.pointer,
        fontSize("14px"),
        child.text <-- sidebar.isCollapsed.map(if (_) "\u25B6" else "\u25C0"),
        onClick --> { _ => sidebar.toggleCollapse() }
      ),

      // Page items
      sidebar.pages.map { page =>
        div(
          cls("inline-sidebar-item"),
          cls <-- sidebar.isActive(page).map(if (_) "active" else ""),
          display.flex,
          alignItems.center,
          padding("10px 12px"),
          marginBottom("4px"),
          borderRadius("4px"),
          cursor.pointer,
          whiteSpace.nowrap,
          child.text <-- sidebar.isCollapsed.map { collapsed =>
            if (collapsed) Page.label(page).take(1) else Page.label(page)
          },
          onClick --> { _ => sidebar.navigateTo(page) }
        )
      }
    )
  }
  def topbar(topBar: TopBar): HtmlElement =
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
      span(fontSize("18px"), fontWeight("600"), topBar.brandName),
      rendererToggle(topBar)
    )

  private def injectHoverStyles(): Unit = {
    val exists =
      dom.document.querySelector("[data-inline-sidebar-styles]") != null
    if (!exists) {
      val el = dom.document.createElement("style")
      el.setAttribute("data-inline-sidebar-styles", "")
      el.textContent = """.inline-sidebar-item:hover { background: #d5dbdb !important; }
          |.inline-sidebar-item.active { background: #2c3e50 !important; color: white !important; }
          |""".stripMargin
      dom.document.head.appendChild(el)
    }
  }

  private def rendererToggle(topBar: TopBar): HtmlElement =
    div(
      display.flex,
      alignItems.center,
      gap("8px"),
      label("Renderer:"),
      select(
        topBar.rendererOptions.map { case (v, lbl) =>
          option(value(v), lbl)
        },
        controlled(
          value <-- topBar.currentRenderer,
          onChange.mapToValue --> { v =>
            topBar.selectRenderer(v)
          }
        )
      )
    )
}
