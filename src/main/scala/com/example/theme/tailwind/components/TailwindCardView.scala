package com.example.theme.tailwind.components

import com.example.headless.components.Card
import com.raquo.laminar.api.L._

object TailwindCardView {

  def render(card: Card, content: HtmlElement): HtmlElement = div(
    cls("bg-white rounded-xl shadow-md border border-gray-200 overflow-hidden"),
    div(
      cls("px-7 py-5 border-b border-gray-200 bg-gray-50/80"),
      h3(cls("text-base font-semibold text-gray-800"), card.title)
    ),
    div(cls("p-7"), content)
  )
}
