package com.example.theme.tailwind.components

import com.example.headless.components.PageContainer
import com.raquo.laminar.api.L._

/** Tailwind page container with max-width constraint and centered layout.
  *
  * '''Design techniques:'''
  *   - '''Max-width container''' (`max-w-5xl mx-auto`) keeps content readable on wide screens
  *   - '''Page heading''' uses `text-2xl font-bold text-gray-900` for clear visual hierarchy
  *   - '''Muted description''' (`text-gray-500`) distinguishes helper text from the title
  */
object TailwindPageContainerView {
  def render(container: PageContainer[HtmlElement]): HtmlElement = div(
    cls("max-w-5xl mx-auto"),
    h1(cls("text-2xl font-bold text-gray-900 mb-2"), container.title),
    p(cls("text-gray-500 mb-8"), container.description),
    container.content
  )
}
