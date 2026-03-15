package com.example.headless.pages

import com.example.headless.components.{Table, TableData}
import com.raquo.airstream.web.FetchStream
import com.raquo.laminar.api.L._
import io.circe.generic.auto._
import io.circe.parser.decode

/** Represents a single post from the JSONPlaceholder API. */
final case class Post(userId: Int, id: Int, title: String, body: String)

/** ADT representing the three states of an async fetch operation. */
sealed trait FetchState
object FetchState {
  case object Loading                                       extends FetchState
  final case class Error(message: String)                   extends FetchState
  final case class Success(posts: List[Post], table: Table) extends FetchState
}

/** Headless fetch showcase page: manages async data fetching state and logic, no rendering. */
final class FetchPage {

  val title: String = "Fetch Showcase"
  val description: String =
    "Demonstrates fetching and streaming data from the JSONPlaceholder API using headless state management."

  private val stateVar: Var[FetchState] = Var(FetchState.Loading)

  val state: Signal[FetchState] = stateVar.signal

  /** Fetches posts from JSONPlaceholder and updates state. Returns a fire-and-forget EventStream — themes should bind
    * it (e.g. `fetchPosts() --> Observer.empty`) and observe `state` for results.
    */
  def fetchPosts(): EventStream[Unit] = {
    stateVar.set(FetchState.Loading)
    FetchStream
      .get("https://jsonplaceholder.typicode.com/posts")
      .map { responseText =>
        decode[List[Post]](responseText) match {
          case Right(posts) =>
            val td = TableData(
              headers = List("ID", "User ID", "Title", "Body"),
              rows = posts.map(p => List(p.id.toString, p.userId.toString, p.title, p.body))
            )
            stateVar.set(FetchState.Success(posts, Table(td)))
          case Left(err) => stateVar.set(FetchState.Error(err.getMessage))
        }
      }
      .recover { case err: Throwable =>
        stateVar.set(FetchState.Error(err.getMessage))
        Some(())
      }
  }
}
