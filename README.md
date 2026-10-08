# Fashion Store (Android)

A portfolio-quality e-commerce app for clothing/fashion, built with Kotlin, Jetpack Compose,
Material 3, MVVM + Clean Architecture, and Hilt.

## Status: all 4 phases complete

**Phase 1 — Foundation**
Gradle KTS setup, unified package (`com.sangamesh.fashionstore`), light/dark theme, bottom
navigation shell, domain models, `ProductRepository` + `MockProductRepository` (15 products,
6 categories), reusable state components (`LoadingView`/`ErrorView`/`EmptyView`, `ProductCard`).

**Phase 2 — Browsing**
Splash screen (routes to Login or Home based on session), Login/Register with validation,
full Home screen (greeting header, search entry point, promo banner, category chips, Featured /
New Arrivals / Best Sellers), Categories grid, Product Listing (filter bottom sheet + sort menu),
Product Details (image carousel, mandatory size/color selection, quantity, Add to Cart / Buy Now).

**Phase 3 — Shopping**
Search (name/brand/category), Wishlist (Room-backed, persists across restarts), Cart (Room-backed,
quantity controls, subtotal/discount/delivery/total), unit tests for cart totals and filter/sort logic.

**Phase 4 — Purchase flow**
Address management (add/edit/delete/default, Room-backed), Checkout (address, items, price
breakdown, payment method), mock Payment (Cash on Delivery / Card / UPI, simulated success and
~15% simulated failure with retry), Order Success, Orders history + Order Details with a status
timeline, Profile, Settings (dark mode + notifications via DataStore), and a Retrofit layer
(`ProductApi`, `AuthApi`, DTOs, `AuthInterceptor`) that's wired but not yet bound — see below.

## Architecture

```
presentation (Compose UI + ViewModels)
        ↓
domain (models, repository interfaces, use cases)
        ↓
data (Room + DataStore locally now; Retrofit ready for later)
```

- **State handling**: every network-driven screen has explicit Loading / Success / Error / Empty
  states. Errors are centralised in `AppError` and never expose raw exceptions to the UI.
- **Persistence**: Cart, Wishlist, Addresses and Orders are Room entities (`data/local`). Session,
  dark mode and notification preference are DataStore (`data/repository/SessionManager.kt`,
  `SettingsRepositoryImpl.kt`).
- **Testable business logic** lives in `domain/usecase`, not in ViewModels or Composables:
  `CalculateCartTotalsUseCase`, `FilterAndSortProductsUseCase`, `ValidateCheckoutUseCase`,
  `RegistrationValidator`.

## Swapping in the real backend

The app is not tightly coupled to mock data:

- **Products/Categories**: bind `ProductRepositoryImpl` instead of `MockProductRepository` in
  `di/RepositoryModule.kt`. `ProductRepositoryImpl` already calls `ProductApi` (Retrofit) and maps
  DTOs to domain models in `data/mapper/ProductMapper.kt`.
- **Auth**: swap `AuthRepositoryImpl` for an implementation that calls `AuthApi` the same way.
- **Base URL**: single source of truth is `BuildConfig.BASE_URL` (`app/build.gradle.kts`).
- **Auth header**: `AuthInterceptor` already attaches `Authorization: Bearer <token>` from the
  stored session to every Retrofit call once wired in.

No ViewModel or Composable changes are needed for the swap — they only depend on the domain
repository interfaces.

## Known limitations (by design, for portfolio scope)

- Product images are placeholders from picsum.photos — the emulator needs internet, and images
  are not brand assets.
- Editing an existing address currently reopens a blank form rather than pre-filling it (adding
  pre-fill is a small follow-up: pass the address id through the route and load it in
  `AddEditAddressScreen`).
- Auth is fully mocked (`AuthRepositoryImpl`) — any non-blank password logs in; no real backend
  call is made yet.
- "Forgot password" is a placeholder button.

## Run

Open in Android Studio (Ladybug or newer, JDK 17), let Gradle sync, run the `app` configuration.

## Tests

`app/src/test` — `MockProductRepositoryTest` (mock data integrity), `CalculateCartTotalsUseCaseTest`,
`FilterAndSortProductsUseCaseTest`, `ValidateCheckoutUseCaseTest`, `RegistrationValidatorTest`.
Run with `./gradlew test` (or the Android Studio test runner).

## Project structure

```
com.sangamesh.fashionstore
├── data/        local (Room), remote (Retrofit, ready), repository (impls), mapper
├── domain/      model, repository (interfaces), usecase
├── presentation/ navigation, splash, auth, home, category, product, search, cart,
│                 wishlist, address, checkout, orders, profile, common
├── di/          Hilt modules: Repository, Database, DataStore, Network
└── ui/theme/    Material 3 color scheme + typography
```
