package com.example.headless.components

/** ADT representing the three states of an async fetch operation. */
sealed trait FetchState[+T]
object FetchState {
  case object Loading                     extends FetchState[Nothing]
  final case class Error(message: String) extends FetchState[Nothing]
  final case class Success[+T](data: T)   extends FetchState[T]
}
