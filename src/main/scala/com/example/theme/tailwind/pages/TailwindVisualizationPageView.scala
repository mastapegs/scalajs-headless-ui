package com.example.theme.tailwind.pages

import com.example.headless.components.{ColorScheme, PageContainer, Pattern}
import com.example.headless.pages.VisualizationPage
import com.example.theme.Theme
import com.example.webgl.WebGLRenderer
import com.raquo.laminar.api.L._

/** Tailwind visualization page with WebGL generative art canvas and interactive controls.
  *
  * '''Design techniques:'''
  *   - '''Canvas card''' with `rounded-xl overflow-hidden` for clean edges on the WebGL canvas
  *   - '''Pill buttons''' (`rounded-full`) for pattern and color scheme selection with indigo active state
  *   - '''Range inputs''' with `accent-indigo-600` for consistent theme color on sliders
  *   - '''Toggle switch''' using the same pattern as TailwindToggleView for mouse influence
  *   - '''Grid layout''' (`grid grid-cols-1 md:grid-cols-2 gap-6`) for responsive control arrangement
  */
object TailwindVisualizationPageView {

  def render(page: VisualizationPage, theme: Theme): HtmlElement = {
    val viz = page.visualization

    theme.pageContainer(
      PageContainer(
        page.title,
        page.description,
        div(
          cls("flex flex-col gap-6"),
          // Canvas
          div(
            cls("bg-white rounded-xl shadow-sm border border-gray-200/60 overflow-hidden"),
            styleAttr := "height: 400px;",
            WebGLRenderer.createCanvas(viz)
          ),
          // Controls
          div(
            cls("bg-white rounded-xl shadow-sm border border-gray-200/60 p-6"),
            h3(cls("text-lg font-semibold text-gray-900 mb-5"), "Controls"),
            div(
              cls("grid grid-cols-1 md:grid-cols-2 gap-6"),
              // Pattern selector
              div(
                label(cls("block text-sm font-medium text-gray-700 mb-2"), "Pattern"),
                div(
                  cls("flex flex-wrap gap-2"),
                  Pattern.all.map { p =>
                    button(
                      cls(
                        "px-4 py-2 rounded-full text-sm font-medium transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                      ),
                      cls <-- viz.pattern.map(cur =>
                        if (cur == p) "bg-indigo-600 text-white shadow-sm"
                        else "bg-gray-100 text-gray-700 hover:bg-gray-200"
                      ),
                      Pattern.label(p),
                      onClick --> { _ => viz.setPattern(p) }
                    )
                  }
                )
              ),
              // Color scheme selector
              div(
                label(cls("block text-sm font-medium text-gray-700 mb-2"), "Color Scheme"),
                div(
                  cls("flex flex-wrap gap-2"),
                  ColorScheme.all.map { cs =>
                    button(
                      cls(
                        "px-4 py-2 rounded-full text-sm font-medium transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                      ),
                      cls <-- viz.colorScheme.map(cur =>
                        if (cur == cs) "bg-indigo-600 text-white shadow-sm"
                        else "bg-gray-100 text-gray-700 hover:bg-gray-200"
                      ),
                      ColorScheme.label(cs),
                      onClick --> { _ => viz.setColorScheme(cs) }
                    )
                  }
                )
              ),
              // Speed slider
              div(
                label(
                  cls("block text-sm font-medium text-gray-700 mb-2"),
                  "Speed: ",
                  child.text <-- viz.speed.map(s => f"$s%.1f")
                ),
                input(
                  cls("w-full accent-indigo-600"),
                  typ                            := "range",
                  htmlAttr("min", stringCodec)   := "0.1",
                  htmlAttr("max", stringCodec)   := "5.0",
                  htmlAttr("step", stringCodec)  := "0.1",
                  htmlAttr("value", stringCodec) := "1.0",
                  onInput.mapToValue.map(_.toDouble) --> { v => viz.setSpeed(v) }
                )
              ),
              // Complexity slider
              div(
                label(
                  cls("block text-sm font-medium text-gray-700 mb-2"),
                  "Complexity: ",
                  child.text <-- viz.complexity.map(_.toString)
                ),
                input(
                  cls("w-full accent-indigo-600"),
                  typ                            := "range",
                  htmlAttr("min", stringCodec)   := "1",
                  htmlAttr("max", stringCodec)   := "10",
                  htmlAttr("step", stringCodec)  := "1",
                  htmlAttr("value", stringCodec) := "5",
                  onInput.mapToValue.map(_.toInt) --> { v => viz.setComplexity(v) }
                )
              ),
              // Mouse influence toggle
              div(
                cls("md:col-span-2"),
                div(
                  cls("flex items-center gap-3"),
                  button(
                    cls(
                      "relative inline-flex h-6 w-11 items-center rounded-full transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                    ),
                    cls <-- viz.mouseInfluence.map(on => if (on) "bg-indigo-600" else "bg-gray-200"),
                    role := "switch",
                    aria.checked <-- viz.mouseInfluence.map(_.toString),
                    aria.label := "Toggle mouse influence",
                    span(
                      cls(
                        "inline-block h-4 w-4 transform rounded-full bg-white shadow-sm transition-transform duration-200"
                      ),
                      cls <-- viz.mouseInfluence.map(on => if (on) "translate-x-6" else "translate-x-1")
                    ),
                    onClick --> { _ => viz.toggleMouseInfluence() }
                  ),
                  span(cls("text-sm font-medium text-gray-700"), "Mouse Influence")
                )
              )
            )
          )
        )
      )
    )
  }
}
