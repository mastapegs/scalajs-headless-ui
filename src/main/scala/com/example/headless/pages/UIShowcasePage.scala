package com.example.headless.pages

import com.example.headless.components._

final class UIShowcasePage {

  val title: String = "UI Showcase"
  val description: String =
    "A showcase of headless UI components demonstrating the separation of state and logic from presentation."

  val tabs: Tabs = new Tabs(
    List(
      Tabs.TabDef("Overview", "This tab shows an overview of the headless UI pattern and its benefits."),
      Tabs.TabDef("Components", "Headless components manage state and behavior without any rendering logic."),
      Tabs.TabDef("Themes", "Themes provide the visual layer, rendering headless state in completely different styles.")
    )
  )

  val accordion: Accordion = new Accordion(
    List(
      Accordion.ItemDef(
        "what",
        "What is Headless UI?",
        "Headless UI components provide state management and behavior logic without dictating any visual presentation. The same component can be rendered with completely different styles across themes."
      ),
      Accordion.ItemDef(
        "why",
        "Why Headless?",
        "By separating logic from rendering, you get fully testable business logic, swappable themes at runtime, and zero coupling between state and presentation."
      ),
      Accordion.ItemDef(
        "how",
        "How does it work?",
        "Components expose reactive Signals for state and methods for behavior. Theme views subscribe to these signals and render them using their own styling approach — inline CSS, framework classes, or utility classes."
      )
    )
  )

  val toggleDarkMode: Toggle      = new Toggle("Dark Mode")
  val toggleNotifications: Toggle = new Toggle("Notifications", initialValue = true)

  val progress: Progress = new Progress("Upload Progress", max = 100, initialValue = 0)

  val tagsInput: TagsInput = new TagsInput("Skills", initialTags = List("Scala", "Laminar"), maxTags = 8)

  val tooltip: Tooltip = new Tooltip("This button has a headless tooltip attached to it!", placement = "top")
}
