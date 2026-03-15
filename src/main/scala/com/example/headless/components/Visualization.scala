package com.example.headless.components

import com.raquo.laminar.api.L._

/** Visualization pattern type. */
sealed trait Pattern
object Pattern {
  case object Waves     extends Pattern
  case object Spiral    extends Pattern
  case object Particles extends Pattern
  case object Fractal   extends Pattern

  val all: List[Pattern] = List(Waves, Spiral, Particles, Fractal)

  def label(p: Pattern): String = p match {
    case Waves     => "Waves"
    case Spiral    => "Spiral"
    case Particles => "Particles"
    case Fractal   => "Fractal"
  }
}

/** Color scheme for visualization rendering. */
sealed trait ColorScheme
object ColorScheme {
  case object Ocean      extends ColorScheme
  case object Sunset     extends ColorScheme
  case object Neon       extends ColorScheme
  case object Monochrome extends ColorScheme

  val all: List[ColorScheme] = List(Ocean, Sunset, Neon, Monochrome)

  def label(cs: ColorScheme): String = cs match {
    case Ocean      => "Ocean"
    case Sunset     => "Sunset"
    case Neon       => "Neon"
    case Monochrome => "Monochrome"
  }
}

/** Headless visualization component: manages generative art parameters as reactive state, no rendering. */
final class Visualization(
    initialPattern: Pattern = Pattern.Waves,
    initialSpeed: Double = 1.0,
    initialColorScheme: ColorScheme = ColorScheme.Ocean,
    initialComplexity: Int = 5,
    initialMouseInfluence: Boolean = false
) {

  private val patternVar: Var[Pattern]         = Var(initialPattern)
  private val speedVar: Var[Double]            = Var(initialSpeed)
  private val colorSchemeVar: Var[ColorScheme] = Var(initialColorScheme)
  private val complexityVar: Var[Int]          = Var(initialComplexity.max(1).min(10))
  private val mouseInfluenceVar: Var[Boolean]  = Var(initialMouseInfluence)

  val pattern: Signal[Pattern]         = patternVar.signal
  val speed: Signal[Double]            = speedVar.signal
  val colorScheme: Signal[ColorScheme] = colorSchemeVar.signal
  val complexity: Signal[Int]          = complexityVar.signal
  val mouseInfluence: Signal[Boolean]  = mouseInfluenceVar.signal

  def setPattern(p: Pattern): Unit          = patternVar.set(p)
  def setSpeed(s: Double): Unit             = speedVar.set(s.max(0.1).min(5.0))
  def setColorScheme(cs: ColorScheme): Unit = colorSchemeVar.set(cs)
  def setComplexity(c: Int): Unit           = complexityVar.set(c.max(1).min(10))
  def toggleMouseInfluence(): Unit          = mouseInfluenceVar.update(!_)
}
