package com.example.headless.components

import com.example.headless.SignalHelpers
import munit.FunSuite

class VisualizationSuite extends FunSuite with SignalHelpers {

  test("default pattern is Waves") {
    val viz = new Visualization()
    assertEquals(signalNow(viz.pattern), Pattern.Waves)
  }

  test("setPattern changes pattern") {
    val viz = new Visualization()
    viz.setPattern(Pattern.Spiral)
    assertEquals(signalNow(viz.pattern), Pattern.Spiral)
    viz.setPattern(Pattern.Fractal)
    assertEquals(signalNow(viz.pattern), Pattern.Fractal)
  }

  test("default speed is 1.0") {
    val viz = new Visualization()
    assertEquals(signalNow(viz.speed), 1.0)
  }

  test("setSpeed updates speed") {
    val viz = new Visualization()
    viz.setSpeed(2.5)
    assertEquals(signalNow(viz.speed), 2.5)
  }

  test("setSpeed clamps to minimum 0.1") {
    val viz = new Visualization()
    viz.setSpeed(0.0)
    assertEquals(signalNow(viz.speed), 0.1)
    viz.setSpeed(-5.0)
    assertEquals(signalNow(viz.speed), 0.1)
  }

  test("setSpeed clamps to maximum 5.0") {
    val viz = new Visualization()
    viz.setSpeed(10.0)
    assertEquals(signalNow(viz.speed), 5.0)
  }

  test("default colorScheme is Ocean") {
    val viz = new Visualization()
    assertEquals(signalNow(viz.colorScheme), ColorScheme.Ocean)
  }

  test("setColorScheme changes color scheme") {
    val viz = new Visualization()
    viz.setColorScheme(ColorScheme.Neon)
    assertEquals(signalNow(viz.colorScheme), ColorScheme.Neon)
  }

  test("default complexity is 5") {
    val viz = new Visualization()
    assertEquals(signalNow(viz.complexity), 5)
  }

  test("setComplexity clamps to range 1-10") {
    val viz = new Visualization()
    viz.setComplexity(0)
    assertEquals(signalNow(viz.complexity), 1)
    viz.setComplexity(15)
    assertEquals(signalNow(viz.complexity), 10)
  }

  test("constructor clamps initial complexity") {
    val viz = new Visualization(initialComplexity = 20)
    assertEquals(signalNow(viz.complexity), 10)
    val viz2 = new Visualization(initialComplexity = -3)
    assertEquals(signalNow(viz2.complexity), 1)
  }

  test("default mouseInfluence is false") {
    val viz = new Visualization()
    assertEquals(signalNow(viz.mouseInfluence), false)
  }

  test("toggleMouseInfluence flips state") {
    val viz = new Visualization()
    viz.toggleMouseInfluence()
    assertEquals(signalNow(viz.mouseInfluence), true)
    viz.toggleMouseInfluence()
    assertEquals(signalNow(viz.mouseInfluence), false)
  }

  test("Pattern.all contains all four patterns") {
    assertEquals(Pattern.all.length, 4)
    assert(Pattern.all.contains(Pattern.Waves))
    assert(Pattern.all.contains(Pattern.Spiral))
    assert(Pattern.all.contains(Pattern.Particles))
    assert(Pattern.all.contains(Pattern.Fractal))
  }

  test("Pattern.label returns human-readable names") {
    assertEquals(Pattern.label(Pattern.Waves), "Waves")
    assertEquals(Pattern.label(Pattern.Spiral), "Spiral")
    assertEquals(Pattern.label(Pattern.Particles), "Particles")
    assertEquals(Pattern.label(Pattern.Fractal), "Fractal")
  }

  test("ColorScheme.all contains all four schemes") {
    assertEquals(ColorScheme.all.length, 4)
    assert(ColorScheme.all.contains(ColorScheme.Ocean))
    assert(ColorScheme.all.contains(ColorScheme.Sunset))
    assert(ColorScheme.all.contains(ColorScheme.Neon))
    assert(ColorScheme.all.contains(ColorScheme.Monochrome))
  }

  test("ColorScheme.label returns human-readable names") {
    assertEquals(ColorScheme.label(ColorScheme.Ocean), "Ocean")
    assertEquals(ColorScheme.label(ColorScheme.Sunset), "Sunset")
    assertEquals(ColorScheme.label(ColorScheme.Neon), "Neon")
    assertEquals(ColorScheme.label(ColorScheme.Monochrome), "Monochrome")
  }

  test("custom initial values are respected") {
    val viz = new Visualization(
      initialPattern = Pattern.Particles,
      initialSpeed = 3.0,
      initialColorScheme = ColorScheme.Sunset,
      initialComplexity = 8,
      initialMouseInfluence = true
    )
    assertEquals(signalNow(viz.pattern), Pattern.Particles)
    assertEquals(signalNow(viz.speed), 3.0)
    assertEquals(signalNow(viz.colorScheme), ColorScheme.Sunset)
    assertEquals(signalNow(viz.complexity), 8)
    assertEquals(signalNow(viz.mouseInfluence), true)
  }
}
