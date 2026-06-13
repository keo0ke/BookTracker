# BookTracker (Android Client)

[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.9+-purple?style=for-the-badge&logo=kotlin)](https://kotlinlang.org/)
[![Android SDK](https://img.shields.io/badge/Android%20SDK-26%2B-brightgreen?style=for-the-badge&logo=android)](https://developer.android.com/)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue?style=for-the-badge)]()
[![Backend Integration](https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot)](https://github.com/Pain3900/Booktracker-Backend)

A sleek, native Android application engineered to help passionate readers catalog their personal library and meticulously monitor their reading session progression in real time. Built using modern Android development practices, this client seamlessly interfaces with a dedicated Spring Boot RESTful engine to synchronize user data across sessions.

 **Looking for the server engine?** Check out the [BookTracker Backend API](https://github.com/Pain3900/Booktracker-Backend) developed by [Pain3900](https://github.com/Pain3900).

---

##  Key Features

-  **Personal Library Dashboard:** Visually browse through your catalog, filter books by genre, author, or currently reading status.
-  **Real-Time Progress Tracking:** Log new reading sessions by dynamically updating your current page and calculating your overall percentage completion metrics on the fly.
-  **REST API Integration:** Fully synchronized data persistence powered by asynchronous networking.
-  **Modern Material UI:** Clean, intuitive UI layout designed according to modern Android Material Design guidelines for seamless user immersion.

---

##  Mobile Tech Stack

- **Language:** Kotlin (1.9+)
- **Architecture Pattern:** MVVM (Model-View-ViewModel) for distinct separation of UI presentation and business rules.
- **Networking Layer:** Retrofit 2 & OkHttp for structured Type-Safe HTTP REST communication.
- **JSON Parsing:** Gson / Serialization converter factories.
- **Asynchronous Execution:** Kotlin Coroutines for efficient non-blocking background thread worker pools.
- **UI Tooling:** Android Jetpack (ViewModel, LiveData / Lifecycle components, ViewBinding).
- **Dependency Management:** Gradle (Kotlin DSL / Groovy).

---

##  Architecture Blueprint

The source tree is modularized by layer to enforce clean code and scale safely:

```text
app/src/main/java/com/.../booktracker/
│
├── data/          # Network API definitions, Retrofit clients, and Data Models
├── ui/            # UI Components (Activities, Fragments, Adapters)
└── viewmodel/     # Jetpack ViewModels managing UI state and fetching network resource flows
```

##  Development & Local Setup

To compile, run, and test the mobile client locally, follow these deployment steps:

###  Prerequisites
- **Android Studio** (Ladybug / Meerkat or newer recommended)
- **Android SDK** (Minimum API Level 26, Target API Level 34)
- A running instance of the [BookTracker Backend](https://github.com/Pain3900/Booktracker-Backend)[cite: 1]

### 1. Network Configuration
Open the network configuration file or constants file (e.g., `Constants.kt` or `RetrofitClient.kt`) and point the base URL to your local or deployed backend instance:

```kotlin
// Example network configuration constant
const val BASE_URL = "[http://10.0.2.2:8080/api/v1/](http://10.0.2.2:8080/api/v1/)" // 10.0.2.2 is the default bridge address to your host local machine from the Android Emulator
```
### 2. Build and Launch
1. Clone the repository locally.[cite: 1]
2. Open Android Studio and select **File -> Open**, then select the project root directory.[cite: 1]
3. Wait for the **Gradle Sync** to finish successfully.[cite: 1]
4. Select a target Android Emulator or connect a physical device via **USB Debugging**.[cite: 1]
5. Click the green **Run (Play)** button in the top toolbar to build and install the APK.[cite: 1]

---

##  Contributors

This mobile application is actively developed and maintained by a collaborative engineering effort:[cite: 1]

- **[keo0ke](https://github.com/keo0ke)** — Mobile Architecture & UI/UX Development[cite: 1]
- **[Pain3900](https://github.com/Pain3900)** — Core Backend Engine & API Endpoint Architecture[cite: 1]

---

##  License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.[cite: 1]
