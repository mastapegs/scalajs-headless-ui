package com.example.headless.pages

import com.raquo.airstream.web.FetchStream
import com.raquo.laminar.api.L._
import io.circe.generic.auto._
import io.circe.parser.decode

/** Represents a single post from the JSONPlaceholder API. */
final case class Post(userId: Int, id: Int, title: String, body: String)

/** ADT representing the three states of an async fetch operation. */
sealed trait FetchState
object FetchState {
  case object Loading                         extends FetchState
  final case class Error(message: String)     extends FetchState
  final case class Success(posts: List[Post]) extends FetchState
}

/** Headless fetch showcase page: manages async data fetching state and logic, no rendering. */
final class FetchPage {

  val title: String = "Fetch Showcase"
  val description: String =
    "Demonstrates fetching and streaming data from the JSONPlaceholder API using headless state management."

  private val stateVar: Var[FetchState] = Var(FetchState.Loading)

  val state: Signal[FetchState] = stateVar.signal

  /** Returns a lazy EventStream that, when started (subscribed to),
    * fetches posts from JSONPlaceholder and updates state accordingly.
    */
  def fetchPosts(): EventStream[FetchState] = {
    stateVar.set(FetchState.Loading)
    FetchStream
      .get("https://jsonplaceholder.typicode.com/posts")
      .map { responseText =>
        decode[List[Post]](responseText) match {
          case Right(posts) =>
            val st = FetchState.Success(posts)
            stateVar.set(st)
            st
          case Left(err) =>
            val st = FetchState.Error(err.getMessage)
            stateVar.set(st)
            st
        }
      }
      .recover { case err: Throwable =>
        val st = FetchState.Error(err.getMessage)
        stateVar.set(st)
        Some(st)
      }
  }

  def setLoading(): Unit                   = stateVar.set(FetchState.Loading)
  def setError(message: String): Unit      = stateVar.set(FetchState.Error(message))
  def setSuccess(posts: List[Post]): Unit  = stateVar.set(FetchState.Success(posts))
}
