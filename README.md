# PinPoint

PinPoint is a real-time location-sharing app for groups. Create or join a group, share a
group ID with friends, and see everyone's live location together on a map — perfect for
trips, events, and meetups.

Built with **Kotlin Multiplatform** + **Compose Multiplatform** (Android-first, with an iOS
target scaffolded).

## Features

- **Google Sign-In** via Credential Manager + Firebase Auth
- **Create / join groups** using a shareable group ID
- **Live location sharing** — group members' positions update in real time on a Google Map
- **Delete or leave groups** — owners delete for everyone, members leave on their own
- Dark, orange-accented UI

## Tech Stack

- **UI:** Compose Multiplatform, Material 3
- **Architecture:** MVI via [Orbit MVI](https://orbit-mvi.org/)
- **DI:** Hilt
- **Navigation:** [Compose Destinations](https://composedestinations.rafaelcosta.xyz/)
- **Auth:** Firebase Auth + AndroidX Credential Manager (`GetGoogleIdOption`)
- **Backend:** Firebase Realtime Database (groups & live locations)
- **Maps:** Google Maps SDK for Android (`maps-compose`)

## Project Structure

- [`/composeApp`](./composeApp/src) — shared Compose code and the Android app
  - [`commonMain`](./composeApp/src/commonMain/kotlin) — code common to all targets
  - [`androidMain`](./composeApp/src/androidMain/kotlin) — Android-specific code (features, repositories, DI)
- [`/iosApp`](./iosApp/iosApp) — iOS entry point (SwiftUI host)
- [`/shared`](./shared/src) — code shared across all targets

## Setup

This project needs a few secrets and Firebase/Google Cloud configuration that are **not**
checked into the repo.

### 1. Firebase

1. Create a Firebase project and add an **Android app** with package name `com.pinpoint`.
2. Enable **Authentication → Google** sign-in.
3. Enable **Realtime Database**.
4. Add your debug signing **SHA-1** to the Android app (required for Google Sign-In):
   ```shell
   ./gradlew signingReport
   ```
   Copy the `SHA1` under `Variant: debug` into Firebase → Project Settings → Your Android app.
5. Download `google-services.json` and place it at:
   ```
   composeApp/google-services.json
   ```

### 2. Google Maps API key

1. In Google Cloud Console, enable **Maps SDK for Android** and create an API key
   (restrict by SHA-1 + package `com.pinpoint`).
2. Add it to `local.properties` in the project root:
   ```properties
   MAPS_API_KEY=YOUR_KEY_HERE
   ```

### 3. Realtime Database rules

Make sure authenticated users can read/write groups, e.g.:

```json
{
  "rules": {
    "groups": { ".read": "auth != null", ".write": "auth != null" },
    "locations": { ".read": "auth != null", ".write": "auth != null" }
  }
}
```

## Build & Run

### Android

```shell
./gradlew :composeApp:assembleDebug
```

Or use the run configuration in your IDE's toolbar.

### iOS

Open [`/iosApp`](./iosApp) in Xcode and run, or use the iOS run configuration in the IDE.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).
