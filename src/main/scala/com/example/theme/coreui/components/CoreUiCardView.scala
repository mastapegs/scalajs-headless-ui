package com.example.theme.coreui.components

import com.example.headless.components.Card
import com.raquo.laminar.api.L._

object CoreUiCardView {

  def render(card: Card[HtmlElement, HtmlElement]): HtmlElement = div(
    cls("card mb-4"),
    div(
      cls("card-header"),
      h5(cls("card-title mb-0"), card.title)
    ),
    div(cls("card-body"), card.content)
  )
}
