package com.example.headless.components

import com.raquo.laminar.api.L._

/** Headless modal dialog component.
  *
  * Combines a generic content container (like `Card[T, C]` / `PageContainer[C]`) with stateful open/close management
  * (like `Toggle`). Themes concretize `C` as `HtmlElement`. All rendering concerns (backdrop, focus trapping, scroll
  * locking, ARIA attributes, animations) belong to the theme layer.
  *
  * @tparam C
  *   the type of the modal body content — themes concretize as `HtmlElement`
  * @param title
  *   dialog title (used by themes for `aria-labelledby`)
  * @param content
  *   the body content rendered inside the dialog
  * @param closeOnEscape
  *   whether pressing Escape should close the modal (theme layer reads this to decide whether to bind the key)
  * @param closeOnOutsideClick
  *   whether clicking the backdrop should close the modal (theme layer reads this to decide whether to bind the click)
  */
class Modal[C](
    val title: String,
    val content: C,
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

  /** Create a new view over this modal with transformed content, sharing the same open/close state.
    *
    * This is useful when a headless page holds `Modal[String]` and a theme view needs `Modal[HtmlElement]` — the view
    * calls `modal.mapContent(text => span(text))` to obtain a presentation-typed modal that delegates all state
    * operations to the original.
    */
  def mapContent[D](f: C => D): Modal[D] = {
    val source = this
    new Modal[D](title, f(content), closeOnEscape, closeOnOutsideClick) {
      override def open(): Unit                = source.open()
      override def close(): Unit               = source.close()
      override def requestCloseEscape(): Unit  = source.requestCloseEscape()
      override def requestCloseOutside(): Unit = source.requestCloseOutside()
      override val isOpen: Signal[Boolean]     = source.isOpen
    }
  }
}
