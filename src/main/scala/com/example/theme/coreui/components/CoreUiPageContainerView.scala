package com.example.theme.coreui.components

import com.example.headless.components.PageContainer
import com.raquo.laminar.api.L._

object CoreUiPageContainerView {
  def render(container: PageContainer[HtmlElement]): HtmlElement = div(
    cls("container-lg"),
    h1(cls("mb-3"), container.title),
    p(container.description),
    container.content
  )
}
