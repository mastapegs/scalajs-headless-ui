package com.example.headless.components

import com.example.headless.SignalHelpers
import munit.FunSuite

class ModalSuite extends FunSuite with SignalHelpers {

  test("starts closed by default") {
    val modal = new Modal("Test")
    assertEquals(signalNow(modal.isOpen), false)
  }

  test("open sets isOpen to true") {
    val modal = new Modal("Test")
    modal.open()
    assertEquals(signalNow(modal.isOpen), true)
  }

  test("close sets isOpen to false") {
    val modal = new Modal("Test")
    modal.open()
    modal.close()
    assertEquals(signalNow(modal.isOpen), false)
  }

  test("open is idempotent") {
    val modal = new Modal("Test")
    modal.open()
    modal.open()
    assertEquals(signalNow(modal.isOpen), true)
  }

  test("close is idempotent") {
    val modal = new Modal("Test")
    modal.close()
    assertEquals(signalNow(modal.isOpen), false)
  }

  test("title is stored") {
    val modal = new Modal("Confirm Delete")
    assertEquals(modal.title, "Confirm Delete")
  }

  test("requestCloseEscape closes when closeOnEscape is true") {
    val modal = new Modal("Test", closeOnEscape = true)
    modal.open()
    modal.requestCloseEscape()
    assertEquals(signalNow(modal.isOpen), false)
  }

  test("requestCloseEscape does nothing when closeOnEscape is false") {
    val modal = new Modal("Test", closeOnEscape = false)
    modal.open()
    modal.requestCloseEscape()
    assertEquals(signalNow(modal.isOpen), true)
  }

  test("requestCloseOutside closes when closeOnOutsideClick is true") {
    val modal = new Modal("Test", closeOnOutsideClick = true)
    modal.open()
    modal.requestCloseOutside()
    assertEquals(signalNow(modal.isOpen), false)
  }

  test("requestCloseOutside does nothing when closeOnOutsideClick is false") {
    val modal = new Modal("Test", closeOnOutsideClick = false)
    modal.open()
    modal.requestCloseOutside()
    assertEquals(signalNow(modal.isOpen), true)
  }

  test("default policy enables both escape and outside click") {
    val modal = new Modal("Test")
    assertEquals(modal.closeOnEscape, true)
    assertEquals(modal.closeOnOutsideClick, true)
  }
}
