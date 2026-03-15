package com.example.headless.components

import com.example.headless.SignalHelpers
import munit.FunSuite

class ModalSuite extends FunSuite with SignalHelpers {

  test("starts closed by default") {
    val modal = new Modal("Test", "body")
    assertEquals(signalNow(modal.isOpen), false)
  }

  test("open sets isOpen to true") {
    val modal = new Modal("Test", "body")
    modal.open()
    assertEquals(signalNow(modal.isOpen), true)
  }

  test("close sets isOpen to false") {
    val modal = new Modal("Test", "body")
    modal.open()
    modal.close()
    assertEquals(signalNow(modal.isOpen), false)
  }

  test("open is idempotent") {
    val modal = new Modal("Test", "body")
    modal.open()
    modal.open()
    assertEquals(signalNow(modal.isOpen), true)
  }

  test("close is idempotent") {
    val modal = new Modal("Test", "body")
    modal.close()
    assertEquals(signalNow(modal.isOpen), false)
  }

  test("title is stored") {
    val modal = new Modal("Confirm Delete", "Are you sure?")
    assertEquals(modal.title, "Confirm Delete")
  }

  test("content is stored") {
    val modal = new Modal("Test", "Some body content")
    assertEquals(modal.content, "Some body content")
  }

  test("generic content type is preserved") {
    val modal = new Modal[List[Int]]("Test", List(1, 2, 3))
    assertEquals(modal.content, List(1, 2, 3))
  }

  test("requestCloseEscape closes when closeOnEscape is true") {
    val modal = new Modal("Test", "body", closeOnEscape = true)
    modal.open()
    modal.requestCloseEscape()
    assertEquals(signalNow(modal.isOpen), false)
  }

  test("requestCloseEscape does nothing when closeOnEscape is false") {
    val modal = new Modal("Test", "body", closeOnEscape = false)
    modal.open()
    modal.requestCloseEscape()
    assertEquals(signalNow(modal.isOpen), true)
  }

  test("requestCloseOutside closes when closeOnOutsideClick is true") {
    val modal = new Modal("Test", "body", closeOnOutsideClick = true)
    modal.open()
    modal.requestCloseOutside()
    assertEquals(signalNow(modal.isOpen), false)
  }

  test("requestCloseOutside does nothing when closeOnOutsideClick is false") {
    val modal = new Modal("Test", "body", closeOnOutsideClick = false)
    modal.open()
    modal.requestCloseOutside()
    assertEquals(signalNow(modal.isOpen), true)
  }

  test("default policy enables both escape and outside click") {
    val modal = new Modal("Test", "body")
    assertEquals(modal.closeOnEscape, true)
    assertEquals(modal.closeOnOutsideClick, true)
  }
}
