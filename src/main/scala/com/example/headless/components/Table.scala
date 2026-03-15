package com.example.headless.components

/** Headless table component: optional caption, column headers, and string rows, no rendering. */
final case class Table(caption: Option[String], headers: List[String], rows: List[List[String]])
