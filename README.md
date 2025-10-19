# RunAnywhere SDK - Simple Chat App

A simple Android chat application demonstrating the RunAnywhere SDK for on-device AI inference.

## What This App Does

This is a minimal example showing how to:

1. Initialize the RunAnywhere SDK
2. Download AI models (LLMs)
3. Load models into memory
4. Run text generation with streaming responses

## Features

- **Model Management**: Download and load AI models directly in the app
- **Real-time Streaming**: See AI responses generate word-by-word
- **Simple UI**: Clean Jetpack Compose interface
- **On-Device AI**: All inference runs locally on your Android device

## Quick Start

### 1. Build and Run

```bash
./gradlew assembleDebug
# Or open in Android Studio and click Run
```

### 2. Download a Model

1. Launch the app
2. Tap "Models" in the top bar
3. Choose a model (we recommend starting with "SmolLM2 360M Q8_0" - only 119 MB)
4. Tap "Download" and wait for it to complete

### 3. Load the Model

1. Once downloaded, tap "Load" on the model
2. Wait for "Model loaded! Ready to chat." message

### 4. Start Chatting!

1. Type a message in the text field
2. Tap "Send"
3. Watch the AI response generate in real-time

## Available Models

The app comes pre-configured with two models:

| Model | Size | Quality | Best For |
|-------|------|---------|----------|
| SmolLM2 360M Q8_0 | 119 MB | Basic | Testing, quick responses |
| Qwen 2.5 0.5B Instruct Q6_K | 374 MB | Better | General conversations |

## Technical Details

### SDK Components Used

- **RunAnywhere Core SDK**: Component architecture and model management
- **LlamaCpp Module**: Optimized llama.cpp inference engine with 7 ARM64 variants
- **Kotlin Coroutines**: For async operations and streaming

### Architecture

```
MyApplication (initialization)
    ↓
ChatViewModel (state management)
    ↓
ChatScreen (UI layer)
```

### Key Files

- `MyApplication.kt` - SDK initialization and model registration
- `ChatViewModel.kt` - Business logic and state management
- `MainActivity.kt` - UI components and composables

## Requirements

- Android 7.0 (API 24) or higher
- ~200 MB free storage (for smallest model)
- Internet connection (for downloading models)

## Troubleshooting

### Models not showing up

- Wait a few seconds for SDK initialization
- Tap "Refresh" in the Models section
- Check logcat for initialization errors

### Download fails

- Check internet connection
- Ensure sufficient storage space
- Verify INTERNET permission in AndroidManifest.xml

### App crashes during generation

- Try the smaller model (SmolLM2 360M)
- Close other apps to free memory
- Check that `largeHeap="true"` is set in AndroidManifest.xml

### Generation is slow

- This is normal for on-device inference
- Smaller models run faster
- Performance depends on device CPU

## Next Steps

Want to customize this app? Try:

1. **Add more models** - Edit `MyApplication.kt` → `registerModels()`
2. **Customize UI** - Edit `MainActivity.kt` compose functions
3. **Add system prompts** - Modify message format in `ChatViewModel.kt`
4. **Persist chat history** - Add Room database or DataStore
5. **Add model parameters** - Explore temperature, top-k, top-p settings

## Resources

- [Full Quick Start Guide](app/src/main/java/com/runanywhere/startup_hackathon20/QUICK_START_ANDROID.md)
- [RunAnywhere SDK Repository](https://github.com/RunanywhereAI/runanywhere-sdks)
- [SDK Documentation](https://github.com/RunanywhereAI/runanywhere-sdks/blob/main/CLAUDE.md)

## License

This example app follows the license of the RunAnywhere SDK.
