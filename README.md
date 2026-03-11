# Scala.js Headless UI

A proof-of-concept exploring **headless components** — the idea that a component's
state and logic can (and should) live completely apart from how it looks.

**Live site:** [scalajs-headless-ui.netlify.app](https://scalajs-headless-ui.netlify.app/)

## What Are Headless Components?

Think of a headless component as the *brain* without a *body*. It knows what data
it holds, what actions are possible, and how state changes over time — but it has
absolutely no opinion about pixels, colors, or layout.

This separation gives you a powerful property: **you can swap the entire visual
presentation without touching a single line of business logic.**

This project demonstrates that idea with a small dashboard app built in
[Scala.js](https://www.scala-js.org/) and [Laminar](https://laminar.dev/).

## How It Works

The architecture has two layers:

### 1. Headless Layer — State & Logic

Each component is a plain Scala class that manages its own reactive state using
Laminar's `Var` and `Signal` primitives. No HTML, no CSS, no DOM — just data and
behavior.

```scala
// headless/components/Counter.scala
final class Counter(initialValue: Int = 0) {

  private val countVar: Var[Int] = Var(initialValue)

  val count: Signal[Int] = countVar.signal

  def increment(): Unit =
    countVar.update(_ + 1)
}
```

Notice there is nothing visual here. The `Counter` only knows it has a number and
a way to increment it.

### 2. Theme Layer — Presentation

A `Theme` trait defines how every headless component gets rendered. Each theme
implementation receives headless components and returns `HtmlElement` trees:

```scala
trait Theme {
  def counter(counter: Counter): HtmlElement
  def sidebar(sidebar: Sidebar): HtmlElement
  def topbar(topBar: TopBar): HtmlElement
  // ...
}
```

The project ships with two themes to prove the point:

- **Inline Theme** — styles everything with Laminar's CSS-in-Scala DSL, no
  external framework needed.
- **CoreUI Theme** — renders the same components using
  [CoreUI](https://coreui.io/) CSS classes.

Both themes read from the exact same headless components. You can switch between
them at runtime and watch the UI completely transform while the underlying state
stays intact.

## Project Structure

```
src/main/scala/com/example/
├── App.scala                    # Entry point, theme switching
├── AppRouter.scala              # Fragment-based routing (Waypoint)
├── Page.scala                   # Page definitions
├── headless/
│   ├── components/              # Headless state & logic
│   │   ├── Counter.scala
│   │   ├── Sidebar.scala
│   │   └── TopBar.scala
│   └── pages/                   # Page-level state
│       ├── DashboardPage.scala
│       ├── MetricsPage.scala
│       └── SettingsPage.scala
└── theme/
    ├── Theme.scala              # Theme trait (the contract)
    ├── inline/                  # Pure CSS-in-Scala theme
    └── coreui/                  # CoreUI CSS framework theme
```

## Getting Started

### Prerequisites

- [JDK 11+](https://adoptium.net/)
- [sbt](https://www.scala-sbt.org/)

### Run Locally

1. Compile Scala.js to JavaScript:

   ```bash
   sbt fastLinkJS
   ```

2. Serve `index.html` with any static file server. For example:

   ```bash
   npx http-server . -o
   ```

   Then open [http://localhost:8080](http://localhost:8080) in your browser.

## Tech Stack

| Tool | Role |
|------|------|
| [Scala.js](https://www.scala-js.org/) | Compiles Scala to JavaScript |
| [Laminar](https://laminar.dev/) | Reactive UI library for Scala.js |
| [Waypoint](https://github.com/raquo/Waypoint) | URL routing |
| [sbt](https://www.scala-sbt.org/) | Build tool |

## Deployment

The project deploys to [Netlify](https://www.netlify.com/):

- **Pull requests** automatically get a preview build so you can see changes
  before merging.
- **Merges to `main`** trigger a production deploy to
  [scalajs-headless-ui.netlify.app](https://scalajs-headless-ui.netlify.app/).

## Why This Matters

Headless components aren't a new idea — libraries like Headless UI, Radix, and
React Aria have popularized the pattern in the JavaScript ecosystem. But the
concept translates beautifully to Scala.js and Laminar, where strong types and
reactive signals make the boundary between logic and presentation even cleaner.

If you've ever struggled with tightly coupled UI code — where changing a button's
color somehow requires understanding a state machine — headless components offer a
way out. Separate the *what* from the *how*, and both become easier to reason about.
