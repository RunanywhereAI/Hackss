# ✨ QuoteGen - AI-Powered Quote Generator

A beautiful Android app that generates inspiring quotes on-demand using on-device AI models powered
by the RunAnywhere SDK.

![QuoteGen Banner](https://img.shields.io/badge/Android-QuoteGen-6C63FF?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-100%25-7F52FF?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?style=for-the-badge&logo=jetpackcompose)

## 🌟 Features

### Core Features

- **🎯 8 Quote Categories**: Motivation, Success, Life, Love, Wisdom, Happiness, Inspiration, and
  Random
- **⚡ Real-time Generation**: Watch quotes being generated with smooth animations
- **❤️ Favorites System**: Save your favorite quotes for later
- **📜 Quote History**: Browse all previously generated quotes
- **📤 Easy Sharing**: Share quotes via any app on your device
- **📋 Copy to Clipboard**: Quick copy functionality
- **🎨 Beautiful UI**: Modern Material Design 3 with smooth animations
- **🔒 100% Offline**: All AI processing happens on-device after model download

### Technical Features

- **On-Device AI**: Powered by RunAnywhere SDK with LlamaCpp engine
- **Multiple Models**: Choose between different AI models based on quality/size preference
- **Smart Prompting**: Optimized prompts for each category
- **State Management**: Clean architecture with ViewModel and StateFlow
- **Smooth Animations**: Spring-based animations and transitions

## 📱 Screenshots

### Main Screen

- Beautiful gradient card displaying quotes
- Horizontal scrolling category selector with emojis
- Animated generate button with pulsing effect

### Features

- **Model Selector**: Download and manage AI models
- **Quote History**: View all generated quotes with favorites
- **Dark/Light Theme**: Automatic theme support

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest version recommended)
- Android device or emulator running Android 7.0 (API 24) or higher
- ~200 MB free storage for the smallest model
- Internet connection for initial model download

### Installation

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd QuoteGen
   ```

2. **Open in Android Studio**
    - Open Android Studio
    - Select "Open an existing project"
    - Navigate to the cloned directory

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   # Or click the Run button in Android Studio
   ```

## 🎮 How to Use

### First Time Setup

1. **Launch the App**
    - Open QuoteGen on your device

2. **Download a Model**
    - Tap the ⚙️ Settings icon in the top right
    - Choose a model (recommended: "SmolLM2 360M Q8_0" - 119 MB)
    - Tap "Download" and wait for completion

3. **Load the Model**
    - Once downloaded, tap "Load"
    - Wait for "Ready to generate quotes!" message

### Generating Quotes

1. **Select a Category**
    - Swipe through the horizontal category selector
    - Tap on any category to select it
    - Categories include: 💪 Motivation, 🎯 Success, 🌟 Life, and more!

2. **Generate**
    - Tap the "Generate Quote" button
    - Watch as your quote appears with a smooth animation

3. **Interact with Quotes**
    - ❤️ **Favorite**: Tap the heart icon to save favorites
    - 📤 **Share**: Share to any app (WhatsApp, Twitter, etc.)
    - 📋 **Copy**: Copy to clipboard for pasting anywhere

### Managing Quotes

- **View History**: Tap the badge in the top right to see all quotes
- **Filter Favorites**: Use the history panel to see favorite quotes
- **Delete Quotes**: Swipe or tap delete in the history panel

## 🎨 Customization

### Available Models

| Model | Size | Quality | Speed | Best For |
|-------|------|---------|-------|----------|
| SmolLM2 360M Q8_0 | 119 MB | Good | Fast | Testing, quick quotes |
| Qwen 2.5 0.5B Instruct Q6_K | 374 MB | Better | Moderate | Quality conversations |

### Adding More Models

Edit `MyApplication.kt` and add models in the `registerModels()` function:

```kotlin
private suspend fun registerModels() {
    addModelFromURL(
        url = "https://huggingface.co/your-model-url",
        name = "Your Model Name",
        type = "LLM"
    )
}
```

### Customizing Categories

Edit `QuoteViewModel.kt` to add new categories:

```kotlin
enum class QuoteCategory(val displayName: String, val emoji: String) {
    YOUR_CATEGORY("Your Category", "🎨"),
    // ... existing categories
}
```

Then add the prompt in `buildPrompt()`:

```kotlin
private fun buildPrompt(category: QuoteCategory): String {
    return when (category) {
        QuoteCategory.YOUR_CATEGORY -> "Your custom prompt here..."
        // ... existing prompts
    }
}
```

### Changing Colors

Edit `app/src/main/java/com/runanywhere/startup_hackathon20/ui/theme/Color.kt`:

```kotlin
val Primary = Color(0xFF6C63FF) // Change to your brand color
val Secondary = Color(0xFFFF6584)
// ... more colors
```

## 🏗️ Architecture

```
QuoteGen/
├── MainActivity.kt          # Main UI with all composables
├── QuoteViewModel.kt        # Business logic & state management
├── MyApplication.kt         # SDK initialization
└── ui/theme/
    ├── Color.kt            # Color definitions
    ├── Theme.kt            # Theme configuration
    └── Type.kt             # Typography
```

### Tech Stack

- **UI**: Jetpack Compose (Material Design 3)
- **Architecture**: MVVM with ViewModel
- **State Management**: Kotlin StateFlow
- **AI Engine**: RunAnywhere SDK + LlamaCpp
- **Async**: Kotlin Coroutines
- **Language**: 100% Kotlin

## 🔧 Troubleshooting

### Models Not Showing

- Wait 3-5 seconds for SDK initialization
- Tap "Refresh" in Model Settings
- Check logcat for errors

### Download Fails

- Check internet connection
- Ensure sufficient storage (~200 MB minimum)
- Verify INTERNET permission in AndroidManifest.xml

### Quotes Not Generating

- Ensure a model is loaded (check status message)
- Close other apps to free memory
- Try the smaller SmolLM2 model

### App Crashes

- Check device has enough RAM (2GB+ recommended)
- Verify `largeHeap="true"` in AndroidManifest.xml
- Try clearing app data and reloading model

### Generation is Slow

- This is normal for on-device AI
- Smaller models generate faster
- Performance varies by device CPU
- Close background apps

## 🎯 Future Ideas

- [ ] Custom quote templates
- [ ] Quote collections/albums
- [ ] Daily quote notifications
- [ ] Widget support
- [ ] Image backgrounds for quotes
- [ ] Multiple quote generation
- [ ] Quote editing capabilities
- [ ] Export quotes as images
- [ ] Sync favorites across devices
- [ ] Theme customization

## 📚 Resources

- [RunAnywhere SDK Documentation](https://github.com/RunanywhereAI/runanywhere-sdks)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)

## 🤝 Contributing

Contributions are welcome! Feel free to:

- Report bugs
- Suggest new features
- Submit pull requests
- Improve documentation

## 📄 License

This project follows the license of the RunAnywhere SDK.

## 🙏 Acknowledgments

- **RunAnywhere SDK** - For making on-device AI possible
- **Material Design** - For the beautiful design system
- **Jetpack Compose** - For the modern UI toolkit

---

Made with ❤️ and ✨ using RunAnywhere SDK

**Enjoy generating inspiring quotes! 🎉**
