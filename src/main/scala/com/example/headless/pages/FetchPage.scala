package com.example.headless.pages

import com.raquo.laminar.api.L._

/** Represents a single post from the JSONPlaceholder API. */
final case class Post(userId: Int, id: Int, title: String, body: String)

/** ADT representing the three states of an async fetch operation. */
sealed trait FetchState
object FetchState {
  case object Loading                        extends FetchState
  final case class Error(message: String)    extends FetchState
  final case class Success(posts: List[Post]) extends FetchState
}

/** Headless fetch showcase page: manages async data fetching state and logic, no rendering. */
final class FetchPage {

  val title: String = "Fetch Showcase"
  val description: String =
    "Demonstrates fetching and streaming data from the JSONPlaceholder API using headless state management."

  private val stateVar: Var[FetchState] = Var(FetchState.Loading)

  val state: Signal[FetchState] = stateVar.signal

  def setLoading(): Unit =
    stateVar.set(FetchState.Loading)

  def setError(message: String): Unit =
    stateVar.set(FetchState.Error(message))

  def setSuccess(posts: List[Post]): Unit =
    stateVar.set(FetchState.Success(posts))
}
