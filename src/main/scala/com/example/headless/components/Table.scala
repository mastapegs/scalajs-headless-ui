package com.example.headless.components

/** Table-ready representation of data: column headers and string rows. */
final case class TableData(headers: List[String], rows: List[List[String]])

/** Headless table component: a typed container for tabular data, no rendering. */
final case class Table(data: TableData)
