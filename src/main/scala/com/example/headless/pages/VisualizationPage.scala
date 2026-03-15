package com.example.headless.pages

import com.example.headless.components.Visualization

/** Headless visualization page: owns a Visualization instance for WebGL generative art. */
final class VisualizationPage {

  val title: String = "Visualizations"
  val description: String =
    "Interactive WebGL generative art gallery with real-time parameter controls."

  val visualization: Visualization = new Visualization()
}
