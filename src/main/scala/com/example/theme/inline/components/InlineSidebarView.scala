package com.example.theme.inline.components

import org.scalajs.dom
import com.raquo.laminar.api.L._

import com.example.headless.Sidebar
import com.example.Page

object InlineSidebarView {
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

  def render(sidebar: Sidebar): HtmlElement = {
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
}
