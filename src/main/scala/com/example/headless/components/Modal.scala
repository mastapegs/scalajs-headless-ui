package com.example.headless.components

import com.raquo.laminar.api.L._

/** Headless modal dialog component.
  *
  * Manages open/close state and policy flags. All rendering concerns (backdrop, focus trapping, scroll locking, ARIA
  * attributes, animations) belong to the theme layer.
  *
  * @param title
  *   dialog title (used by themes for `aria-labelledby`)
  * @param closeOnEscape
  *   whether pressing Escape should close the modal (theme layer reads this to decide whether to bind the key)
  * @param closeOnOutsideClick
  *   whether clicking the backdrop should close the modal (theme layer reads this to decide whether to bind the click)
  */
final class Modal(
    val title: String,
    val closeOnEscape: Boolean = true,
    val closeOnOutsideClick: Boolean = true
) {

  private val openVar: Var[Boolean] = Var(false)

  /** Read-only signal indicating whether the modal is currently open. */
  val isOpen: Signal[Boolean] = openVar.signal

  /** Open the modal. No-op if already open. */
  def open(): Unit = openVar.set(true)

  /** Close the modal. No-op if already closed. */
  def close(): Unit = openVar.set(false)

  /** Request to close from an Escape key press. Respects `closeOnEscape` policy. */
  def requestCloseEscape(): Unit =
    if (closeOnEscape) close()

  /** Request to close from a backdrop/outside click. Respects `closeOnOutsideClick` policy. */
  def requestCloseOutside(): Unit =
    if (closeOnOutsideClick) close()
}
