package com.example.headless.components

import munit.FunSuite

class CardSuite extends FunSuite {

  test("title is stored") {
    val card = Card("My Card")
    assertEquals(card.title, "My Card")
  }

  test("different cards have independent titles") {
    val card1 = Card("First")
    val card2 = Card("Second")
    assertEquals(card1.title, "First")
    assertEquals(card2.title, "Second")
  }

  test("empty title is allowed") {
    val card = Card("")
    assertEquals(card.title, "")
  }
}
