package com.example.headless.components

import munit.FunSuite

class CardSuite extends FunSuite {

  test("stores title") {
    val card = Card("My Card", "content")
    assertEquals(card.title, "My Card")
  }

  test("stores content") {
    val card = Card("Title", "body")
    assertEquals(card.content, "body")
  }

  test("different cards have independent values") {
    val card1 = Card("First", "a")
    val card2 = Card("Second", "b")
    assertEquals(card1.title, "First")
    assertEquals(card2.title, "Second")
    assertNotEquals(card1, card2)
  }

  test("supports different type parameters") {
    val strCard = Card("title", "content")
    val intCard = Card(1, 2)
    assertEquals(strCard.title, "title")
    assertEquals(intCard.title, 1)
  }
}
