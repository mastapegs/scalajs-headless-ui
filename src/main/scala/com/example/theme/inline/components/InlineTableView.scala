package com.example.theme.inline.components

import com.example.headless.components.Table
import com.raquo.laminar.api.L._

object InlineTableView {
  def render(t: Table): HtmlElement =
    table(
      width("100%"),
      borderCollapse("collapse"),
      thead(
        tr(
          t.headers.map(h =>
            th(
              padding("8px 12px"),
              borderBottom("2px solid #ccc"),
              textAlign.left,
              fontWeight.bold,
              h
            )
          )
        )
      ),
      tbody(
        t.rows.map(row =>
          tr(
            row.map(cell =>
              htmlTag("td")(
                padding("8px 12px"),
                borderBottom("1px solid #eee"),
                cell
              )
            )
          )
        )
      )
    )
}
