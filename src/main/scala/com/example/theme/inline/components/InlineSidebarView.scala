package com.example.theme.inline.components

import com.example.Page
import com.example.headless.Sidebar
import com.raquo.laminar.api.L._
import org.scalajs.dom

object InlineSidebarView {
  private def injectHoverStyles(): Unit = {
    val exists =
      dom.document.querySelector("[data-inline-sidebar-styles]") != null
    if (!exists) {
      val el = dom.document.createElement("style")
      el.setAttribute("data-inline-sidebar-styles", "")
      el.textContent = """.inline-sidebar-item:hover { background: #d5dbdb !important; }
          |.inline-sidebar-item.active { background: #2c3e50 !important; color: white !important; }
          |@media (min-width: 769px) {
          |  .inline-hamburger { display: none !important; }
          |  .inline-sidebar-backdrop { display: none !important; }
          |}
          |@media (max-width: 768px) {
          |  .inline-sidebar-mobile { display: none !important; }
          |  .inline-sidebar-mobile.mobile-open { display: flex !important; position: fixed !important; top: 56px !important; left: 0 !important; bottom: 0 !important; z-index: 99 !important; width: 220px !important; }
          |  .inline-sidebar-backdrop { position: fixed; top: 56px; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.4); z-index: 98; }
          |  .inline-sidebar-backdrop.hidden { display: none !important; }
          |}
          |""".stripMargin
      dom.document.head.appendChild(el)
    }
  }

  def render(sidebar: Sidebar): HtmlElement = {
    injectHoverStyles()

    div(
      // Backdrop for mobile overlay
      div(
        cls("inline-sidebar-backdrop"),
        cls <-- sidebar.isMobileOpen.map(if (_) "" else "hidden"),
        onClick --> { _ => sidebar.closeMobile() }
      ),
      htmlTag("nav")(
        cls("inline-sidebar-mobile"),
        cls <-- sidebar.isMobileOpen.map(if (_) "mobile-open" else ""),
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
    )
  }
}
