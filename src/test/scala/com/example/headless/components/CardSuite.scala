package com.example.headless.components

import munit.FunSuite

class CardSuite extends FunSuite {

  test("title is stored") {
    val card = new Card("My Card")
    assertEquals(card.title, "My Card")
  }

  test("different cards have independent titles") {
    val card1 = new Card("First")
    val card2 = new Card("Second")
    assertEquals(card1.title, "First")
    assertEquals(card2.title, "Second")
  }

  test("empty title is allowed") {
    val card = new Card("")
    assertEquals(card.title, "")
  }
}
