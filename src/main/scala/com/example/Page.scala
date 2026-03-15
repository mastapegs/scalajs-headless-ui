package com.example

sealed trait Page
object Page {
  case object Dashboard      extends Page
  case object Metrics        extends Page
  case object Settings       extends Page
  case object Fetch          extends Page
  case object UIShowcase     extends Page
  case object Visualizations extends Page

  val all: List[Page] = List(Dashboard, Metrics, Settings, Fetch, UIShowcase, Visualizations)

  def label(page: Page): String = page match {
    case Dashboard      => "Dashboard"
    case Metrics        => "Metrics"
    case Settings       => "Settings"
    case Fetch          => "Fetch"
    case UIShowcase     => "UI Showcase"
    case Visualizations => "Visualizations"
  }

  def serialize(page: Page): String = page match {
    case Dashboard      => "dashboard"
    case Metrics        => "metrics"
    case Settings       => "settings"
    case Fetch          => "fetch"
    case UIShowcase     => "ui-showcase"
    case Visualizations => "visualizations"
  }

  def deserialize(s: String): Page = s match {
    case "dashboard"      => Dashboard
    case "metrics"        => Metrics
    case "settings"       => Settings
    case "fetch"          => Fetch
    case "ui-showcase"    => UIShowcase
    case "visualizations" => Visualizations
    case _                => Dashboard
  }
}
