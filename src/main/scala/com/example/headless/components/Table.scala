package com.example.headless.components

/** Headless table component: column headers and string rows, no rendering. */
final case class Table(headers: List[String], rows: List[List[String]])
