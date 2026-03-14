package com.example.headless.components

import com.raquo.laminar.api.L._
import munit.FunSuite

class CardSuite extends FunSuite {

  test("stores title element") {
    val title = span("My Card")
    val card  = Card(title, div())
    assert(card.title eq title)
  }

  test("stores content element") {
    val content = div("body")
    val card    = Card(span("Title"), content)
    assert(card.content eq content)
  }

  test("different cards have independent elements") {
    val card1 = Card(span("First"), div("a"))
    val card2 = Card(span("Second"), div("b"))
    assert(!(card1.title eq card2.title))
    assert(!(card1.content eq card2.content))
  }
}
