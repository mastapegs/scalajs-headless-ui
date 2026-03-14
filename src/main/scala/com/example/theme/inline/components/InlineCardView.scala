package com.example.theme.inline.components

import com.example.headless.components.Card
import com.raquo.laminar.api.L._

object InlineCardView {

  def render(card: Card, content: HtmlElement): HtmlElement = div(
    marginBottom("32px"),
    h2(
      fontSize("18px"),
      fontWeight("600"),
      marginBottom("12px"),
      borderBottom("2px solid #2c3e50"),
      paddingBottom("8px"),
      card.title
    ),
    content
  )
}
