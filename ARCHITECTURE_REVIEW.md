# Frontend Architecture Review: scalajs-headless-ui

## 1. Executive Summary

This repository is a **well-architected proof-of-concept** that demonstrates the headless UI pattern in Scala.js with notable clarity and discipline. It cleanly separates business logic from presentation across 9 headless components, 5 pages, and 3 interchangeable themes (Inline CSS, CoreUI, Tailwind). The architecture is consistent, the code is readable, and the testing strategy correctly targets the headless layer without DOM coupling.

**Overall Score: 7.8/10** — Solidly in the **Professional Production Architecture** tier. The codebase demonstrates patterns found in high-quality open-source libraries like Radix UI and Headless UI, adapted to the Scala.js/Laminar ecosystem. It exceeds the architectural maturity of most Scala.js demo applications and rivals well-structured React/TypeScript reference projects. Since the prior review, the extraction of the `SignalHelpers` test utility and improved CI workflow organization have addressed key weaknesses, nudging the score upward.

**Estimated Author Level:** Mid-to-senior frontend engineer with strong functional programming sensibilities and architectural awareness.

---

## 2. Repository Overview

| Metric | Value |
|--------|-------|
| Language | Scala 2.13.18 → Scala.js 1.20.2 |
| UI Framework | Laminar 17.2.1 (reactive DOM) |
| Total Scala files | 76 (63 main + 13 test) |
| Lines of code (headless components) | ~200 |
| Lines of code (headless pages) | ~140 |
| Lines of code (app/routing) | ~142 |
| Lines of code (theme layer) | ~1,834 |
| Lines of code (tests) | ~686 |
| Theme implementations | 3 (Inline, CoreUI, Tailwind) |
| Theme view files | 46 |
| Headless components | 9 (Counter, Sidebar, TopBar, Tabs, Accordion, Toggle, Progress, TagsInput, Tooltip) |
| Pages | 5 (Dashboard, Metrics, Settings, Fetch, UIShowcase) |
| Tests | 91 across 13 test files (12 suites + 1 shared helper trait) |
| Build tool | SBT 1.12.5 |
| CI/CD | 3 GitHub Actions workflows (CI, deploy-production, deploy-preview) |
| External JS deps | None (pure SBT/Scala.js) |

---

## 3. Architecture Analysis

### 3.1 Headless Component Pattern

**Implementation:** Each headless component is an **immutable `final class`** that encapsulates state in `Var[T]` (mutable reactive cell) and exposes it via read-only `Signal[T]`. No component imports DOM or rendering concerns.

```
Counter.scala (14 lines):
  - Var[Int] for count state
  - Signal[Int] for read-only exposure
  - increment() for behavior

TagsInput.scala (39 lines):
  - Var[List[String]] + Var[String] for tags and input state
  - Signal-derived canAdd, tagCount
  - addTag() with validation (empty, duplicate, max check)
  - removeTag(), removeLastTag(), clearAll()
```

**Strengths:**
- Pure separation — headless components have zero knowledge of rendering
- Constructor-injected dependencies (e.g., `Sidebar` receives `Signal[Page]` and `onNavigate: Page => Unit`)
- Components are independently testable without DOM
- Pattern is consistent across all 9 components

**Comparison to industry:**
- Matches **Radix UI Primitives** philosophy: logic primitives expose state, renderers consume it
- More disciplined than **Headless UI (Tailwind Labs)**: those components still return JSX fragments, while this repo's headless layer is truly renderless
- Similar to **Vue Composition API composables**: reactive primitives (`ref`/`computed`) encapsulating logic, consumed by separate template renderers
- Aligns with the **TC39 Signals proposal** (Stage 1): `Signal.State` (writable) maps to `Var[T]`, `Signal.Computed` (derived) maps to `Signal[T]`

### 3.2 Theme System

**Implementation:** A `Theme` trait defines the rendering contract — one method per component/page. Three `object` implementations (`InlineTheme`, `CoreUiTheme`, `TailwindTheme`) each delegate to `*View` objects containing the actual markup.

**Key design decisions:**
- `protected def renderTopbar` / `final def topbar` pattern — allows the trait to inject ARIA attributes and lifecycle hooks uniformly
- `onActivate()` / `onDeactivate()` lifecycle for CDN resource management
- Runtime theme switching via `Var[Theme]` in `App.scala`
- Default implementations for page methods prevent Scala.js ESModule splitting issues
- `appLayout()` as a final method that composes topbar, sidebar, and main content in a single call

**Strengths:**
- Theme switching is seamless — no page reload, no state loss
- ARIA accessibility is enforced at the trait level, not left to individual themes
- Clear naming convention: `Inline*View`, `CoreUi*View`, `Tailwind*View`

**Weaknesses:**
- View objects are pure `render()` functions with no shared structure — duplication of structural patterns across themes (see Section 6)
- Theme trait has a growing surface area — each new component/page adds methods to the trait

### 3.3 State Management

**Pattern:** `Var[T]` for mutable state, `Signal[T]` for derived/exposed state. No global state store.

- Component-local state (e.g., `Counter.countVar`, `Tabs.selectedIndexVar`)
- Page-level composition (e.g., `UIShowcasePage` composes `Tabs`, `Accordion`, `Toggle`, etc.)
- App-level state: `Var[Theme]` for theme, `Router` for routing

**Comparison:**
- Simpler than Redux/Zustand — no actions, reducers, or middleware
- Closest analogy: Vue 3 Composition API with `ref()` / `computed()`, or SolidJS `createSignal()` / `createMemo()`
- The `Var[T]` / `Signal[T]` pair directly mirrors the TC39 Signals proposal's `Signal.State` / `Signal.Computed` — positioning this codebase well for the industry's convergence on signals-based reactivity
- Appropriate for this scale; a larger app would benefit from a more structured state management approach

### 3.4 Routing

**Implementation:** Waypoint fragment-based routing with `Page` sealed trait.

- `Page` sealed trait with 5 case objects
- `Page.serialize` / `Page.deserialize` for URL mapping
- `-Xfatal-warnings` enforces exhaustive match at compile time
- Pages are pre-instantiated singletons in `AppRouter`

**Strengths:**
- Type-safe routing with sealed trait exhaustiveness
- Clean separation of route definitions from page content rendering
- Compile-time safety against missing route cases

### 3.5 Build & Tooling

- ESModules with small module splitting for development
- Scalafmt + Scalafix for code quality
- `-Xfatal-warnings` for compile-time strictness
- Three CI workflows: reusable `ci.yml` (lint + test), `deploy.yml` (production), `pull-request.yml` (preview deploys with PR comments)
- SHA-pinned GitHub Actions for supply-chain security
- Concurrency controls to prevent overlapping deploys
- No npm/Node.js dependencies — pure Scala.js build

---

## 4. Comparison With Industry Repositories

### 4.1 Scala.js Ecosystem

| Repository | Architecture Approach | Comparison |
|-----------|----------------------|------------|
| **Laminar Examples** (raquo/laminar-examples) | Single-file components, TodoMVC-style | This repo is significantly more structured with clear layer separation |
| **Laminar Full Stack Demo** (raquo/laminar-full-stack-demo) | Full-stack with http4s backend, SPA architecture | Similar Laminar patterns, but no headless separation; focuses on full-stack concerns rather than UI architecture |
| **scalajs-react examples** | React component model in Scala | The headless approach here is more architecturally novel |

**This repo is among the most architecturally sophisticated Scala.js UI projects publicly available.** Most Scala.js demos are TodoMVC-level; this one demonstrates a real architectural pattern with multiple themes and comprehensive testing. While the Laminar ecosystem has grown (EVE Online tools, Tauri desktop apps, Web Components integration), none demonstrate headless separation at this level.

### 4.2 React/TypeScript Ecosystem

| Repository | Architecture | Score Comparison |
|-----------|-------------|-----------------|
| **bulletproof-react** (~32.8k stars) | Feature-based, layered, unidirectional dependency flow | Similar architectural discipline; bulletproof has more mature error handling, API layer abstraction, and the broader community backing |
| **Radix UI Primitives** (~18k stars) | 32+ headless accessibility primitives | The headless components follow the same philosophy but are simpler (no portal rendering, focus management, complex ARIA patterns) |
| **Headless UI** (Tailwind Labs) | Unstyled accessible components | The separation here is actually cleaner — Headless UI components still return JSX |
| **shadcn/ui** | Radix primitives + Tailwind styling (copy-paste) | Similar philosophy of separating behavior from presentation; shadcn/ui has become the dominant pattern in React 2025-2026 |

### 4.3 Vue/Svelte/Signals Ecosystem

| Framework | Pattern | Comparison |
|-----------|---------|------------|
| **Vue 3 Composition API** | `ref()` + `computed()` composables | Very similar reactive model to `Var[T]` + `Signal[T]`; Vue refs are functionally signals |
| **SolidJS** | `createSignal()` + `createMemo()` | Near-identical reactive primitives; Solid's fine-grained rendering without VDOM mirrors Laminar's direct DOM bindings |
| **Angular Signals** (v17+) | `signal()` + `computed()` | Same shift toward fine-grained reactivity; validates this repo's `Var`/`Signal` approach |
| **Svelte 5 Runes** | `$state` + `$derived` | Compiler-driven signals; same conceptual model, different syntax |

### 4.4 Pattern Alignment

| Architecture Pattern | Implementation | Industry Reference |
|---------------------|--------------------|--------------------|
| Headless UI | Fully implemented | Radix UI, Headless UI, React Aria |
| Separation of concerns | 3-layer (headless → theme trait → view) | Clean Architecture, bulletproof-react, Feature-Sliced Design |
| Type-safe routing | Sealed trait + exhaustive match | TypeScript discriminated unions |
| Reactive state | Var/Signal (Airstream) | TC39 Signals proposal, Vue ref/computed, SolidJS signals |
| Theme pluggability | Runtime-swappable via trait | Styled-components ThemeProvider, but language-level |
| Accessibility enforcement | Trait-level ARIA wrapping | Radix UI's a11y-first design |
| Unidirectional data flow | Headless → Theme → View | Feature-Sliced Design layers, bulletproof-react shared → features → app |

---

## 5. Strengths

### S1: Exemplary Headless Separation
The cleanest headless UI implementation in the Scala.js ecosystem. Components are truly renderless — no DOM imports, no framework coupling. This exceeds even Headless UI (Tailwind Labs), which still couples to React's JSX rendering. The approach aligns with the industry's 2025-2026 convergence on headless/unstyled primitives (Radix, React Aria, Base UI, Ark UI).

### S2: Consistent Architectural Patterns
Every component follows the same pattern: `final class` + `Var[T]` + `Signal[T]` + behavior methods. This consistency makes the codebase highly learnable and sets clear expectations for contributors.

### S3: Compile-Time Safety
`-Xfatal-warnings` + sealed trait exhaustiveness checking catches entire categories of bugs that TypeScript projects need runtime guards for. The `Page` sealed trait ensures every route case is handled. This is stronger than TypeScript discriminated unions, which can be bypassed.

### S4: Testing Strategy
91 tests that correctly target the headless layer. No DOM mocking, no browser testing complexity. The shared `SignalHelpers` trait (extracted to `TestHelpers.scala`) provides a clean, reusable pattern for synchronous signal testing with `ManualOwner`.

### S5: Zero External JS Dependencies
Pure SBT/Scala.js build with no npm/Node.js toolchain. Eliminates an entire class of dependency management complexity. This is especially notable in 2026, where JavaScript dependency chains remain a significant source of supply-chain risk.

### S6: ARIA Accessibility at the Architecture Level
The `Theme` trait's `final def topbar/sidebar/mainContent/fetchPage` methods enforce ARIA attributes and lifecycle hooks regardless of which theme is active. This mirrors Radix UI's a11y-first design philosophy — accessibility is an architectural decision, not an afterthought.

### S7: Well-Documented Project
Comprehensive CLAUDE.md documentation covers architecture, patterns, build commands, testing strategy, extension guides, and even instructions for running tools in constrained environments. The README provides user-facing context with industry comparisons.

### S8: Mature CI/CD Pipeline
Reusable workflow pattern (`workflow_call`), SHA-pinned Actions for supply-chain security, concurrency controls per-PR with `cancel-in-progress`, and separate preview/production deploy paths demonstrate production-grade CI/CD practices.

---

## 6. Weaknesses

### W1: Theme View Duplication
The three theme implementations share significant structural patterns (same reactive bindings, same event handlers) with only styling differences. Each counter view, for example, repeats the same `child.text <-- counter.count.map(_.toString)` and `onClick --> { _ => counter.increment() }` bindings.

**Impact:** Adding a new component requires implementing 3 nearly-identical views. This scales linearly with theme count × component count (currently 3×15 = 45 view files, plus 1 `Theme.scala`).

**Industry comparison:** Radix UI and shadcn/ui solve this with unstyled base components that accept className props. A middle-ground approach could use a "structural template" that accepts styling modifiers.

### W2: No Error Boundary Pattern
There's no mechanism for component-level error recovery. The `FetchPage` has error state handling via the `FetchState` ADT, but there's no general-purpose error boundary. Larger applications benefit from error boundary patterns (React's `ErrorBoundary`, Vue's `onErrorCaptured`).

### W3: Limited Component Interaction Patterns
Components are independently composed but don't demonstrate cross-component communication patterns (event buses, shared signals, coordination). Real applications need these patterns — e.g., a form submission triggering a notification, or theme changes affecting component behavior beyond styling.

### W4: No Lazy Loading or Code Splitting Strategy
All pages and components are eagerly instantiated in `AppRouter`. While acceptable at this scale, larger applications would benefit from lazy page loading and route-based code splitting.

### W5: Pre-Instantiated Page Singletons
`AppRouter` creates all page instances (`dashboardPage`, `metricsPage`, etc.) at startup. This means all page state persists across route changes. While intentional for demo purposes, production apps typically need lifecycle management (cleanup on unmount, fresh state on revisit).

### W6: Hardcoded Theme Keys
Theme keys (`"inline"`, `"coreui"`, `"tailwind"`) are string literals referenced in `TopBar.rendererOptions` and `Theme.forKey`. A sealed trait or enum for theme keys would add type safety and compile-time exhaustiveness checking.

### W7: Growing Theme Trait Surface Area
Each new component or page adds a method to the `Theme` trait, which all 3 implementations must override. At scale, this becomes unwieldy. Feature-sliced approaches or sub-trait composition would group related methods into smaller, focused interfaces.

---

## 7. Improvement Recommendations

### R1: Consider Structural View Templates (Priority: Medium, Effort: Medium)
For components with identical reactive structure across themes, consider a "structural skeleton" approach:
```scala
object CounterStructure {
  def render(counter: Counter, styles: CounterStyles): HtmlElement = ...
}
```
This preserves theme flexibility while reducing the 3× duplication for each component.

### R2: Type-Safe Theme Keys (Priority: Low, Effort: Low)
Replace string theme keys with a sealed trait:
```scala
sealed trait ThemeKey { def value: String }
object ThemeKey {
  case object Inline extends ThemeKey { val value = "inline" }
  case object CoreUi extends ThemeKey { val value = "coreui" }
  case object Tailwind extends ThemeKey { val value = "tailwind" }
}
```

### R3: Modularize Theme Trait (Priority: Medium, Effort: Medium)
Consider splitting the `Theme` trait into composable sub-traits as it grows:
```scala
trait ComponentTheme { def counter(...); def tabs(...); ... }
trait PageTheme { def dashboardPage(...); ... }
trait LayoutTheme { def renderAppLayout(...); ... }
trait Theme extends ComponentTheme with PageTheme with LayoutTheme
```
This follows the Interface Segregation Principle and aligns with Feature-Sliced Design's emphasis on explicit boundaries.

### R4: Page Lifecycle Management (Priority: Low, Effort: Medium)
For production use, pages should support lifecycle events (init/cleanup) rather than living as permanent singletons. Consider lazy instantiation or factory functions:
```scala
def pageContent(page: Page, theme: Theme): HtmlElement =
  page match {
    case Page.Dashboard => theme.dashboardPage(new DashboardPage())
    // Fresh instance per navigation
  }
```

### R5: Cross-Component Communication Example (Priority: Low, Effort: Low)
Add a demo of components coordinating through shared signals (e.g., a global notification system or a theme-aware component that responds to toggle state). This would demonstrate the architecture's capability for real-world interaction patterns.

### R6: Error Recovery Pattern (Priority: Low, Effort: Medium)
Consider a generic error boundary pattern that wraps page content:
```scala
def withErrorRecovery(content: => HtmlElement): HtmlElement = ...
```
This could catch rendering errors and display a fallback UI, similar to React's `ErrorBoundary` pattern.

---

## 8. Scoring Table

| Category | Score (0-10) | Notes |
|----------|:---:|-------|
| **Architecture Quality** | 8.5 | Clean 3-layer separation, consistent patterns, well-defined contracts |
| **Code Organization** | 8.0 | Clear directory structure, good naming conventions, predictable file locations |
| **Readability** | 8.5 | Concise components, consistent patterns, minimal boilerplate |
| **Maintainability** | 7.5 | Theme duplication creates linear scaling cost; growing trait surface area — offset by shared test helpers and good docs |
| **Type Safety** | 9.0 | Sealed traits, `-Xfatal-warnings`, exhaustive matching; strong compile-time guarantees |
| **Testability** | 9.0 | Headless layer is trivially testable; good coverage; clean shared `SignalHelpers` trait |
| **Scalability** | 6.0 | Theme×component multiplication, singleton pages, no lazy loading |
| **Separation of Concerns** | 9.5 | Industry-leading headless separation; ARIA at trait level |
| **Dependency Management** | 8.0 | Minimal, well-chosen deps; no JS toolchain; CDN for theme CSS |
| **Developer Experience** | 8.0 | Good docs, simple build, clear patterns; SBT-only but well-configured |

**Overall Weighted Score: 7.8/10**

---

## 9. Final Ranking and Assessment

### Ranking

| Tier | Description | This Repo? |
|------|-------------|:---:|
| Beginner-level | Tutorials, TodoMVC, scattered files | No |
| Intermediate | Reasonable structure, some patterns | No |
| **Professional Production** | **Clear architecture, tested, documented, CI/CD** | **Yes** |
| Exemplary Open-Source | Reference architecture, community standard | Close |

This repository sits in the **upper range of Professional Production Architecture**, approaching the Exemplary tier. It would need to address the scalability concerns (theme duplication, trait growth) and add more advanced patterns (lazy loading, error boundaries, cross-component coordination) to fully reach exemplary status.

### Comparison to Notable Repos

- **Better than** most Laminar/Scala.js examples, most React starter templates, and many production React apps
- **On par with** bulletproof-react in architectural discipline, Vue 3 Composition API examples in reactive patterns
- **Below** Radix UI Primitives in component sophistication (focus management, portal rendering, complex accessibility patterns like 32+ composable primitives) — but this is expected as a PoC vs. a mature library

### Position in the 2025-2026 Landscape

The industry has converged on signals-based reactivity (SolidJS, Angular 17+, Vue 3, Svelte 5 Runes, TC39 Signals proposal). This repo's `Var[T]`/`Signal[T]` model was always aligned with this trend, and the convergence validates the architectural choice. The headless UI pattern has also matured from a niche approach to a dominant paradigm, with Radix UI, shadcn/ui, React Aria, Ark UI, and Base UI all championing the separation of behavior from presentation. This repo demonstrates the same pattern in Scala.js ahead of the mainstream curve.

### Author Assessment

The codebase suggests a **mid-to-senior frontend engineer** who:
- Has strong functional programming instincts (immutable classes, constructor injection, sealed ADTs)
- Understands UI architecture patterns beyond framework-specific idioms
- Values compile-time safety and testing discipline
- Has experience with multiple UI frameworks (evidenced by the multi-theme approach)
- Thinks architecturally about separation of concerns and extensibility
- Maintains good CI/CD hygiene (SHA-pinned actions, concurrency controls, reusable workflows)

### Changes Since Prior Review

| Item | Status | Impact |
|------|--------|--------|
| `SignalHelpers` extracted to shared trait (was W3/R1) | **Resolved** | Testability score +0.5, Maintainability score +0.5 |
| CI workflows modernized (SHA pinning, reusable workflows, concurrency) | **Improved** | Better supply-chain security and DX |
| Overall score | **7.6 → 7.8** | Reflects resolved weaknesses |

### Key Takeaway

This is a **genuinely novel architectural demonstration** in the Scala.js ecosystem. The headless UI pattern, while well-established in React (Radix, Headless UI, React Aria) and now spreading to other frameworks, has not been widely implemented in Scala.js with this level of clarity and completeness. The three interchangeable themes prove the architecture works in practice, not just in theory. The `Var[T]`/`Signal[T]` reactive model is well-aligned with the industry's 2025-2026 convergence on signals. The main growth areas are in reducing theme duplication and preparing for larger-scale applications.

---

*Sources consulted for comparison:*
- [bulletproof-react](https://github.com/alan2207/bulletproof-react) — React architecture reference (~32.8k stars)
- [Radix UI Primitives](https://www.radix-ui.com/primitives) — Headless component library (~18k stars)
- [Headless UI](https://headlessui.com/) — Tailwind Labs unstyled components
- [shadcn/ui](https://ui.shadcn.com/) — Radix + Tailwind copy-paste components
- [Laminar](https://laminar.dev/) — Scala.js reactive DOM library
- [Laminar Full Stack Demo](https://github.com/raquo/laminar-full-stack-demo) — Scala.js reference app
- [Martin Fowler: Headless Component Pattern](https://martinfowler.com/articles/headless-component.html)
- [Feature-Sliced Design](https://feature-sliced.design/) — Frontend architecture methodology
- [Vue Composition API](https://vuejs.org/guide/extras/composition-api-faq.html)
- [SolidJS Signals](https://github.com/solidjs/signals) — Fine-grained reactivity
- [TC39 Signals Proposal](https://github.com/tc39/proposal-signals) — Stage 1 JavaScript signals standard
- [2026 Frontend Framework Analysis](https://dev.to/linou518/2026-frontend-framework-war-signals-won-react-is-living-off-its-ecosystem-2dki) — Signals adoption trends
- [Frontend Design Patterns 2026](https://www.netguru.com/blog/frontend-design-patterns) — Current best practices

---

## Appendix: How This Report Was Generated

This report is generated by **Claude Code** (Anthropic's CLI agent) and can be regenerated or updated at any time by providing the prompt below. The process is fully automated — no manual analysis is required.

### Generation Prompt

To regenerate this report, run Claude Code (CLI or web) in the repository root and provide the following prompt:

```
You are performing a professional frontend architecture and code quality review.

Your goal is to evaluate my frontend repository and compare it against high-quality frontend repositories across multiple ecosystems.

Use the web to research comparable open source frontend repositories.

You should include examples from:
- Scala.js UI projects (Laminar, scalajs-react, etc.)
- JavaScript React projects
- TypeScript React projects
- Other popular modern UI frameworks if relevant (Vue, Svelte, etc.)

Examples of the kinds of repos you may want to examine include:
- Laminar / Scala.js demo applications
- Production-grade React + TypeScript applications
- Well-known architecture reference repos such as "bulletproof-react"
- Real-world example applications used as architecture references

Your process should be:

1. Analyze my repository
   - Project structure
   - Folder organization
   - Component architecture
   - State management
   - Functional patterns
   - Modularity and separation of concerns
   - Build tooling
   - Type safety
   - Testing strategy
   - Documentation
   - Dependency usage
   - Maintainability signals (complexity, coupling, cohesion)

2. Research and analyze comparable repositories on the web
   - Scala.js UI repos
   - React TypeScript repos
   - Modern frontend architecture examples
   - Popular open source UI applications

3. Identify common architectural patterns in high-quality frontend repos
   Examples:
   - feature-based architecture
   - layered architecture
   - domain-driven UI structure
   - component composition patterns
   - state management strategies
   - dependency isolation
   - testability patterns

4. Compare my repository against those examples.

5. Score the repository in several categories (0–10):

   - Architecture quality
   - Code organization
   - Readability
   - Maintainability
   - Type safety
   - Testability
   - Scalability
   - Separation of concerns
   - Dependency management
   - Developer experience

6. Rank the repository against typical open source frontend repos:

   Categories:
   - Beginner-level architecture
   - Intermediate architecture
   - Professional production architecture
   - Exemplary open-source architecture

7. Identify strengths and weaknesses of the codebase.

8. Provide specific improvement suggestions.

9. Provide concrete examples from other repositories that illustrate better patterns.

10. Provide a final summary including:
   - Overall score
   - Architectural maturity
   - What level of frontend engineer likely wrote this codebase
   - How it compares to strong open-source UI repos

Output format:

Produce a structured report with the following sections:

1. Executive Summary
2. Repository Overview
3. Architecture Analysis
4. Comparison With Industry Repositories
5. Strengths
6. Weaknesses
7. Improvement Recommendations
8. Scoring Table
9. Final Ranking and Assessment

Focus heavily on architecture and project structure, not just syntax or style.
Treat this like a senior engineering code review.

Generate a markdown file version of this report and place it in the root of
this directory as ARCHITECTURE_REVIEW.md. Preserve the "Appendix: How This
Report Was Generated" section from the existing file.
```

### What the Agent Does

1. **Explores the codebase** — reads all headless components, theme implementations, pages, routing, build config, tests, and CI workflows
2. **Researches comparable repos on the web** — searches for Laminar/Scala.js examples, bulletproof-react, Radix UI, Headless UI, shadcn/ui, SolidJS signals, Vue/Svelte architecture patterns, Feature-Sliced Design, and TC39 proposals
3. **Compares patterns** — maps this repo's architecture against industry standards
4. **Scores and ranks** — produces the 10-category scoring table and tier ranking
5. **Writes the report** — outputs `ARCHITECTURE_REVIEW.md` in the repo root

### Notes for Regeneration

- The prompt includes an instruction to preserve this appendix section, so it will survive regeneration
- Scores may change between runs as the codebase evolves and as the agent discovers different comparable repos
- Web search results may vary, so the specific repos cited in Section 4 may differ across generations
- The report reflects the state of the codebase at generation time — regenerate after significant architectural changes
