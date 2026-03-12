package com.example.headless.pages

import com.raquo.airstream.core.Signal
import com.raquo.airstream.ownership.ManualOwner
import io.circe.generic.auto._
import io.circe.parser.decode
import munit.FunSuite

class FetchPageSuite extends FunSuite {

  private def signalNow[A](signal: Signal[A]): A = {
    val owner = new ManualOwner
    var value = Option.empty[A]
    signal.foreach(v => value = Some(v))(owner)
    owner.killSubscriptions()
    value.get
  }

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
    assertEquals(signalNow(page.state), FetchState.Loading: FetchState)
  }

  // -- Circe decoding tests: prove the JSON → Post pipeline works --

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

  // -- TableData tests --

  test("TableData.fromPosts transforms posts correctly") {
    val posts = List(Post(1, 10, "Hello", "World"), Post(2, 20, "Foo", "Bar"))
    val td    = TableData.fromPosts(posts)
    assertEquals(td.headers, List("ID", "User ID", "Title", "Body"))
    assertEquals(td.rows.length, 2)
    assertEquals(td.rows.head, List("10", "1", "Hello", "World"))
    assertEquals(td.rows(1), List("20", "2", "Foo", "Bar"))
  }

  test("Success state includes tableData with correct headers") {
    val posts   = List(Post(1, 1, "t", "b"))
    val success = FetchState.Success(posts, TableData.fromPosts(posts))
    assertEquals(success.tableData.headers, List("ID", "User ID", "Title", "Body"))
  }

  test("Success state includes tableData with correct rows") {
    val posts   = List(Post(3, 42, "title", "body"))
    val success = FetchState.Success(posts, TableData.fromPosts(posts))
    assertEquals(success.tableData.rows, List(List("42", "3", "title", "body")))
  }
}
