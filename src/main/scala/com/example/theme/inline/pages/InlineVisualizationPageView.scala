package com.example.theme.inline.pages

import com.example.headless.components.{ColorScheme, PageContainer, Pattern}
import com.example.headless.pages.VisualizationPage
import com.example.theme.Theme
import com.example.webgl.WebGLRenderer
import com.raquo.laminar.api.L._

object InlineVisualizationPageView {

  def render(page: VisualizationPage, theme: Theme): HtmlElement = {
    val viz = page.visualization

    theme.pageContainer(
      PageContainer(
        page.title,
        page.description,
        div(
          display.flex,
          flexDirection.column,
          gap("24px"),
          // Canvas
          div(
            border("1px solid #ddd"),
            borderRadius("8px"),
            overflow.hidden,
            height("400px"),
            WebGLRenderer.createCanvas(viz)
          ),
          // Controls
          div(
            display.flex,
            flexDirection.column,
            gap("16px"),
            padding("20px"),
            backgroundColor("#f9fafb"),
            borderRadius("8px"),
            border("1px solid #e5e7eb"),
            // Pattern selector
            div(
              div(
                fontWeight.bold,
                fontSize("14px"),
                marginBottom("8px"),
                color("#374151"),
                "Pattern"
              ),
              div(
                display.flex,
                gap("8px"),
                flexWrap.wrap,
                Pattern.all.map { p =>
                  button(
                    padding("6px 16px"),
                    borderRadius("6px"),
                    cursor.pointer,
                    fontSize("13px"),
                    fontWeight("500"),
                    border("1px solid #d1d5db"),
                    backgroundColor <-- viz.pattern.map(cur => if (cur == p) "#4f46e5" else "#ffffff"),
                    color <-- viz.pattern.map(cur => if (cur == p) "#ffffff" else "#374151"),
                    Pattern.label(p),
                    onClick --> { _ => viz.setPattern(p) }
                  )
                }
              )
            ),
            // Speed slider
            div(
              div(
                fontWeight.bold,
                fontSize("14px"),
                marginBottom("8px"),
                color("#374151"),
                "Speed: ",
                child.text <-- viz.speed.map(s => f"$s%.1f")
              ),
              input(
                typ                            := "range",
                htmlAttr("min", stringCodec)   := "0.1",
                htmlAttr("max", stringCodec)   := "5.0",
                htmlAttr("step", stringCodec)  := "0.1",
                htmlAttr("value", stringCodec) := "1.0",
                width("100%"),
                onInput.mapToValue.map(_.toDouble) --> { v => viz.setSpeed(v) }
              )
            ),
            // Color scheme selector
            div(
              div(
                fontWeight.bold,
                fontSize("14px"),
                marginBottom("8px"),
                color("#374151"),
                "Color Scheme"
              ),
              div(
                display.flex,
                gap("8px"),
                flexWrap.wrap,
                ColorScheme.all.map { cs =>
                  button(
                    padding("6px 16px"),
                    borderRadius("6px"),
                    cursor.pointer,
                    fontSize("13px"),
                    fontWeight("500"),
                    border("1px solid #d1d5db"),
                    backgroundColor <-- viz.colorScheme.map(cur => if (cur == cs) "#4f46e5" else "#ffffff"),
                    color <-- viz.colorScheme.map(cur => if (cur == cs) "#ffffff" else "#374151"),
                    ColorScheme.label(cs),
                    onClick --> { _ => viz.setColorScheme(cs) }
                  )
                }
              )
            ),
            // Complexity slider
            div(
              div(
                fontWeight.bold,
                fontSize("14px"),
                marginBottom("8px"),
                color("#374151"),
                "Complexity: ",
                child.text <-- viz.complexity.map(_.toString)
              ),
              input(
                typ                            := "range",
                htmlAttr("min", stringCodec)   := "1",
                htmlAttr("max", stringCodec)   := "10",
                htmlAttr("step", stringCodec)  := "1",
                htmlAttr("value", stringCodec) := "5",
                width("100%"),
                onInput.mapToValue.map(_.toInt) --> { v => viz.setComplexity(v) }
              )
            ),
            // Mouse influence toggle
            div(
              display.flex,
              alignItems.center,
              gap("8px"),
              label(
                display.flex,
                alignItems.center,
                gap("8px"),
                cursor.pointer,
                input(
                  typ := "checkbox",
                  checked <-- viz.mouseInfluence,
                  onChange.mapToChecked --> { _ => viz.toggleMouseInfluence() }
                ),
                span(
                  fontSize("14px"),
                  fontWeight("500"),
                  color("#374151"),
                  "Mouse Influence"
                )
              )
            )
          )
        )
      )
    )
  }
}
