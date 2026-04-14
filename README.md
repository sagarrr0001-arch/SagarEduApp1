# SagarEduApp1

SagarEduApp1 is a modern Android educational application built with Jetpack Compose. It features a mission-based learning/gaming experience with persistent data storage and a clean architecture.

## 🚀 Features

- **Interactive UI**: Fully built using **Jetpack Compose** for a modern, reactive user experience.
- **Smooth Navigation**: Uses **Navigation Compose** for seamless transitions between screens.
- **Educational Gameplay**: Includes a Game Screen for interactive missions and puzzles.
- **User Dashboard**: Personalized settings and level selection.
- **Data Persistence**: Uses **Room Database** to store scores and user progress.
- **Dependency Injection**: Powered by **Koin** for clean and maintainable code.
- **Edge-to-Edge Design**: Supports modern Android display standards.

## 🛠️ Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture**: Single Activity Architecture
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Navigation**: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)


## 🏗️ Getting Started

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 11+
- Android SDK 26+ (Minimum)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/SagarEduApp1.git
   ```
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Run the app on an emulator or physical device.

## 📂 Project Structure
- `MainActivity.kt`: The main entry point and navigation host.
- `screen/`: Contains all Compose screen implementations (Landing, Game, Setting, Score, TestDB).
- `viewmodel/`: Contains `AppViewModel` for state management.
- `ui.theme/`: Custom theme and styling for the application.

## 📜 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
