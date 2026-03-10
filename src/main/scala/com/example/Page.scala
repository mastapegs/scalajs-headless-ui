package com.example

sealed trait Page
object Page {
  case object Dashboard extends Page
  case object Metrics   extends Page
  case object Settings  extends Page

  val all: List[Page] = List(Dashboard, Metrics, Settings)

  def label(page: Page): String = page match {
    case Dashboard => "Dashboard"
    case Metrics   => "Metrics"
    case Settings  => "Settings"
  }

  def serialize(page: Page): String = page match {
    case Dashboard => "dashboard"
    case Metrics   => "metrics"
    case Settings  => "settings"
  }

  def deserialize(s: String): Page = s match {
    case "dashboard" => Dashboard
    case "metrics"   => Metrics
    case "settings"  => Settings
    case _           => Dashboard
  }
}
