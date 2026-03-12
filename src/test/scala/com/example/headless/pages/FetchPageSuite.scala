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

  test("setError transitions state to Error") {
    val page = new FetchPage()
    page.setError("network failure")
    assertEquals(signalNow(page.state), FetchState.Error("network failure"): FetchState)
  }

  test("setSuccess transitions state to Success") {
    val page  = new FetchPage()
    val posts = List(Post(1, 1, "hello", "world"))
    page.setSuccess(posts)
    assertEquals(signalNow(page.state), FetchState.Success(posts): FetchState)
  }

  test("setLoading resets state back to Loading") {
    val page = new FetchPage()
    page.setError("oops")
    page.setLoading()
    assertEquals(signalNow(page.state), FetchState.Loading: FetchState)
  }

  test("state transitions: Loading -> Success -> Error -> Loading") {
    val page  = new FetchPage()
    val posts = List(Post(1, 1, "t", "b"))

    assertEquals(signalNow(page.state), FetchState.Loading: FetchState)
    page.setSuccess(posts)
    assertEquals(signalNow(page.state), FetchState.Success(posts): FetchState)
    page.setError("fail")
    assertEquals(signalNow(page.state), FetchState.Error("fail"): FetchState)
    page.setLoading()
    assertEquals(signalNow(page.state), FetchState.Loading: FetchState)
  }

  // -- Circe decoding tests: prove the JSON → Post pipeline works --

  test("Circe decodes a single Post from JSON") {
    val json = """{"userId":1,"id":42,"title":"Test Title","body":"Test body content"}"""
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
}
