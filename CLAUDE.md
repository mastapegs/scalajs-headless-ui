# CLAUDE.md

## Project Overview

Scala.js proof-of-concept demonstrating the **headless UI pattern** — business logic and state management are fully separated from rendering/presentation. Two interchangeable themes (Inline CSS-in-Scala and CoreUI) render identical logic with completely different visual styles.

**Live site:** scalajs-headless-ui.netlify.app

## Tech Stack

- **Language:** Scala 2.13.18, compiled to JavaScript via Scala.js 1.20.2
- **UI Framework:** [Laminar](https://laminar.dev/) 17.2.1 (reactive DOM library)
- **Routing:** Waypoint 10.0.0-M1 (fragment-based URL routing)
- **DOM:** scalajs-dom 2.8.1
- **Build Tool:** SBT 1.12.5
- **Module Output:** ESModules with small module splitting

## Build & Development Commands

```bash
# Compile Scala to JS (fast, development mode)
sbt fastLinkJS

# Full optimization build
sbt fullLinkJS

# Format all code
sbt fmtall          # runs scalafmtAll + scalafmtSbt

# Lint and format all code
sbt fixall           # runs scalafixAll + fmtall

# Run tests
sbt test

# Check formatting (CI uses this)
sbt scalafmtCheckAll scalafmtSbtCheck

# Check linting (CI uses this)
sbt "scalafixAll --check"

# Continuous compilation (watches for changes)
sbt ~fastLinkJS
```

**Build output:** `target/scala-2.13/ui-template-sandbox-fastopt/main.js`

## Testing

- **Framework:** [MUnit](https://scalameta.org/munit/) 1.1.0 (Scala.js compatible)
- **Run tests:** `sbt test`
- **Test location:** `src/test/scala/com/example/headless/`
- **Coverage:** All headless components (`Counter`, `Sidebar`, `TopBar`) and page containers (`DashboardPage`, `MetricsPage`, `SettingsPage`)
- Tests focus on **state and behavior only** — no DOM or rendering tests

```
src/test/scala/com/example/headless/
├── components/
│   ├── CounterSuite.scala
│   ├── SidebarSuite.scala
│   └── TopBarSuite.scala
└── pages/
    └── PagesSuite.scala
```

## Project Structure

```
src/main/scala/com/example/
├── App.scala              # Entry point, theme switching, main composition
├── AppRouter.scala        # Fragment-based URL routing (Waypoint)
├── Page.scala             # Sealed trait: Dashboard | Metrics | Settings
├── headless/
│   ├── components/        # Pure state/logic (no rendering)
│   │   ├── Counter.scala  # Int state + increment()
│   │   ├── Sidebar.scala  # Collapsed state, current page, navigation
│   │   └── TopBar.scala   # Brand name, renderer selection
│   └── pages/             # Page-level state containers
│       ├── DashboardPage.scala
│       ├── MetricsPage.scala
│       └── SettingsPage.scala
└── theme/
    ├── Theme.scala        # Trait defining render contract for all components
    ├── inline/            # CSS-in-Scala theme (no external deps)
    │   ├── InlineTheme.scala
    │   ├── components/    # InlineCounterView, InlineSidebarView, InlineTopbarView
    │   └── pages/         # InlineDashboardPageView, etc.
    └── coreui/            # CoreUI CSS framework theme
        ├── CoreUiTheme.scala
        ├── components/    # CoreUiCounterView, CoreUiSidebarView, CoreUiTopbarView
        └── pages/         # CoreUiDashboardPageView, etc.
```

## Architecture & Key Patterns

### Headless Components
- **Immutable final classes** with constructor-injected dependencies
- State held in `Var[T]` (mutable reactive cell), exposed as read-only `Signal[T]`
- No DOM or rendering logic — purely state and behavior
- Example: `Counter` owns `Var[Int]`, exposes `count: Signal[Int]` and `increment()` method

### Theme Layer
- `Theme` trait defines a method per component/page returning `HtmlElement`
- Each theme object implements the full trait with its own styling approach
- Themes are swappable at runtime without touching business logic

### Reactive Patterns (Laminar)
- `Var[T]` — mutable observable state
- `Signal[T]` — read-only derived reactive values
- `child <-- signal` — subscribe DOM to signal changes
- `onClick --> callback` — event handling
- `.signal` converts `Var` to `Signal`; `.map()` transforms signals

### Naming Conventions
- Headless components: simple names (`Counter`, `Sidebar`, `TopBar`)
- Theme views: prefixed by theme name (`InlineSidebarView`, `CoreUiCounterView`)
- Pages: suffixed with `Page` (`DashboardPage`, `MetricsPage`)
- Package structure mirrors the headless/theme separation

## Code Style

- **Scalafmt** v3.8.6 with `maxColumn = 120`, `align.preset = more`
- **Scalafix** rules: `OrganizeImports` (grouped, merged)
- **Scalafmt rewrites:** `SortImports`, `RedundantBraces`, `RedundantParens`, `SortModifiers`
- **Dialect:** scala213
- Always run `sbt fmtall` before committing

## CI/CD

Three GitHub Actions workflows in `.github/workflows/`:
- **ci.yml** — On PRs and pushes to `main`: checks formatting (`scalafmtCheckAll`), linting (`scalafixAll --check`), and runs tests (`sbt test`)
- **deploy-production.yml** — On push to `main`: runs tests, builds with `sbt fastLinkJS`, deploys to Netlify (production)
- **deploy-preview.yml** — On PRs: runs tests, builds, and deploys Netlify preview, posts preview URL as PR comment
- All workflows use Java 17 (Temurin)

## Adding New Components

1. Create headless component in `headless/components/` — define state with `Var`, expose with `Signal`
2. Add render method to `Theme` trait
3. Implement the view in both `theme/inline/components/` and `theme/coreui/components/`
4. Wire into the appropriate page or layout in `App.scala`

## Adding New Pages

1. Add case to `Page` sealed trait in `Page.scala`
2. Create page state class in `headless/pages/`
3. Add route in `AppRouter.scala`
4. Add render methods to `Theme` trait
5. Implement page views in both `theme/inline/pages/` and `theme/coreui/pages/`
6. Add navigation entry in `Sidebar` component
