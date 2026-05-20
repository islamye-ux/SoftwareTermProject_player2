# KoLi — Korea Life Assistant

An Android app that helps foreigners living in Korea break the language barrier.
Combines **camera OCR translation**, **AI life Q&A**, and **nearby place search** in a single app.

---

## Features

| Feature | Description |
|---------|-------------|
| Camera OCR | Point camera at Korean text → instant translation via ML Kit + Google Translate |
| AI Chat | Ask anything about Korean life (visa, healthcare, banking) powered by ChatGPT |
| Nearby Places | Find hospitals, pharmacies, government offices, restaurants around you |
| Favorites | Save frequently visited places |
| Phrase Book | Essential Korean phrases with one-tap clipboard copy |

---

## Tech Stack

- **Language:** Kotlin
- **Architecture:** MVVM + Repository Pattern
- **Async:** Coroutines + StateFlow/LiveData
- **Networking:** Retrofit2 + OkHttp3
- **ML:** ML Kit Text Recognition (Korean, on-device)
- **Maps:** Google Maps SDK + Places API (New)
- **AI:** OpenAI GPT-3.5-turbo
- **Translation:** Google Cloud Translation API
- **Database:** Room (favorites, chat history)
- **Camera:** CameraX
- **Jetpack:** RecyclerView, ViewPager2, Fragment, DrawerLayout

---

## Project Structure

```
app/src/main/java/com/example/swtermproject/
├── data/
│   ├── api/          Retrofit interfaces (OpenAI, Google Translate)
│   ├── db/           Room database, DAOs, Entities
│   ├── model/        Data classes
│   └── repository/   Repository layer
├── ui/
│   ├── splash/       SplashActivity
│   ├── main/         MainActivity (DrawerLayout + ViewPager2)
│   ├── camera/       CameraActivity + ML Kit OCR
│   ├── map/          MapFragment + Places API
│   ├── chat/         ChatFragment + ChatGPT
│   ├── favorite/     FavoriteFragment + Room
│   ├── phrase/       PhraseFragment
│   └── placedetail/  PlaceDetailActivity
└── util/
    └── Constants.kt
```

---

## Setup

### 1. Clone
```bash
git clone https://github.com/mikepark814-beep/SoftwareTermProject.git
cd SoftwareTermProject
git checkout develop
```

### 2. API Keys
Create or edit `local.properties` in the project root (this file is gitignored):
```properties
MAPS_API_KEY=your_google_maps_api_key
OPENAI_API_KEY=your_openai_api_key
TRANSLATE_API_KEY=your_google_translate_api_key
```

### 3. Google Cloud Console
Enable the following APIs for your project:
- Maps SDK for Android
- Places API (New)
- Cloud Translation API

### 4. Build
Open in Android Studio → **Sync Project with Gradle Files** → Run

---

## Branch Strategy

```
main          production-ready releases
develop       integration branch
feature/*     individual features (merged into develop via PR)
```

---

## Team

| Name |
|------|
| Sejun |
| Yerik |
