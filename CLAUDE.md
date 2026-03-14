# CLAUDE.md

## Project Overview

Scala.js proof-of-concept demonstrating the **headless UI pattern** — business logic and state management are fully separated from rendering/presentation. Three interchangeable themes (Inline CSS-in-Scala, CoreUI, and Tailwind CSS) render identical logic with completely different visual styles.

**Live site:** scalajs-headless-ui.netlify.app

## Tech Stack

- **Language:** Scala 2.13.18, compiled to JavaScript via Scala.js 1.20.2
- **UI Framework:** [Laminar](https://laminar.dev/) 17.2.1 (reactive DOM library)
- **Routing:** Waypoint 10.0.0-M1 (fragment-based URL routing)
- **DOM:** scalajs-dom 2.8.1
- **JSON:** [Circe](https://circe.github.io/circe/) 0.14.15 (circe-core, circe-generic, circe-parser) — used for API response decoding in FetchPage
- **Build Tool:** SBT 1.12.5
- **SBT Plugins:** sbt-scalajs 1.20.2, sbt-scalafmt 2.5.4, sbt-scalafix 0.14.2
- **Module Output:** ESModules with small module splitting
- **No npm/Node.js dependencies** — pure SBT/Scala.js build

## Build & Development Commands

```bash
# Compile Scala to JS (fast, development mode)
sbt fastLinkJS

# Full optimization build (used in production deploy)
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

**Build output:**
- Development: `target/scala-2.13/ui-template-sandbox-fastopt/main.js`
- Production: `target/scala-2.13/ui-template-sandbox-opt/main.js`

## Testing

- **Framework:** [MUnit](https://scalameta.org/munit/) 1.1.0 (Scala.js compatible)
- **Run tests:** `sbt test`
- **Test location:** `src/test/scala/com/example/headless/`
- **Coverage:** All headless components (`Counter`, `Sidebar`, `TopBar`, `Tabs`, `Accordion`, `Toggle`, `Progress`, `TagsInput`, `Tooltip`) and page containers (`DashboardPage`, `MetricsPage`, `SettingsPage`, `FetchPage`, `UIShowcasePage`) — 90+ tests total
- Tests focus on **state and behavior only** — no DOM or rendering tests
- Tests use `ManualOwner` from Airstream to synchronously read `Signal` values

```
src/test/scala/com/example/headless/
├── components/
│   ├── AccordionSuite.scala    # 6 tests: open/close, single/multi mode
│   ├── CounterSuite.scala      # 5 tests: init, custom init, increment, accumulation
│   ├── ProgressSuite.scala     # 7 tests: value, percentage, bounds, reset
│   ├── SidebarSuite.scala      # 8 tests: collapse toggle, navigation, isActive
│   ├── TabsSuite.scala         # 8 tests: selection, navigation, wrapping
│   ├── TagsInputSuite.scala    # 9 tests: add, remove, duplicates, max tags
│   ├── ToggleSuite.scala       # 5 tests: toggle, setOn, setOff
│   ├── TooltipSuite.scala      # 4 tests: show, hide, text, placement
│   └── TopBarSuite.scala       # 4 tests: brand, renderer options, selection
└── pages/
    ├── FetchPageSuite.scala    # 11 tests: Circe decoding, FetchState, TableData
    ├── PagesSuite.scala        # 8 tests: title/description for all pages
    └── UIShowcasePageSuite.scala  # 10 tests: composition, independent state
```

## Project Structure

```
src/main/scala/com/example/
├── App.scala              # Entry point, theme switching, main composition
├── AppRouter.scala        # Fragment-based URL routing (Waypoint), page content signal
├── Page.scala             # Sealed trait: Dashboard | Metrics | Settings | Fetch | UIShowcase
├── headless/
│   ├── components/        # Pure state/logic (no rendering)
│   │   ├── Accordion.scala  # Expandable sections with single/multi mode
│   │   ├── Counter.scala  # Int state + increment()
│   │   ├── Progress.scala # Bounded value with percentage computation
│   │   ├── Sidebar.scala  # Collapsed state, current page, navigation
│   │   ├── Tabs.scala     # Tab selection with keyboard navigation
│   │   ├── TagsInput.scala # Tag list with add/remove/validation
│   │   ├── Toggle.scala   # Boolean on/off switch
│   │   ├── Tooltip.scala  # Hover-driven visibility state
│   │   └── TopBar.scala   # Brand name, renderer selection (inline/coreui/tailwind)
│   └── pages/             # Page-level state containers
│       ├── DashboardPage.scala
│       ├── FetchPage.scala    # Async data fetching with loading/error/success states
│       ├── MetricsPage.scala
│       ├── SettingsPage.scala
│       └── UIShowcasePage.scala # Composes all headless components as a showcase
└── theme/
    ├── Theme.scala        # Trait defining render contract + ARIA accessibility
    ├── inline/            # CSS-in-Scala theme (no external deps)
    │   ├── InlineTheme.scala
    │   ├── components/    # InlineCounterView, InlineSidebarView, InlineTopbarView
    │   └── pages/         # InlineDashboardPageView, InlineFetchPageView, etc.
    ├── coreui/            # CoreUI CSS framework theme (v5.3.1 via CDN)
    │   ├── CoreUiTheme.scala
    │   ├── components/    # CoreUiCounterView, CoreUiSidebarView, CoreUiTopbarView
    │   └── pages/         # CoreUiDashboardPageView, CoreUiFetchPageView, etc.
    └── tailwind/          # Tailwind CSS theme (v4 via CDN)
        ├── TailwindTheme.scala
        ├── components/    # TailwindCounterView, TailwindSidebarView, TailwindTopbarView
        └── pages/         # TailwindDashboardPageView, TailwindFetchPageView, etc.
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
- `Theme.all` lists all available themes; `Theme.forKey(key)` resolves by string key
- `onActivate()` / `onDeactivate()` lifecycle hooks for CDN resource injection (CoreUI stylesheet, Tailwind script)
- ARIA accessibility: `topbar()`, `sidebar()`, `mainContent()`, and `fetchPage()` are final methods that wrap rendered output with `aria.label` attributes or lifecycle hooks

### Three Theme Implementations
| Theme | Key | Styling Approach | External Dependencies |
|-------|-----|------------------|-----------------------|
| **InlineTheme** | `"inline"` | CSS-in-Scala via Laminar DSL (`backgroundColor(...)`, `display.flex`) | None |
| **CoreUiTheme** | `"coreui"` | CoreUI CSS classes (`cls("card d-flex")`) | CoreUI v5.3.1 CSS (CDN, injected at runtime) |
| **TailwindTheme** | `"tailwind"` | Tailwind utility classes (`cls("bg-white rounded-lg shadow p-6")`) | Tailwind CSS v4 browser script (CDN, injected at runtime) |

### Reactive Patterns (Laminar)
- `Var[T]` — mutable observable state
- `Signal[T]` — read-only derived reactive values
- `child <-- signal` — subscribe DOM to signal changes
- `onClick --> callback` — event handling
- `.signal` converts `Var` to `Signal`; `.map()` transforms signals

### Naming Conventions
- Headless components: simple names (`Counter`, `Sidebar`, `TopBar`)
- Theme views: prefixed by theme name (`InlineSidebarView`, `CoreUiCounterView`, `TailwindTopbarView`)
- Pages: suffixed with `Page` (`DashboardPage`, `MetricsPage`, `FetchPage`)
- Package structure mirrors the headless/theme separation

## Code Style

- **Scalafmt** v3.8.6 with `maxColumn = 120`, `align.preset = more`
- **Scalafix** rules: `OrganizeImports` (grouped, merged)
- **Scalafmt rewrites:** `SortImports`, `RedundantBraces`, `RedundantParens`, `SortModifiers`
- **Dialect:** scala213
- **Compiler flags:** `-Xfatal-warnings` — all warnings are errors (including non-exhaustive match on sealed traits)
- Always run `sbt fmtall` before committing

### Running Lint/Format without SBT (Claude Code on the Web)

**SBT cannot run in Claude Code on the Web.** The SBT launcher bootstraps by fetching artifacts from `repo.scala-sbt.org` and `repo.typesafe.com`, which are blocked by the container's HTTP proxy. Only Maven Central (`repo1.maven.org`) is accessible. This means `sbt compile`, `sbt test`, `sbt fastLinkJS`, etc. are all unavailable — compilation and tests must be validated by CI.

For formatting and linting, use Coursier to install standalone CLIs. The **JVM-based `coursier.jar`** must be used instead of the native `cs` binary, because the native binary cannot route through the container's HTTP proxy — the JVM jar picks up proxy settings from `JAVA_TOOL_OPTIONS` automatically.

```bash
# 1. Download the Coursier JVM launcher (one-time setup)
curl -fLo ~/coursier.jar "https://github.com/coursier/coursier/releases/latest/download/coursier.jar"

# 2. Download the scalafmt native binary (one-time setup)
curl -fLo ~/scalafmt "https://github.com/scalameta/scalafmt/releases/download/v3.8.6/scalafmt-linux-musl"
chmod +x ~/scalafmt

# 3. Run scalafmt (replaces `sbt fmtall`)
~/scalafmt --config .scalafmt.conf --non-interactive src/main/scala src/test/scala

# 4. Check scalafmt (replaces `sbt scalafmtCheckAll`)
~/scalafmt --config .scalafmt.conf --non-interactive --check src/main/scala src/test/scala

# 5. Run scalafix OrganizeImports (replaces `sbt scalafixAll`)
java -jar ~/coursier.jar launch ch.epfl.scala:scalafix-cli_2.13.18:0.14.6 \
  -M scalafix.cli.Cli -- \
  --rules OrganizeImports --config .scalafix.conf \
  -f src/main/scala -f src/test/scala

# 6. Run scalac standalone for type-checking (limited — no Laminar/Scala.js on classpath)
#    Useful for checking sealed trait exhaustiveness on pure-Scala files:
java -jar ~/coursier.jar launch org.scala-lang:scala-compiler:2.13.18 \
  -M scala.tools.nsc.Main -- -d /tmp/out <files...>
```

**Note on scalafix:** The `OrganizeImports` rule is semantic and requires SemanticDB output from a prior compilation. Without SBT to compile first, scalafix will report "SemanticDB not found" errors. However, since the project config sets `removeUnused = false`, OrganizeImports only merges and sorts imports — and scalafmt's `SortImports` rewrite already handles import sorting. In practice, **running scalafmt alone is sufficient to pass CI lint checks** as long as imports are written correctly (one import per package, alphabetically ordered).

### Sealed Trait Exhaustiveness

`Page` is a **sealed trait** — the compiler warns on non-exhaustive pattern matches. With `-Xfatal-warnings` enabled in `build.sbt`, these warnings become compile errors in CI. However, since SBT cannot run in Claude Code on the Web, **you must manually ensure all pattern matches on `Page` are updated when adding new cases.** Key locations to check:

- `Page.scala` — `label`, `serialize`, `deserialize`
- `AppRouter.scala` — `pageContent` match
- `CoreUiSidebarView.scala` — `iconFor` match
- `TailwindSidebarView.scala` — `iconFor` match
- (`InlineSidebarView.scala` does not use icons, so no match to update)

## CI/CD

Three GitHub Actions workflows in `.github/workflows/`:
- **ci.yml** — On PRs and pushes to `main` (+ manual dispatch): checks formatting (`scalafmtCheckAll`), linting (`scalafixAll --check`), and runs tests (`sbt test`)
- **deploy-production.yml** — On push to `main`: runs tests, builds with `sbt fullLinkJS` (optimized), deploys to Netlify production
- **deploy-preview.yml** — On PRs: runs tests, builds with `sbt fastLinkJS`, deploys Netlify preview, posts preview URL as PR comment
- All workflows use Java 17 (Temurin) with SBT dependency caching

## Adding New Components

1. Create headless component in `headless/components/` — define state with `Var`, expose with `Signal`
2. Add render method to `Theme` trait
3. Implement the view in all three theme directories: `theme/inline/components/`, `theme/coreui/components/`, and `theme/tailwind/components/`
4. Wire into the appropriate page or layout in `App.scala`
5. Add tests in `src/test/scala/com/example/headless/components/`

## Adding New Pages

1. Add case to `Page` sealed trait in `Page.scala` — update `all`, `label`, `serialize`, `deserialize`
2. Create page state class in `headless/pages/`
3. Add route in `AppRouter.scala` — add route val, add to router routes list, instantiate page, add match case in `pageContent`
4. Add render method to `Theme` trait (use concrete method with default impl, not abstract — see note below)
5. Implement page views in all three theme directories: `theme/inline/pages/`, `theme/coreui/pages/`, and `theme/tailwind/pages/`
6. Override the render method in all three theme objects (`InlineTheme`, `CoreUiTheme`, `TailwindTheme`)
7. Add icon case in `CoreUiSidebarView.iconFor` and `TailwindSidebarView.iconFor`
8. Add tests in `src/test/scala/com/example/headless/pages/`

**Note:** When adding page render methods to `Theme`, prefer `def myPage(page: MyPage): HtmlElement = div(...)` (concrete with default) over abstract. This avoids issues with Scala.js `fastLinkJS` ESModule splitting.

## Adding New Themes

1. Create a new package under `theme/` (e.g., `theme/mytheme/`)
2. Create theme object implementing `Theme` trait with a unique `key` string
3. Implement `onActivate()` / `onDeactivate()` if external CSS/JS resources are needed
4. Implement all component views and page views
5. Add theme to `Theme.all` list in `Theme.scala`
6. Add option to `TopBar.rendererOptions` for the theme selector
