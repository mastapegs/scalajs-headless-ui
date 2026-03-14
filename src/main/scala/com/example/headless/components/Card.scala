package com.example.headless.components

import com.raquo.laminar.api.L._

/** Headless card component: a titled container for grouping content, no rendering. */
final case class Card(title: HtmlElement, content: HtmlElement)
