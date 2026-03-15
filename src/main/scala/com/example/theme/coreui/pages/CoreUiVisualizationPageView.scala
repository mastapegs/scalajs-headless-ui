package com.example.theme.coreui.pages

import com.example.headless.components.{ColorScheme, PageContainer, Pattern}
import com.example.headless.pages.VisualizationPage
import com.example.theme.Theme
import com.example.webgl.WebGLRenderer
import com.raquo.laminar.api.L._
import com.raquo.laminar.codecs.StringAsIsCodec

object CoreUiVisualizationPageView {

  def render(page: VisualizationPage, theme: Theme): HtmlElement = {
    val viz = page.visualization

    theme.pageContainer(
      PageContainer(
        page.title,
        page.description,
        div(
          // Canvas card
          div(
            cls("card mb-4"),
            div(
              cls("card-body p-0"),
              styleAttr := "height: 400px;",
              WebGLRenderer.createCanvas(viz)
            )
          ),
          // Controls card
          div(
            cls("card"),
            div(
              cls("card-header"),
              h5(cls("card-title mb-0"), "Controls")
            ),
            div(
              cls("card-body"),
              div(
                cls("row g-4"),
                // Pattern selector
                div(
                  cls("col-md-6"),
                  label(cls("form-label fw-semibold"), "Pattern"),
                  div(
                    cls("btn-group d-flex flex-wrap"),
                    role := "group",
                    Pattern.all.map { p =>
                      button(
                        typ := "button",
                        cls("btn"),
                        cls <-- viz.pattern.map(cur => if (cur == p) "btn-primary" else "btn-outline-primary"),
                        Pattern.label(p),
                        onClick --> { _ => viz.setPattern(p) }
                      )
                    }
                  )
                ),
                // Color scheme selector
                div(
                  cls("col-md-6"),
                  label(cls("form-label fw-semibold"), "Color Scheme"),
                  div(
                    cls("btn-group d-flex flex-wrap"),
                    role := "group",
                    ColorScheme.all.map { cs =>
                      button(
                        typ := "button",
                        cls("btn"),
                        cls <-- viz.colorScheme.map(cur => if (cur == cs) "btn-primary" else "btn-outline-primary"),
                        ColorScheme.label(cs),
                        onClick --> { _ => viz.setColorScheme(cs) }
                      )
                    }
                  )
                ),
                // Speed slider
                div(
                  cls("col-md-6"),
                  label(
                    cls("form-label fw-semibold"),
                    "Speed: ",
                    child.text <-- viz.speed.map(s => f"$s%.1f")
                  ),
                  input(
                    cls("form-range"),
                    typ                                := "range",
                    htmlAttr("min", StringAsIsCodec)   := "0.1",
                    htmlAttr("max", StringAsIsCodec)   := "5.0",
                    htmlAttr("step", StringAsIsCodec)  := "0.1",
                    htmlAttr("value", StringAsIsCodec) := "1.0",
                    onInput.mapToValue.map(_.toDouble) --> { v => viz.setSpeed(v) }
                  )
                ),
                // Complexity slider
                div(
                  cls("col-md-6"),
                  label(
                    cls("form-label fw-semibold"),
                    "Complexity: ",
                    child.text <-- viz.complexity.map(_.toString)
                  ),
                  input(
                    cls("form-range"),
                    typ                                := "range",
                    htmlAttr("min", StringAsIsCodec)   := "1",
                    htmlAttr("max", StringAsIsCodec)   := "10",
                    htmlAttr("step", StringAsIsCodec)  := "1",
                    htmlAttr("value", StringAsIsCodec) := "5",
                    onInput.mapToValue.map(_.toInt) --> { v => viz.setComplexity(v) }
                  )
                ),
                // Mouse influence toggle
                div(
                  cls("col-12"),
                  div(
                    cls("form-check form-switch"),
                    input(
                      cls("form-check-input"),
                      typ    := "checkbox",
                      idAttr := "mouseInfluenceToggle",
                      role   := "switch",
                      checked <-- viz.mouseInfluence,
                      onChange.mapToChecked --> { _ => viz.toggleMouseInfluence() }
                    ),
                    label(
                      cls("form-check-label"),
                      forId := "mouseInfluenceToggle",
                      "Mouse Influence"
                    )
                  )
                )
              )
            )
          )
        )
      )
    )
  }
}
