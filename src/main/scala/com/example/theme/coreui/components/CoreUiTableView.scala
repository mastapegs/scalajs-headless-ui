package com.example.theme.coreui.components

import com.example.headless.components.Table
import com.raquo.laminar.api.L._

object CoreUiTableView {
  def render(t: Table): HtmlElement =
    div(
      cls("table-responsive"),
      table(
        cls("table table-striped table-hover"),
        thead(
          tr(t.headers.map(h => th(cls("text-nowrap"), h)))
        ),
        tbody(
          t.rows.map(row => tr(row.map(cell => htmlTag("td")(cell))))
        )
      )
    )
}
