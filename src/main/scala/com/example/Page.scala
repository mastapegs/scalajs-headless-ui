package com.example

sealed trait Page
object Page {
  case object Dashboard extends Page
  case object Metrics   extends Page
  case object Settings  extends Page
  case object Fetch     extends Page

  val all: List[Page] = List(Dashboard, Metrics, Settings, Fetch)

  def label(page: Page): String = page match {
    case Dashboard => "Dashboard"
    case Metrics   => "Metrics"
    case Settings  => "Settings"
    case Fetch     => "Fetch"
  }

  def serialize(page: Page): String = page match {
    case Dashboard => "dashboard"
    case Metrics   => "metrics"
    case Settings  => "settings"
    case Fetch     => "fetch"
  }

  def deserialize(s: String): Page = s match {
    case "dashboard" => Dashboard
    case "metrics"   => Metrics
    case "settings"  => Settings
    case "fetch"     => Fetch
    case _           => Dashboard
  }
}
