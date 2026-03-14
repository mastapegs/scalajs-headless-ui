package com.example.headless.components

import com.raquo.laminar.api.L._

final class TagsInput(val label: String, initialTags: List[String] = Nil, val maxTags: Int = Int.MaxValue) {

  private val tagsVar: Var[List[String]] = Var(initialTags)
  private val inputVar: Var[String]      = Var("")

  val tags: Signal[List[String]] = tagsVar.signal
  val input: Signal[String]      = inputVar.signal
  val tagCount: Signal[Int]      = tags.map(_.length)
  val canAdd: Signal[Boolean]    = tagCount.map(_ < maxTags)

  def addTag(tag: String): Boolean = {
    val trimmed = tag.trim
    if (trimmed.isEmpty) return false
    val current = tagsVar.now()
    if (current.contains(trimmed)) return false
    if (current.length >= maxTags) return false
    tagsVar.update(_ :+ trimmed)
    inputVar.set("")
    true
  }

  def removeTag(tag: String): Unit =
    tagsVar.update(_.filterNot(_ == tag))

  def removeLastTag(): Unit =
    tagsVar.update(ts => if (ts.isEmpty) ts else ts.init)

  def setInput(value: String): Unit =
    inputVar.set(value)

  def clearAll(): Unit = {
    tagsVar.set(Nil)
    inputVar.set("")
  }
}
