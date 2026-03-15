package com.example.headless.components

/** Headless page container component: a titled, described wrapper for page content, no rendering.
  *
  * Captures the common page-level structure (title + description + body content) as a pure data container, allowing
  * each theme to render it with its own container strategy (e.g. `container-lg` in CoreUI, `max-w-5xl mx-auto` in
  * Tailwind).
  */
final case class PageContainer[C](title: String, description: String, content: C)
