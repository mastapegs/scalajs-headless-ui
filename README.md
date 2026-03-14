# Scala.js Headless UI

A proof-of-concept exploring **headless components** — the idea that a component's
state and logic can (and should) live completely apart from how it looks.

**Live demo:** [scalajs-headless-ui.netlify.app](https://scalajs-headless-ui.netlify.app/)

---

## What Are Headless Components?

Think of a headless component as the *brain* without a *body*. It knows what data
it holds, what actions are possible, and how state changes over time — but it has
absolutely no opinion about pixels, colors, or layout.

This separation gives you a powerful property: **you can swap the entire visual
presentation without touching a single line of business logic.**

If you've worked with libraries like [Headless UI](https://headlessui.com/),
[Radix](https://www.radix-ui.com/), or [React Aria](https://react-spectrum.adobe.com/react-aria/),
you've seen this pattern in the JavaScript world. This project shows how naturally
it translates to [Scala.js](https://www.scala-js.org/) and
[Laminar](https://laminar.dev/), where strong types and reactive signals make the
boundary between logic and presentation even cleaner.

## How It Works

The architecture has two layers. Understanding how they interact is the key
insight of the whole project.

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
a way to increment it. The pattern is consistent across all headless components:
state lives in a private `Var`, the outside world reads it through a `Signal`, and
mutations happen through explicit methods. This is a small but meaningful
discipline — it means you can *always* reason about where state changes come from.

### 2. Theme Layer — Presentation & the `Theme` Trait Contract

A `Theme` trait defines how every headless component gets rendered. Each theme
implementation receives headless components and returns `HtmlElement` trees:

```scala
trait Theme {
  def card(card: Card[HtmlElement, HtmlElement]): HtmlElement
  def counter(counter: Counter): HtmlElement
  def tabs(tabs: Tabs): HtmlElement
  def accordion(accordion: Accordion): HtmlElement
  def toggle(toggle: Toggle): HtmlElement
  def progress(progress: Progress): HtmlElement
  def tagsInput(tagsInput: TagsInput): HtmlElement
  def tooltip(tooltip: Tooltip): HtmlElement

  def dashboardPage(page: DashboardPage): HtmlElement
  def metricsPage(page: MetricsPage): HtmlElement
  def settingsPage(page: SettingsPage): HtmlElement
  // ...and so on for every page
}
```

This trait is the **contract** between logic and presentation. It's also the thing
you need to update whenever you introduce something new. Adding a headless
component without declaring it in `Theme` means no theme can render it. Adding a
page without a corresponding `Theme` method means the router has nothing to
display. The compiler will remind you — `Theme` is the single source of truth for
what the presentation layer is expected to render.

The project ships with **three themes** to prove the point:

| Theme | Styling Approach | External Dependencies |
|-------|------------------|-----------------------|
| **Inline** | CSS-in-Scala via Laminar's DSL (`backgroundColor(...)`, `display.flex`) | None |
| **CoreUI** | [CoreUI](https://coreui.io/) v5.3.1 CSS classes (`cls("card d-flex")`) | CoreUI stylesheet (CDN) |
| **Tailwind** | [Tailwind CSS](https://tailwindcss.com/) v4 utility classes (`cls("bg-white rounded-xl shadow-sm p-6")`) | Tailwind browser script (CDN) |

All three themes read from the exact same headless components. You can switch
between them at runtime using the selector in the top bar and watch the UI
completely transform while the underlying state stays intact. That's the whole
point.

#### Tailwind Theme Design System

The Tailwind theme uses a cohesive design system built from utility-first
principles. Each view object documents its specific techniques via Scaladoc.
Here are the overarching conventions:

- **Color palette** — Indigo as the primary accent (`indigo-500`/`indigo-600`),
  Slate for dark surfaces (top bar, sidebar), and neutral Grays for text and
  borders.
- **Card surfaces** — `bg-white rounded-xl shadow-sm border border-gray-200/60`
  with `hover:shadow-md transition-shadow` for interactive lift on hover.
- **Typography** — Bold headings (`text-2xl font-bold text-gray-900`), muted
  labels (`text-sm font-medium text-gray-500`), and relaxed body text.
- **Button system** — Primary buttons in indigo with hover/active/focus states
  and `focus:ring-2 focus:ring-offset-2` for accessible focus indication.
  Secondary buttons use a white background with a gray border.
- **Transitions** — Smooth `transition-colors`, `transition-shadow`, and
  `transition-all` on interactive elements for a polished, responsive feel.
- **Layout** — Gray-50 main content background creates contrast against white
  cards, a classic dashboard pattern. Gradient sidebar and top bar add depth.

### How Themes Load External Resources

The Inline theme needs nothing beyond what Laminar provides. But CoreUI and
Tailwind each require external CSS or JavaScript from a CDN. Rather than loading
all of them upfront, each theme has `onActivate()` and `onDeactivate()` lifecycle
hooks that inject or remove `<link>` and `<script>` tags dynamically. This keeps
the page clean — only the active theme's resources are present in the document at
any given time.

### Accessibility

The `Theme` trait includes `final` methods — `topbar()`, `sidebar()`, and
`mainContent()` — that wrap each theme's rendered output with ARIA `role` and
`aria-label` attributes. This means accessibility semantics are enforced at the
trait level, not left up to individual theme implementations to remember.

## Extending the Project

The headless architecture makes it straightforward to add new pieces — but every
addition touches multiple layers. The `Theme` trait is the glue that holds it all
together, and keeping it in sync is the one thing you genuinely can't skip.

### Adding a New Component

1. **Create the headless component** in `headless/components/`. Define state with
   `Var`, expose it through `Signal`, and provide methods for mutations.
2. **Add a render method to the `Theme` trait** in `theme/Theme.scala`. This is
   what makes every theme aware that the component exists. Without this step, the
   component is invisible to the presentation layer.
3. **Implement the view in all three themes** — `theme/inline/components/`,
   `theme/coreui/components/`, and `theme/tailwind/components/`. Each theme object
   (`InlineTheme`, `CoreUiTheme`, `TailwindTheme`) must override the new method.
4. **Wire it in.** Compose the component into the appropriate page or layout in
   `App.scala`.
5. **Write tests** in `src/test/scala/com/example/headless/components/`. Test the
   state and behavior — the headless part — not the rendering.

### Adding a New Page

Pages require a few more touch points because they interact with routing,
navigation, and the sealed `Page` trait:

1. **Add a case** to the `Page` sealed trait in `Page.scala` — update `all`,
   `label`, `serialize`, and `deserialize`. Since `Page` is sealed and the
   compiler runs with `-Xfatal-warnings`, a missing case will fail the build.
2. **Create the page state class** in `headless/pages/`.
3. **Add a route** in `AppRouter.scala` — define a route val, add it to the
   router's route list, instantiate the page, and add a match case in
   `pageContent`.
4. **Add a render method to the `Theme` trait.** Use a concrete method with a
   default implementation (e.g., `def myPage(page: MyPage): HtmlElement = div(...)`)
   rather than an abstract one — this avoids issues with Scala.js `fastLinkJS`
   ESModule splitting.
5. **Implement the page view in all three themes** — `theme/inline/pages/`,
   `theme/coreui/pages/`, and `theme/tailwind/pages/`. Override the method in each
   theme object.
6. **Add sidebar icons** in `CoreUiSidebarView.iconFor` and
   `TailwindSidebarView.iconFor`. (The Inline theme doesn't use icons.)
7. **Write tests** in `src/test/scala/com/example/headless/pages/`.

The common thread: **every new component or page must update the `Theme` trait**.
This is by design — the trait is the render contract, and the compiler enforces
it. If you add a headless piece but forget to declare it in `Theme`, none of the
themes can render it. If you add it to `Theme` but forget a theme implementation,
the build fails. The system keeps itself honest.

### Adding a New Theme

1. Create a new package under `theme/` (e.g., `theme/mytheme/`).
2. Implement the `Theme` trait with a unique `key` string.
3. Add `onActivate()` / `onDeactivate()` hooks if external CSS/JS resources are
   needed.
4. Implement views for every component and page — the `Theme` trait will tell you
   exactly what's required.
5. Register the theme in `Theme.all` (in `Theme.scala`) and add it to
   `TopBar.rendererOptions` so it appears in the theme selector.

## Project Structure

```
src/main/scala/com/example/
├── App.scala                    # Entry point, theme switching, main composition
├── AppRouter.scala              # Fragment-based routing (Waypoint)
├── Page.scala                   # Sealed trait: Dashboard | Metrics | Settings | Fetch | UIShowcase
├── headless/
│   ├── components/              # Pure state & logic (no rendering)
│   │   ├── Accordion.scala      # Expandable sections with single/multi mode
│   │   ├── Card.scala           # Generic titled container Card[T, C]
│   │   ├── Counter.scala        # Int state + increment()
│   │   ├── Progress.scala       # Bounded value with percentage computation
│   │   ├── Sidebar.scala        # Collapsed state, current page, navigation
│   │   ├── Tabs.scala           # Tab selection with keyboard navigation
│   │   ├── TagsInput.scala      # Tag list with add/remove/validation
│   │   ├── Toggle.scala         # Boolean on/off switch
│   │   ├── Tooltip.scala        # Hover-driven visibility state
│   │   └── TopBar.scala         # Brand name, renderer selection
│   └── pages/                   # Page-level state containers
│       ├── DashboardPage.scala
│       ├── FetchPage.scala      # Async data fetching with loading/error/success states
│       ├── MetricsPage.scala
│       ├── SettingsPage.scala
│       └── UIShowcasePage.scala  # Composes all headless components as a showcase
└── theme/
    ├── Theme.scala              # Trait defining the render contract
    ├── inline/                  # CSS-in-Scala theme (no external deps)
    │   ├── InlineTheme.scala
    │   ├── components/          # InlineCounterView, InlineSidebarView, ...
    │   └── pages/               # InlineDashboardPageView, ...
    ├── coreui/                  # CoreUI CSS framework theme
    │   ├── CoreUiTheme.scala
    │   ├── components/          # CoreUiCounterView, CoreUiSidebarView, ...
    │   └── pages/               # CoreUiDashboardPageView, ...
    └── tailwind/                # Tailwind CSS utility theme
        ├── TailwindTheme.scala
        ├── components/          # TailwindCounterView, TailwindSidebarView, ...
        └── pages/               # TailwindDashboardPageView, ...
```

## Getting Started

### Prerequisites

- [JDK 17+](https://adoptium.net/) (the CI pipeline uses Temurin 17)
- [sbt](https://www.scala-sbt.org/)

No npm or Node.js required — this is a pure SBT/Scala.js build.

### Run Locally

1. Compile Scala.js to JavaScript:

   ```bash
   sbt fastLinkJS
   ```

2. Serve `index.html` with any static file server:

   ```bash
   npx http-server . -o
   ```

   Then open [http://localhost:8080](http://localhost:8080) in your browser.

3. For continuous development (recompiles on file changes):

   ```bash
   sbt ~fastLinkJS
   ```

### Code Quality

```bash
sbt fmtall    # Format all code (Scalafmt)
sbt fixall    # Lint + format (Scalafix + Scalafmt)
```

## Testing

The project uses [MUnit](https://scalameta.org/munit/) to test all headless
components and page containers — 95 tests across 13 suites. Tests focus purely on
state and behavior. No DOM, no rendering, no browser required.

```bash
sbt test
```

```
src/test/scala/com/example/headless/
├── components/
│   ├── CardSuite.scala         # 4 tests: title, content, independence, type parameters
│   ├── AccordionSuite.scala    # 6 tests: open/close, single/multi mode
│   ├── CounterSuite.scala      # 7 tests: init, custom init, increment, decrement, reset, accumulation
│   ├── ProgressSuite.scala     # 7 tests: value, percentage, bounds, reset
│   ├── SidebarSuite.scala      # 8 tests: collapse toggle, navigation, isActive
│   ├── TabsSuite.scala         # 8 tests: selection, navigation, wrapping
│   ├── TagsInputSuite.scala    # 9 tests: add, remove, duplicates, max tags
│   ├── ToggleSuite.scala       # 5 tests: toggle, setOn, setOff
│   ├── TooltipSuite.scala      # 4 tests: show, hide, text, placement
│   └── TopBarSuite.scala       # 4 tests: brand, renderer options, selection
└── pages/
    ├── FetchPageSuite.scala    # 11 tests: Circe decoding, FetchState, TableData
    ├── PagesSuite.scala        # 12 tests: title/description for all pages
    └── UIShowcasePageSuite.scala  # 10 tests: composition, independent state
```

This is one of the benefits of headless architecture — because state is just data,
testing it is straightforward. No need to mount components, simulate clicks, or
query the DOM. You call a method, read a signal, and assert.

## Tech Stack

| Tool | Version | Role |
|------|---------|------|
| [Scala](https://www.scala-lang.org/) | 2.13.18 | Language |
| [Scala.js](https://www.scala-js.org/) | 1.20.2 | Compiles Scala to JavaScript |
| [Laminar](https://laminar.dev/) | 17.2.1 | Reactive UI library for Scala.js |
| [Waypoint](https://github.com/raquo/Waypoint) | 10.0.0-M1 | Fragment-based URL routing |
| [scalajs-dom](https://github.com/nicklawls/scala-js-dom) | 2.8.1 | Scala.js DOM API bindings |
| [Circe](https://circe.github.io/circe/) | 0.14.15 | JSON encoding and decoding (used in FetchPage) |
| [MUnit](https://scalameta.org/munit/) | 1.1.0 | Testing framework |
| [sbt](https://www.scala-sbt.org/) | 1.12.5 | Build tool |

## Deployment

The project deploys to [Netlify](https://www.netlify.com/) via GitHub Actions:

- **Pull requests** automatically get a preview build — a bot comment posts the
  preview URL so you can see changes before merging.
- **Merges to `main`** trigger a production deploy with fully optimized output
  (`sbt fullLinkJS`) to
  [scalajs-headless-ui.netlify.app](https://scalajs-headless-ui.netlify.app/).

A **CI workflow** runs on every PR and push to `main`, checking:

1. **Formatting** — `sbt scalafmtCheckAll scalafmtSbtCheck`
2. **Linting** — `sbt "scalafixAll --check"`
3. **Tests** — `sbt test`

All checks must pass before merges are allowed.

## Why This Matters

Headless components aren't a new idea, but they're an important one. The core
lesson is about **separation of concerns taken seriously** — not just "put logic
in one file and styles in another," but a real architectural boundary where the
logic layer has *zero knowledge* of how it will be rendered.

This gives you concrete benefits:

- **Testability.** State is just data. Test it without a browser.
- **Reusability.** Write the logic once, render it with any CSS framework — or
  your own hand-rolled styles.
- **Flexibility.** Swap themes at runtime. Try a new design system without
  rewriting your state management.
- **Clarity.** When you read a headless component, you understand its behavior.
  When you read a theme view, you understand its presentation. Neither is
  cluttered with the other's concerns.

If you've ever struggled with tightly coupled UI code — where changing a button's
color somehow requires understanding a state machine — headless components offer a
way out. Separate the *what* from the *how*, and both become easier to reason
about.
