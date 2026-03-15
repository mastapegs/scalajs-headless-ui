package com.example.headless.pages

import com.example.headless.SignalHelpers
import com.example.headless.components.Table
import io.circe.generic.auto._
import io.circe.parser.decode
import munit.FunSuite

class FetchPageSuite extends FunSuite with SignalHelpers {

  private def tableFromPosts(posts: List[Post]): Table = Table(
    caption = Some("Posts"),
    headers = List("ID", "User ID", "Title", "Body"),
    rows = posts.take(10).map(p => List(p.id.toString, p.userId.toString, p.title, p.body))
  )

  test("FetchPage has correct title") {
    val page = new FetchPage()
    assertEquals(page.title, "Fetch Showcase")
  }

  test("FetchPage has correct description") {
    val page = new FetchPage()
    assert(page.description.contains("JSONPlaceholder"))
  }

  test("initial state is Loading") {
    val page = new FetchPage()
    assertEquals(signalNow(page.state), FetchState.Loading: FetchState[List[Table]])
  }

  // -- Circe decoding tests: prove the JSON → case class pipelines work --

  test("Circe decodes a single Post from JSON") {
    val json   = """{"userId":1,"id":42,"title":"Test Title","body":"Test body content"}"""
    val result = decode[Post](json)
    assertEquals(result, Right(Post(1, 42, "Test Title", "Test body content")))
  }

  test("Circe decodes a list of Posts from JSON") {
    val json =
      """[
        |  {"userId":1,"id":1,"title":"first","body":"body1"},
        |  {"userId":2,"id":2,"title":"second","body":"body2"}
        |]""".stripMargin
    val result = decode[List[Post]](json)
    assertEquals(result, Right(List(Post(1, 1, "first", "body1"), Post(2, 2, "second", "body2"))))
  }

  test("Circe decodes a single User from JSON") {
    val json   = """{"id":1,"name":"Leanne Graham","username":"Bret","email":"leanne@example.com"}"""
    val result = decode[User](json)
    assertEquals(result, Right(User(1, "Leanne Graham", "Bret", "leanne@example.com")))
  }

  test("Circe decodes a single Todo from JSON") {
    val json   = """{"userId":1,"id":1,"title":"delectus aut autem","completed":false}"""
    val result = decode[Todo](json)
    assertEquals(result, Right(Todo(1, 1, "delectus aut autem", false)))
  }

  test("Circe returns error for malformed JSON") {
    val json   = """{"not":"a post"}"""
    val result = decode[Post](json)
    assert(result.isLeft)
  }

  test("Circe returns error for invalid JSON syntax") {
    val json   = """not json at all"""
    val result = decode[Post](json)
    assert(result.isLeft)
  }

  test("Circe decodes empty list") {
    val json   = "[]"
    val result = decode[List[Post]](json)
    assertEquals(result, Right(List.empty[Post]))
  }

  // -- FetchState tests --

  test("FetchState.Success holds generic data") {
    val data: FetchState[String] = FetchState.Success("hello")
    data match {
      case FetchState.Success(s) => assertEquals(s, "hello")
      case _                     => fail("expected Success")
    }
  }

  test("FetchState.Loading is a subtype of any FetchState") {
    val s: FetchState[Int] = FetchState.Loading
    assertEquals(s, FetchState.Loading)
  }

  test("FetchState.Error is a subtype of any FetchState") {
    val s: FetchState[Int] = FetchState.Error("boom")
    s match {
      case FetchState.Error(msg) => assertEquals(msg, "boom")
      case _                     => fail("expected Error")
    }
  }

  // -- Table tests --

  test("tableFromPosts transforms posts correctly") {
    val posts = List(Post(1, 10, "Hello", "World"), Post(2, 20, "Foo", "Bar"))
    val t     = tableFromPosts(posts)
    assertEquals(t.headers, List("ID", "User ID", "Title", "Body"))
    assertEquals(t.rows.length, 2)
    assertEquals(t.rows.head, List("10", "1", "Hello", "World"))
    assertEquals(t.rows(1), List("20", "2", "Foo", "Bar"))
  }

  test("Success state includes table with correct headers") {
    val posts   = List(Post(1, 1, "t", "b"))
    val success = FetchState.Success(tableFromPosts(posts))
    assertEquals(success.data.headers, List("ID", "User ID", "Title", "Body"))
  }

  test("Success state includes table with correct rows") {
    val posts   = List(Post(3, 42, "title", "body"))
    val success = FetchState.Success(tableFromPosts(posts))
    assertEquals(success.data.rows, List(List("42", "3", "title", "body")))
  }
}
