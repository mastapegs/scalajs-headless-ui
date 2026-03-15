package com.example.headless.pages

import com.example.headless.components.{FetchEndpoint, FetchState, Table}
import com.raquo.laminar.api.L._
import io.circe.generic.auto._

/** Represents a single post from the JSONPlaceholder API. */
final case class Post(userId: Int, id: Int, title: String, body: String)

/** Represents a single user from the JSONPlaceholder API. */
final case class User(id: Int, name: String, username: String, email: String)

/** Represents a single todo from the JSONPlaceholder API. */
final case class Todo(userId: Int, id: Int, title: String, completed: Boolean)

/** Headless fetch showcase page: manages async data fetching state and logic, no rendering. */
final class FetchPage {

  val title: String = "Fetch Showcase"
  val description: String =
    "Demonstrates fetching and streaming data from the JSONPlaceholder API using headless state management."

  private val postsState: Var[FetchState[Table]] = Var(FetchState.Loading)
  private val usersState: Var[FetchState[Table]] = Var(FetchState.Loading)
  private val todosState: Var[FetchState[Table]] = Var(FetchState.Loading)

  val state: Signal[FetchState[List[Table]]] =
    postsState.signal
      .combineWith(usersState.signal, todosState.signal)
      .map { case (s1, s2, s3) =>
        val all    = List(s1, s2, s3)
        val errors = all.collect { case FetchState.Error(msg) => msg }
        if (errors.nonEmpty) FetchState.Error(errors.mkString("; "))
        else if (all.contains(FetchState.Loading)) FetchState.Loading
        else FetchState.Success(all.collect { case FetchState.Success(t) => t })
      }

  /** Fetches data from three JSONPlaceholder endpoints and updates state. Returns a fire-and-forget EventStream —
    * themes should bind it (e.g. `fetchPosts() --> Observer.empty`) and observe `state` for results.
    */
  def fetchPosts(): EventStream[Unit] = {
    postsState.set(FetchState.Loading)
    usersState.set(FetchState.Loading)
    todosState.set(FetchState.Loading)
    EventStream.merge(
      FetchEndpoint.fetch[Post](
        "https://jsonplaceholder.typicode.com/posts",
        postsState,
        "Posts",
        List("ID", "User ID", "Title", "Body"),
        p => List(p.id.toString, p.userId.toString, p.title, p.body)
      ),
      FetchEndpoint.fetch[User](
        "https://jsonplaceholder.typicode.com/users",
        usersState,
        "Users",
        List("ID", "Name", "Username", "Email"),
        u => List(u.id.toString, u.name, u.username, u.email)
      ),
      FetchEndpoint.fetch[Todo](
        "https://jsonplaceholder.typicode.com/todos",
        todosState,
        "Todos",
        List("ID", "User ID", "Title", "Completed"),
        t => List(t.id.toString, t.userId.toString, t.title, t.completed.toString)
      )
    )
  }
}
