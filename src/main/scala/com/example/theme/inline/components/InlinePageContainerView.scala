package com.example.theme.inline.components

import com.example.headless.components.PageContainer
import com.raquo.laminar.api.L._

object InlinePageContainerView {
  def render(container: PageContainer[HtmlElement]): HtmlElement = div(
    maxWidth("1024px"),
    marginLeft("auto"),
    marginRight("auto"),
    h1(marginBottom("16px"), container.title),
    p(marginBottom("24px"), container.description),
    container.content
  )
}
