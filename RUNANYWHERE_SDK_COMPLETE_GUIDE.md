# RunAnywhere SDK - Complete Guide

**The Complete Guide to On-Device AI for Android**

Version: 0.1.2-alpha  
Last Updated: 2025  
Platform: Android (API 24+)

---

## Table of Contents

1. [Overview](#overview)
2. [What is RunAnywhere SDK?](#what-is-runanywhere-sdk)
3. [Architecture & Components](#architecture--components)
4. [Installation & Setup](#installation--setup)
5. [Core APIs](#core-apis)
6. [Model Management](#model-management)
7. [Text Generation (LLM)](#text-generation-llm)
8. [Advanced Features](#advanced-features)
9. [Use Cases & Examples](#use-cases--examples)
10. [Best Practices](#best-practices)
11. [Performance Optimization](#performance-optimization)
12. [Troubleshooting](#troubleshooting)
13. [API Reference](#api-reference)

---

## Overview

### What is RunAnywhere SDK?

RunAnywhere SDK is a powerful Android library that enables developers to run AI models (specifically
Large Language Models) **directly on Android devices** without requiring server infrastructure or
internet connectivity after initial model download.

**Key Features:**

- 🚀 **On-Device Inference**: Run AI models locally on Android devices
- 📦 **Component Architecture**: Modular design with extensible service providers
- 🔄 **Model Management**: Download, cache, and manage multiple AI models
- 📊 **Analytics & Monitoring**: Built-in analytics with device registration
- 🛠️ **Tool Calling**: Prompt-based tool calling with few-shot examples
- ⚡ **Optimized Performance**: 7 CPU-optimized llama.cpp variants for ARM64
- 🔒 **Privacy First**: All data stays on device
- 💾 **Smart Caching**: Automatic model caching and version management

**What's Included:**

- **Core SDK** (4.0MB):
    - Component architecture
    - Model management and registry
    - Event system
    - Analytics infrastructure
    - Prompt-based tool calling
    - Security and encryption utilities

- **LlamaCpp Module** (2.1MB):
    - 7 optimized llama.cpp native libraries for ARM64
    - CPU variants: Baseline, fp16, dotprod, v8.4, i8mm, sve, i8mm+sve
    - Runtime CPU feature detection (automatically selects best variant)
    - GGUF model format support

---

## Architecture & Components

### SDK Architecture

```
┌─────────────────────────────────────────┐
│        Your Android Application         │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│         RunAnywhere Core SDK            │
│  ┌─────────────────────────────────┐   │
│  │  Public API Layer               │   │
│  │  - RunAnywhere singleton        │   │
│  │  - Extension functions          │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Component System               │   │
│  │  - Service Providers            │   │
│  │  - Model Registry               │   │
│  │  - Event Bus                    │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Core Services                  │   │
│  │  - Model Manager                │   │
│  │  - Download Manager             │   │
│  │  - Analytics Engine             │   │
│  │  - Security & Encryption        │   │
│  └─────────────────────────────────┘   │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│      LlamaCpp Service Provider          │
│  - Native llama.cpp integration         │
│  - 7 optimized ARM64 variants           │
│  - GGUF model loader                    │
│  - Text generation engine               │
└─────────────────────────────────────────┘
```

### Component Types

1. **Service Providers**: Extensible modules that provide specific functionality (e.g., LLM
   inference)
2. **Model Registry**: Central repository of available and downloaded models
3. **Event System**: Pub/sub architecture for SDK events (download progress, errors, etc.)
4. **Analytics**: Device registration and usage tracking
5. **Storage Layer**: Secure model caching with encryption support

---

## Installation & Setup

### Prerequisites

- **Android Studio**: Latest stable version
- **JDK**: 17 or higher
- **Minimum Android SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 36 (recommended)
- **Device Requirements**:
    - ARM64 architecture (arm64-v8a)
    - 2GB+ RAM (4GB+ recommended)
    - Storage: Varies by model (100MB - 2GB per model)

### Option 1: Local AAR Files (Fastest)

**Step 1: Download SDK AARs**

Download both required files:

- [RunAnywhereKotlinSDK-release.aar](https://github.com/RunanywhereAI/runanywhere-sdks/releases/download/android/v0.1.2-alpha/RunAnywhereKotlinSDK-release-clean.aar) (
  4.0MB)
- [runanywhere-llm-llamacpp-release.aar](https://github.com/RunanywhereAI/runanywhere-sdks/releases/download/android/v0.1.2-alpha/runanywhere-llm-llamacpp-release.aar) (
  2.1MB)

Or via command line:

```bash
cd yourproject/app/libs

curl -L -o RunAnywhereKotlinSDK-release.aar \
  https://github.com/RunanywhereAI/runanywhere-sdks/releases/download/android/v0.1.2-alpha/RunAnywhereKotlinSDK-release-clean.aar

curl -L -o runanywhere-llm-llamacpp-release.aar \
  https://github.com/RunanywhereAI/runanywhere-sdks/releases/download/android/v0.1.2-alpha/runanywhere-llm-llamacpp-release.aar
```

**Step 2: Place in `app/libs/` directory**

```
yourproject/
└── app/
    └── libs/
        ├── RunAnywhereKotlinSDK-release.aar
        └── runanywhere-llm-llamacpp-release.aar
```

**Step 3: Configure `app/build.gradle.kts`**

```kotlin
dependencies {
    // RunAnywhere SDK - Local AARs
    implementation(files("libs/RunAnywhereKotlinSDK-release.aar"))
    implementation(files("libs/runanywhere-llm-llamacpp-release.aar"))

    // Required SDK dependencies
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

    // Ktor for networking
    implementation("io.ktor:ktor-client-core:3.0.3")
    implementation("io.ktor:ktor-client-okhttp:3.0.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.3")
    implementation("io.ktor:ktor-client-logging:3.0.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Gson
    implementation("com.google.code.gson:gson:2.11.0")

    // Okio
    implementation("com.squareup.okio:okio:3.9.1")

    // AndroidX WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.0")

    // AndroidX Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // AndroidX Security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
}
```

### Option 2: Via JitPack (Recommended for Auto-Updates)

**Step 1: Add JitPack repository in `settings.gradle.kts`**

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

**Step 2: Add dependencies in `app/build.gradle.kts`**

```kotlin
dependencies {
    // RunAnywhere SDK via JitPack
    implementation("com.github.RunanywhereAI.runanywhere-sdks:runanywhere-kotlin:android-v0.1.2-alpha")
    implementation("com.github.RunanywhereAI.runanywhere-sdks:runanywhere-llm-llamacpp:android-v0.1.2-alpha")

    // Same dependencies as Option 1 above...
}
```

⚠️ **Note**: First build with JitPack takes 2-3 minutes while it builds the SDK. Subsequent syncs
are instant.

### Android Manifest Configuration

Update `AndroidManifest.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Required Permissions -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission 
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="28" />

    <application
        android:name=".MyApplication"
        android:largeHeap="true"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.MyApp">

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

**Key Configuration:**

- `android:name=".MyApplication"` - Custom Application class for SDK initialization
- `android:largeHeap="true"` - **Required** for running AI models (increases available heap memory)
- `INTERNET` permission - For downloading models
- `WRITE_EXTERNAL_STORAGE` - For model caching (Android 9 and below)

---

## Core APIs

### SDK Initialization

The SDK must be initialized before use, typically in your custom `Application` class.

**Create `MyApplication.kt`:**

```kotlin
package com.example.myapp

import android.app.Application
import android.util.Log
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.sdk.data.models.SDKEnvironment
import com.runanywhere.sdk.public.extensions.addModelFromURL
import com.runanywhere.sdk.llm.llamacpp.LlamaCppServiceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize SDK asynchronously
        GlobalScope.launch(Dispatchers.IO) {
            initializeSDK()
        }
    }

    private suspend fun initializeSDK() {
        try {
            // Step 1: Initialize SDK
            RunAnywhere.initialize(
                context = this@MyApplication,
                apiKey = "dev",  // Any string in DEVELOPMENT mode
                environment = SDKEnvironment.DEVELOPMENT
            )

            // Step 2: Register LLM Service Provider
            LlamaCppServiceProvider.register()

            // Step 3: Register Models
            registerModels()

            // Step 4: Scan for previously downloaded models
            RunAnywhere.scanForDownloadedModels()

            Log.i("MyApp", "SDK initialized successfully")

        } catch (e: Exception) {
            Log.e("MyApp", "SDK initialization failed: ${e.message}", e)
        }
    }

    private suspend fun registerModels() {
        // Register your models here
        // See Model Management section for details
    }
}
```

**Initialization Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `context` | `Context` | Application context (use `applicationContext`) |
| `apiKey` | `String` | API key for analytics (any string works in DEVELOPMENT mode) |
| `environment` | `SDKEnvironment` | `DEVELOPMENT` or `PRODUCTION` |

**Environment Modes:**

- **DEVELOPMENT**:
    - No API key validation
    - Verbose logging enabled
    - Analytics optional
    - Suitable for testing

- **PRODUCTION**:
    - API key validation required
    - Minimal logging
    - Analytics enabled
    - Optimized performance

**What Happens During Initialization:**

1. **SDK Setup**: Initializes core components and storage
2. **Service Registration**: Registers LLM provider (enables text generation)
3. **Model Registration**: Adds models to the registry
4. **Model Scanning**: Checks local storage for previously downloaded models
5. **Analytics**: Registers device (if in PRODUCTION mode)

---

## Model Management

### Registering Models

Models must be registered before they can be downloaded or used. Registration adds model metadata to
the SDK's registry.

**Basic Model Registration:**

```kotlin
import com.runanywhere.sdk.public.extensions.addModelFromURL

suspend fun registerModels() {
    addModelFromURL(
        url = "https://huggingface.co/prithivMLmods/SmolLM2-360M-GGUF/resolve/main/SmolLM2-360M.Q8_0.gguf",
        name = "SmolLM2 360M Q8_0",
        type = "LLM"
    )
}
```

**Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `url` | `String` | Direct download URL to GGUF model file |
| `name` | `String` | Human-readable model name |
| `type` | `String` | Model type (currently only "LLM" supported) |

### Recommended Models

Here's a curated list of models optimized for on-device inference:

#### Ultra-Light Models (Testing & Quick Responses)

```kotlin
// SmolLM2 360M - Fastest, smallest (119 MB)
addModelFromURL(
    url = "https://huggingface.co/prithivMLmods/SmolLM2-360M-GGUF/resolve/main/SmolLM2-360M.Q8_0.gguf",
    name = "SmolLM2 360M Q8_0",
    type = "LLM"
)

// LiquidAI LFM2 350M (210 MB)
addModelFromURL(
    url = "https://huggingface.co/Triangle104/LiquidAI-LFM-2-350M-1T-Instruct-Q4_K_M-GGUF/resolve/main/liquidai-lfm-2-350m-1t-instruct-q4_k_m.gguf",
    name = "LiquidAI LFM2 350M Q4_K_M",
    type = "LLM"
)
```

#### Light Models (General Chat)

```kotlin
// Qwen 2.5 0.5B - Good balance (374 MB)
addModelFromURL(
    url = "https://huggingface.co/Triangle104/Qwen2.5-0.5B-Instruct-Q6_K-GGUF/resolve/main/qwen2.5-0.5b-instruct-q6_k.gguf",
    name = "Qwen 2.5 0.5B Instruct Q6_K",
    type = "LLM"
)
```

#### Medium Models (Better Quality)

```kotlin
// Llama 3.2 1B - Better quality (815 MB)
addModelFromURL(
    url = "https://huggingface.co/bartowski/Llama-3.2-1B-Instruct-GGUF/resolve/main/Llama-3.2-1B-Instruct-Q6_K_L.gguf",
    name = "Llama 3.2 1B Instruct Q6_K",
    type = "LLM"
)
```

#### Large Models (Best Quality)

```kotlin
// Qwen 2.5 1.5B - Best quality (1.2 GB)
addModelFromURL(
    url = "https://huggingface.co/Qwen/Qwen2.5-1.5B-Instruct-GGUF/resolve/main/qwen2.5-1.5b-instruct-q6_k.gguf",
    name = "Qwen 2.5 1.5B Instruct Q6_K",
    type = "LLM"
)
```

**Model Selection Guide:**

| Model | Size | Speed | Quality | RAM Req. | Best For |
|-------|------|-------|---------|----------|----------|
| SmolLM2 360M Q8_0 | 119 MB | ⚡⚡⚡ | ⭐ | 1GB | Testing, demos |
| LiquidAI LFM2 350M | 210 MB | ⚡⚡⚡ | ⭐⭐ | 1GB | Quick responses |
| Qwen 2.5 0.5B | 374 MB | ⚡⚡ | ⭐⭐⭐ | 2GB | General chat |
| Llama 3.2 1B | 815 MB | ⚡ | ⭐⭐⭐⭐ | 3GB | Quality conversations |
| Qwen 2.5 1.5B | 1.2 GB | ⚡ | ⭐⭐⭐⭐⭐ | 4GB | Professional use |

### Listing Available Models

```kotlin
import com.runanywhere.sdk.public.extensions.listAvailableModels
import com.runanywhere.sdk.models.ModelInfo

suspend fun getModels(): List<ModelInfo> {
    return listAvailableModels()
}

// Usage in ViewModel:
viewModelScope.launch {
    val models = listAvailableModels()
    models.forEach { model ->
        println("Model: ${model.name}")
        println("ID: ${model.id}")
        println("Downloaded: ${model.isDownloaded}")
        println("Size: ${model.size}")
    }
}
```

**ModelInfo Properties:**

```kotlin
data class ModelInfo(
    val id: String,              // Unique model identifier
    val name: String,            // Display name
    val type: String,            // Model type ("LLM")
    val url: String,             // Download URL
    val size: Long?,             // File size in bytes (if known)
    val isDownloaded: Boolean,   // Whether model is cached locally
    val downloadPath: String?,   // Local file path (if downloaded)
    val version: String?         // Model version (optional)
)
```

### Downloading Models

Download models with progress tracking:

```kotlin
import com.runanywhere.sdk.public.RunAnywhere
import kotlinx.coroutines.flow.Flow

fun downloadModel(modelId: String) {
    viewModelScope.launch {
        try {
            RunAnywhere.downloadModel(modelId).collect { progress ->
                // progress is Float from 0.0 to 1.0
                val percentage = (progress * 100).toInt()
                Log.d("Download", "Progress: $percentage%")
                
                // Update UI
                _downloadProgress.value = percentage
            }
            Log.d("Download", "Model downloaded successfully")
            _downloadProgress.value = null
        } catch (e: Exception) {
            Log.e("Download", "Download failed: ${e.message}", e)
            _downloadProgress.value = null
        }
    }
}
```

**Download Features:**

- ✅ Resumable downloads (automatically resumes if interrupted)
- ✅ Progress tracking via Flow
- ✅ Automatic file verification
- ✅ Caching (downloaded models are reused)
- ✅ Concurrent download support (download multiple models)

**Handling Download States:**

```kotlin
sealed class DownloadState {
    object Idle : DownloadState()
    data class Downloading(val progress: Float) : DownloadState()
    object Completed : DownloadState()
    data class Failed(val error: String) : DownloadState()
}

private val _downloadState = MutableStateFlow<DownloadState>(DownloadState.Idle)
val downloadState: StateFlow<DownloadState> = _downloadState

fun downloadModelWithState(modelId: String) {
    viewModelScope.launch {
        try {
            _downloadState.value = DownloadState.Downloading(0f)
            
            RunAnywhere.downloadModel(modelId).collect { progress ->
                _downloadState.value = DownloadState.Downloading(progress)
            }
            
            _downloadState.value = DownloadState.Completed
        } catch (e: Exception) {
            _downloadState.value = DownloadState.Failed(e.message ?: "Unknown error")
        }
    }
}
```

### Loading Models

Only one model can be loaded at a time. Loading a new model automatically unloads the previous one.

```kotlin
import com.runanywhere.sdk.public.RunAnywhere

suspend fun loadModel(modelId: String): Boolean {
    return try {
        val success = RunAnywhere.loadModel(modelId)
        if (success) {
            Log.d("Model", "Model loaded successfully")
            // Model is now ready for inference
        } else {
            Log.e("Model", "Failed to load model")
        }
        success
    } catch (e: Exception) {
        Log.e("Model", "Error loading model: ${e.message}", e)
        false
    }
}

// Usage in ViewModel:
fun loadModel(modelId: String) {
    viewModelScope.launch {
        _statusMessage.value = "Loading model..."
        
        val success = RunAnywhere.loadModel(modelId)
        
        if (success) {
            _currentModelId.value = modelId
            _statusMessage.value = "Model loaded! Ready to chat."
        } else {
            _statusMessage.value = "Failed to load model"
        }
    }
}
```

**Model Loading Process:**

1. Verify model is downloaded
2. Read model file from cache
3. Initialize llama.cpp engine
4. Select optimal CPU variant
5. Load model into memory
6. Warm up inference engine

**Loading Time Estimates:**

| Model Size | Load Time | Notes |
|------------|-----------|-------|
| 100-200 MB | 2-5 seconds | Fast loading |
| 300-500 MB | 5-10 seconds | Acceptable |
| 800MB-1GB | 10-20 seconds | Show loading indicator |
| 1GB+ | 20-30+ seconds | Consider background loading |

### Unloading Models

Manually unload the current model to free memory:

```kotlin
suspend fun unloadModel() {
    try {
        RunAnywhere.unloadModel()
        Log.d("Model", "Model unloaded successfully")
    } catch (e: Exception) {
        Log.e("Model", "Error unloading model: ${e.message}")
    }
}
```

**When to Unload:**

- App going to background (to free memory)
- Switching to a different model
- Handling low memory warnings
- App cleanup/shutdown

### Scanning for Downloaded Models

Scan local storage to refresh the model registry:

```kotlin
import com.runanywhere.sdk.public.RunAnywhere

suspend fun scanForDownloadedModels() {
    try {
        RunAnywhere.scanForDownloadedModels()
        Log.d("Model", "Model scan completed")
        
        // Refresh your model list
        val models = listAvailableModels()
        _availableModels.value = models
    } catch (e: Exception) {
        Log.e("Model", "Error scanning models: ${e.message}")
    }
}
```

**Use Cases:**

- App startup (find previously downloaded models)
- After manual file copy
- Refresh button in UI
- After clearing app cache

---

## Text Generation (LLM)

### Simple Text Generation

Generate text with a single prompt:

```kotlin
import com.runanywhere.sdk.public.RunAnywhere

suspend fun generateText(prompt: String): String {
    return try {
        val response = RunAnywhere.generate(prompt)
        response
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}

// Usage:
viewModelScope.launch {
    val response = generateText("What is the capital of France?")
    println(response) // "The capital of France is Paris."
}
```

**Use Cases:**

- Simple Q&A
- Text completion
- Code generation
- Translation
- Summarization

### Streaming Text Generation

Stream text token-by-token for real-time responses:

```kotlin
import com.runanywhere.sdk.public.RunAnywhere
import kotlinx.coroutines.flow.Flow

fun generateTextStream(prompt: String): Flow<String> {
    return RunAnywhere.generateStream(prompt)
}

// Usage:
viewModelScope.launch {
    var fullResponse = ""
    
    generateTextStream("Tell me a story about a robot").collect { token ->
        fullResponse += token
        
        // Update UI with each token
        _chatMessage.value = fullResponse
        
        // Process each token
        println("Token: $token")
    }
    
    println("Complete response: $fullResponse")
}
```

**Benefits of Streaming:**

- ✅ Immediate user feedback
- ✅ Better perceived performance
- ✅ Progressive rendering
- ✅ Cancellable mid-generation
- ✅ Token-by-token processing

**Streaming UI Pattern:**

```kotlin
// In ViewModel:
fun sendMessage(text: String) {
    // Add user message
    _messages.value += ChatMessage(text, isUser = true)

    viewModelScope.launch {
        _isLoading.value = true

        var assistantResponse = ""
        RunAnywhere.generateStream(text).collect { token ->
            assistantResponse += token

            // Update or create assistant message
            val currentMessages = _messages.value.toMutableList()
            if (currentMessages.lastOrNull()?.isUser == false) {
                // Update existing assistant message
                currentMessages[currentMessages.lastIndex] = 
                    ChatMessage(assistantResponse, isUser = false)
            } else {
                // Create new assistant message
                currentMessages.add(ChatMessage(assistantResponse, isUser = false))
            }
            _messages.value = currentMessages
        }

        _isLoading.value = false
    }
}
```

### Chat Alias (Convenience Method)

The SDK provides a `chat()` alias for `generate()`:

```kotlin
suspend fun chat(prompt: String): String {
    return RunAnywhere.chat(prompt)
}
```

Both methods are identical - use whichever is more semantic for your use case.

---

## Advanced Features

### System Prompts

System prompts guide the AI's behavior and personality. While the SDK doesn't have a dedicated
system prompt API, you can prepend instructions to user prompts:

```kotlin
class ChatViewModel : ViewModel() {
    
    private val systemPrompt = """
        You are a helpful AI assistant. You provide concise, accurate answers.
        You are friendly but professional. You admit when you don't know something.
    """.trimIndent()
    
    fun sendMessageWithSystem(userMessage: String) {
        val fullPrompt = """
            System: $systemPrompt
            
            User: $userMessage
            
            Assistant:
        """.trimIndent()
        
        viewModelScope.launch {
            RunAnywhere.generateStream(fullPrompt).collect { token ->
                // Handle response
            }
        }
    }
}
```

**System Prompt Examples:**

```kotlin
// Technical Assistant
val technicalPrompt = """
    You are an expert software developer. Provide clear, well-commented code examples.
    Explain technical concepts simply. Focus on best practices and modern standards.
""".trimIndent()

// Creative Writer
val creativePrompt = """
    You are a creative storyteller. Write engaging, imaginative content.
    Use vivid descriptions and maintain narrative flow. Be original and entertaining.
""".trimIndent()

// Educational Tutor
val tutorPrompt = """
    You are a patient tutor. Break down complex topics into simple explanations.
    Use examples and analogies. Encourage questions and provide detailed answers.
""".trimIndent()

// Professional Assistant
val professionalPrompt = """
    You are a professional business assistant. Provide formal, clear communication.
    Focus on efficiency and actionable information. Maintain professional tone.
""".trimIndent()
```

### Conversation History

Maintain context across multiple exchanges:

```kotlin
class ConversationManager {
    
    private val history = mutableListOf<Pair<String, String>>()
    private val maxHistoryLength = 10
    
    suspend fun sendMessage(userMessage: String): String {
        // Build conversation context
        val conversationContext = buildString {
            // Add conversation history
            history.takeLast(maxHistoryLength).forEach { (user, assistant) ->
                appendLine("User: $user")
                appendLine("Assistant: $assistant")
                appendLine()
            }
            
            // Add current message
            appendLine("User: $userMessage")
            appendLine("Assistant:")
        }
        
        // Generate response
        val response = RunAnywhere.generate(conversationContext)
        
        // Store in history
        history.add(userMessage to response)
        
        return response
    }
    
    fun clearHistory() {
        history.clear()
    }
}
```

### Generation Parameters (Advanced)

While the current SDK version doesn't expose generation parameters directly, they can be explored
through custom model configurations:

**Common Parameters (for reference):**

| Parameter | Default | Range | Description |
|-----------|---------|-------|-------------|
| `temperature` | 0.7 | 0.0-2.0 | Randomness (lower = more focused) |
| `top_k` | 40 | 1-100 | Top K sampling |
| `top_p` | 0.9 | 0.0-1.0 | Nucleus sampling |
| `repeat_penalty` | 1.1 | 1.0-2.0 | Repetition penalty |
| `max_tokens` | 256 | 1-4096 | Maximum response length |

**Note**: These parameters may be exposed in future SDK versions.

### Error Handling

Comprehensive error handling for robust applications:

```kotlin
class RobustChatViewModel : ViewModel() {
    
    sealed class ChatState {
        object Idle : ChatState()
        object Loading : ChatState()
        data class Success(val message: String) : ChatState()
        data class Error(val error: String, val canRetry: Boolean) : ChatState()
    }
    
    private val _chatState = MutableStateFlow<ChatState>(ChatState.Idle)
    val chatState: StateFlow<ChatState> = _chatState
    
    fun sendMessage(text: String) {
        viewModelScope.launch {
            _chatState.value = ChatState.Loading
            
            try {
                var response = ""
                RunAnywhere.generateStream(text).collect { token ->
                    response += token
                }
                
                _chatState.value = ChatState.Success(response)
                
            } catch (e: IllegalStateException) {
                // Model not loaded
                _chatState.value = ChatState.Error(
                    "Please load a model first",
                    canRetry = false
                )
                
            } catch (e: OutOfMemoryError) {
                // Out of memory
                _chatState.value = ChatState.Error(
                    "Out of memory. Try a smaller model.",
                    canRetry = false
                )
                
            } catch (e: CancellationException) {
                // User cancelled
                _chatState.value = ChatState.Idle
                
            } catch (e: Exception) {
                // Generic error
                _chatState.value = ChatState.Error(
                    e.message ?: "Unknown error",
                    canRetry = true
                )
            }
        }
    }
    
    fun retry() {
        // Implement retry logic
    }
}
```

### Cancellation

Cancel ongoing generation:

```kotlin
class ChatViewModel : ViewModel() {
    
    private var generationJob: Job? = null
    
    fun sendMessage(text: String) {
        generationJob = viewModelScope.launch {
            RunAnywhere.generateStream(text).collect { token ->
                // Handle tokens
            }
        }
    }
    
    fun cancelGeneration() {
        generationJob?.cancel()
        generationJob = null
        Log.d("Chat", "Generation cancelled")
    }
}
```

### Analytics Events

The SDK includes built-in analytics for monitoring usage (primarily in PRODUCTION mode):

**Tracked Events:**

- Device registration
- Model downloads
- Model loads
- Generation requests
- Errors and exceptions

**Privacy Note**: All analytics data is anonymous and used only for SDK improvement. In DEVELOPMENT
mode, analytics are minimal.

---

## Use Cases & Examples

### Use Case 1: Chat Application

**Full implementation of a chat app with model management:**

```kotlin
// ChatViewModel.kt
class ChatViewModel : ViewModel() {

    data class ChatMessage(val text: String, val isUser: Boolean)

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _availableModels = MutableStateFlow<List<ModelInfo>>(emptyList())
    val availableModels: StateFlow<List<ModelInfo>> = _availableModels

    private val _currentModelId = MutableStateFlow<String?>(null)
    val currentModelId: StateFlow<String?> = _currentModelId

    init {
        loadAvailableModels()
    }

    private fun loadAvailableModels() {
        viewModelScope.launch {
            val models = listAvailableModels()
            _availableModels.value = models
        }
    }

    fun downloadModel(modelId: String) {
        viewModelScope.launch {
            RunAnywhere.downloadModel(modelId).collect { progress ->
                // Update download progress
            }
        }
    }

    fun loadModel(modelId: String) {
        viewModelScope.launch {
            val success = RunAnywhere.loadModel(modelId)
            if (success) {
                _currentModelId.value = modelId
            }
        }
    }

    fun sendMessage(text: String) {
        _messages.value += ChatMessage(text, isUser = true)

        viewModelScope.launch {
            _isLoading.value = true

            var response = ""
            RunAnywhere.generateStream(text).collect { token ->
                response += token
                
                val currentMessages = _messages.value.toMutableList()
                if (currentMessages.lastOrNull()?.isUser == false) {
                    currentMessages[currentMessages.lastIndex] = 
                        ChatMessage(response, isUser = false)
                } else {
                    currentMessages.add(ChatMessage(response, isUser = false))
                }
                _messages.value = currentMessages
            }

            _isLoading.value = false
        }
    }

    fun clearChat() {
        _messages.value = emptyList()
    }
}
```

### Use Case 2: Code Assistant

**AI-powered coding helper:**

```kotlin
class CodeAssistantViewModel : ViewModel() {

    private val systemPrompt = """
        You are an expert Android developer. Provide clear, well-commented Kotlin code.
        Follow Android best practices and Material Design guidelines.
        Include imports and explain your code choices.
    """.trimIndent()

    suspend fun generateCode(request: String): String {
        val prompt = """
            $systemPrompt
            
            Developer Request: $request
            
            Code Solution:
        """.trimIndent()

        return RunAnywhere.generate(prompt)
    }

    suspend fun explainCode(code: String): String {
        val prompt = """
            $systemPrompt
            
            Explain this code in detail:
            
            ```kotlin
            $code
            ```
            
            Explanation:
        """.trimIndent()

        return RunAnywhere.generate(prompt)
    }

    suspend fun fixBug(code: String, error: String): String {
        val prompt = """
            $systemPrompt
            
            This code has an error:
            
            ```kotlin
            $code
            ```
            
            Error: $error
            
            Provide the fixed code with explanation:
        """.trimIndent()

        return RunAnywhere.generate(prompt)
    }
}
```

### Use Case 3: Personal Knowledge Assistant

**Offline-first Q&A system:**

```kotlin
class KnowledgeAssistant {

    private val knowledgeBase = mutableMapOf<String, String>()

    suspend fun askQuestion(question: String): String {
        // Check knowledge base first
        val cached = knowledgeBase[question]
        if (cached != null) return cached

        // Generate new answer
        val prompt = """
            Answer this question clearly and concisely:
            
            Question: $question
            
            Answer:
        """.trimIndent()

        val answer = RunAnywhere.generate(prompt)
        
        // Cache for future
        knowledgeBase[question] = answer
        
        return answer
    }

    suspend fun summarizeText(text: String): String {
        val prompt = """
            Summarize the following text in 2-3 sentences:
            
            $text
            
            Summary:
        """.trimIndent()

        return RunAnywhere.generate(prompt)
    }

    suspend fun extractKeyPoints(text: String): List<String> {
        val prompt = """
            Extract the key points from this text as a bullet list:
            
            $text
            
            Key Points:
        """.trimIndent()

        val response = RunAnywhere.generate(prompt)
        return response.lines().filter { it.startsWith("-") || it.startsWith("•") }
    }
}
```

### Use Case 4: Creative Writing Tool

**Story and content generation:**

```kotlin
class CreativeWritingAssistant : ViewModel() {

    suspend fun generateStory(prompt: String, genre: String): Flow<String> {
        val fullPrompt = """
            You are a creative writer specializing in $genre.
            Write an engaging story based on this prompt:
            
            $prompt
            
            Story:
        """.trimIndent()

        return RunAnywhere.generateStream(fullPrompt)
    }

    suspend fun continueStory(existingStory: String): Flow<String> {
        val prompt = """
            Continue this story naturally and creatively:
            
            $existingStory
            
            Continuation:
        """.trimIndent()

        return RunAnywhere.generateStream(prompt)
    }

    suspend fun generateCharacter(traits: String): String {
        val prompt = """
            Create a detailed character profile with these traits:
            $traits
            
            Include: Name, appearance, personality, background, motivations
            
            Character Profile:
        """.trimIndent()

        return RunAnywhere.generate(prompt)
    }
}
```

### Use Case 5: Educational Tutor

**Interactive learning assistant:**

```kotlin
class TutorAssistant : ViewModel() {

    private val conversationHistory = mutableListOf<String>()

    suspend fun explainConcept(topic: String, level: String): String {
        val prompt = """
            You are a patient tutor. Explain this concept at a $level level:
            
            Topic: $topic
            
            Use simple language, examples, and analogies.
            
            Explanation:
        """.trimIndent()

        return RunAnywhere.generate(prompt)
    }

    suspend fun generateQuiz(topic: String, numQuestions: Int): String {
        val prompt = """
            Create $numQuestions multiple-choice questions about: $topic
            
            Format:
            Q1: [Question]
            A) [Option]
            B) [Option]
            C) [Option]
            D) [Option]
            Answer: [Correct option]
            
            Questions:
        """.trimIndent()

        return RunAnywhere.generate(prompt)
    }

    suspend fun provideFeedback(answer: String, correctAnswer: String): String {
        val prompt = """
            The student answered: $answer
            The correct answer is: $correctAnswer
            
            Provide encouraging feedback and explain why the correct answer is right:
        """.trimIndent()

        return RunAnywhere.generate(prompt)
    }
}
```

### Use Case 6: Text Utilities

**Practical text processing:**

```kotlin
class TextUtilities {

    suspend fun translate(text: String, targetLanguage: String): String {
        val prompt = """
            Translate this text to $targetLanguage:
            
            $text
            
            Translation:
        """.trimIndent()

        return RunAnywhere.generate(prompt)
    }

    suspend fun improveWriting(text: String): String {
        val prompt = """
            Improve this text for clarity, grammar, and style:
            
            Original: $text
            
            Improved:
        """.trimIndent()

        return RunAnywhere.generate(prompt)
    }

    suspend fun generateEmail(purpose: String, tone: String): String {
        val prompt = """
            Write a $tone email for this purpose:
            
            $purpose
            
            Email:
        """.trimIndent()

        return RunAnywhere.generate(prompt)
    }

    suspend fun extractEntities(text: String): Map<String, List<String>> {
        val prompt = """
            Extract named entities from this text:
            
            $text
            
            Format:
            People: [list]
            Organizations: [list]
            Locations: [list]
            Dates: [list]
        """.trimIndent()

        val response = RunAnywhere.generate(prompt)
        // Parse response into map
        return parseEntities(response)
    }

    private fun parseEntities(response: String): Map<String, List<String>> {
        // Implementation to parse response into structured data
        return mapOf()
    }
}
```

---

## Best Practices

### 1. Initialization

✅ **DO:**

- Initialize SDK in `Application.onCreate()`
- Use `GlobalScope.launch(Dispatchers.IO)` for async init
- Handle initialization errors gracefully
- Register models during initialization
- Call `scanForDownloadedModels()` after init

❌ **DON'T:**

- Initialize in Activity (will re-init on rotation)
- Block UI thread during initialization
- Ignore initialization errors

### 2. Model Management

✅ **DO:**

- Start with smallest model for testing (SmolLM2 360M)
- Show download progress to users
- Handle download failures gracefully
- Cache models locally (SDK does this automatically)
- Provide model selection UI

❌ **DON'T:**

- Download large models without user consent
- Download on metered connections without asking
- Keep unused models loaded

### 3. Memory Management

✅ **DO:**

- Set `android:largeHeap="true"` in manifest
- Unload models when app goes to background
- Monitor memory usage
- Choose model size based on device RAM
- Test on low-end devices

❌ **DON'T:**

- Load multiple models simultaneously
- Keep models loaded indefinitely
- Ignore `OutOfMemoryError` exceptions

### 4. User Experience

✅ **DO:**

- Use streaming for real-time feedback
- Show loading indicators during generation
- Allow cancellation of long operations
- Provide clear error messages
- Auto-scroll chat interfaces

❌ **DON'T:**

- Block UI during generation
- Generate without user confirmation
- Hide long loading times

### 5. Error Handling

✅ **DO:**

- Catch and handle all exceptions
- Provide retry mechanisms
- Log errors for debugging
- Show user-friendly error messages
- Validate model loaded before generation

❌ **DON'T:**

- Let app crash on errors
- Show technical error messages to users
- Ignore network/storage errors

### 6. Performance

✅ **DO:**

- Use `Dispatchers.IO` for model operations
- Stream responses for better perceived performance
- Warm up model with test generation
- Profile performance on target devices
- Limit conversation history length

❌ **DON'T:**

- Run heavy operations on main thread
- Generate extremely long responses
- Keep unlimited history in memory

### 7. Privacy & Security

✅ **DO:**

- Emphasize on-device processing in marketing
- Clear chat history when appropriate
- Handle sensitive data carefully
- Use DEVELOPMENT mode for testing
- Respect user privacy preferences

❌ **DON'T:**

- Send user data to external servers (without consent)
- Store sensitive conversations permanently
- Log private user data

---

## Performance Optimization

### Model Selection Strategy

**By Device RAM:**

| Device RAM | Recommended Model | Alternative |
|------------|-------------------|-------------|
| 1-2 GB | SmolLM2 360M | LiquidAI 350M |
| 2-3 GB | Qwen 0.5B | SmolLM2 360M |
| 3-4 GB | Llama 3.2 1B | Qwen 0.5B |
| 4+ GB | Qwen 1.5B | Llama 3.2 1B |

**Runtime Device Detection:**

```kotlin
fun recommendModel(): String {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memoryInfo = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo)
    
    val totalRam = memoryInfo.totalMem / (1024 * 1024 * 1024) // GB
    
    return when {
        totalRam < 2 -> "SmolLM2 360M Q8_0"
        totalRam < 3 -> "Qwen 2.5 0.5B Instruct Q6_K"
        totalRam < 4 -> "Llama 3.2 1B Instruct Q6_K"
        else -> "Qwen 2.5 1.5B Instruct Q6_K"
    }
}
```

### Background Model Loading

Load models in background to avoid blocking UI:

```kotlin
class ModelLoader(private val context: Context) {

    private var loadJob: Job? = null

    fun loadModelInBackground(modelId: String, onComplete: (Boolean) -> Unit) {
        loadJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = RunAnywhere.loadModel(modelId)
                
                withContext(Dispatchers.Main) {
                    onComplete(success)
                }
            } catch (e: Exception) {
                Log.e("ModelLoader", "Background load failed", e)
                withContext(Dispatchers.Main) {
                    onComplete(false)
                }
            }
        }
    }

    fun cancel() {
        loadJob?.cancel()
    }
}
```

### Memory Monitoring

Monitor memory usage during inference:

```kotlin
class MemoryMonitor(private val context: Context) {

    fun getAvailableMemory(): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem
    }

    fun isLowMemory(): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.lowMemory
    }

    fun logMemoryUsage() {
        val runtime = Runtime.getRuntime()
        val used = runtime.totalMemory() - runtime.freeMemory()
        val max = runtime.maxMemory()
        
        Log.d("Memory", "Used: ${used / 1024 / 1024}MB / Max: ${max / 1024 / 1024}MB")
    }
}
```

### Lifecycle Management

Properly manage models across app lifecycle:

```kotlin
class LifecycleAwareModelManager(
    private val application: Application
) : DefaultLifecycleObserver {

    private var currentModelId: String? = null

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // Unload model when app goes to background
        CoroutineScope(Dispatchers.IO).launch {
            RunAnywhere.unloadModel()
            Log.d("Lifecycle", "Model unloaded (app backgrounded)")
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // Reload model when app returns to foreground
        currentModelId?.let { modelId ->
            CoroutineScope(Dispatchers.IO).launch {
                RunAnywhere.loadModel(modelId)
                Log.d("Lifecycle", "Model reloaded (app foregrounded)")
            }
        }
    }

    fun setCurrentModel(modelId: String) {
        currentModelId = modelId
    }
}

// Usage in Application class:
class MyApplication : Application() {
    private lateinit var modelManager: LifecycleAwareModelManager

    override fun onCreate() {
        super.onCreate()
        
        modelManager = LifecycleAwareModelManager(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(modelManager)
    }
}
```

---

## Troubleshooting

### Common Issues

#### 1. SDK Initialization Fails

**Symptoms:**

- App crashes on startup
- "SDK not initialized" errors
- Models don't appear

**Solutions:**

```kotlin
// ✅ Correct: Async initialization
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                RunAnywhere.initialize(
                    context = this@MyApplication,
                    apiKey = "dev",
                    environment = SDKEnvironment.DEVELOPMENT
                )
                LlamaCppServiceProvider.register()
            } catch (e: Exception) {
                Log.e("App", "Init failed", e)
                // Show error to user
            }
        }
    }
}
```

#### 2. Model Download Fails

**Symptoms:**

- Download progress stalls
- "Download failed" error
- Models don't save

**Solutions:**

- Check internet connection
- Verify INTERNET permission in manifest
- Ensure sufficient storage space
- Check HuggingFace URL is accessible
- Retry download

```kotlin
fun downloadWithRetry(modelId: String, maxRetries: Int = 3) {
    viewModelScope.launch {
        repeat(maxRetries) { attempt ->
            try {
                RunAnywhere.downloadModel(modelId).collect { progress ->
                    // Handle progress
                }
                return@launch // Success
            } catch (e: Exception) {
                if (attempt == maxRetries - 1) {
                    // Final attempt failed
                    Log.e("Download", "Failed after $maxRetries attempts", e)
                } else {
                    delay(2000) // Wait before retry
                }
            }
        }
    }
}
```

#### 3. Model Load Fails

**Symptoms:**

- "Failed to load model" error
- App hangs during load
- Model loads but generation fails

**Solutions:**

- Ensure model is fully downloaded
- Check available device memory
- Verify model file isn't corrupted
- Try re-downloading model
- Use smaller model

```kotlin
suspend fun loadModelSafely(modelId: String): Boolean {
    // Check if downloaded
    val models = listAvailableModels()
    val model = models.find { it.id == modelId }
    
    if (model?.isDownloaded != true) {
        Log.e("Load", "Model not downloaded")
        return false
    }
    
    // Check memory
    val memoryMonitor = MemoryMonitor(context)
    if (memoryMonitor.isLowMemory()) {
        Log.e("Load", "Low memory")
        return false
    }
    
    // Try loading
    return try {
        RunAnywhere.loadModel(modelId)
    } catch (e: Exception) {
        Log.e("Load", "Load failed", e)
        false
    }
}
```

#### 4. Generation is Slow

**Symptoms:**

- Tokens generate slowly
- App feels unresponsive
- Long wait times

**Solutions:**

- Use smaller model
- Enable `largeHeap` in manifest
- Close other apps
- Check device specifications
- Limit prompt length

```kotlin
// Optimize generation
suspend fun generateOptimized(prompt: String): String {
    // Limit prompt length
    val trimmedPrompt = if (prompt.length > 1000) {
        prompt.take(1000) + "..."
    } else {
        prompt
    }
    
    return RunAnywhere.generate(trimmedPrompt)
}
```

#### 5. App Crashes During Generation

**Symptoms:**

- OutOfMemoryError
- App force closes
- Device freezes

**Solutions:**

```xml
<!-- AndroidManifest.xml -->
<application
    android:largeHeap="true"
    ... >
</application>
```

```kotlin
// Monitor memory during generation
fun generateWithMemoryMonitoring(prompt: String) {
    val memoryMonitor = MemoryMonitor(context)
    
    viewModelScope.launch {
        try {
            memoryMonitor.logMemoryUsage()
            
            if (memoryMonitor.isLowMemory()) {
                // Warn user or abort
                _error.value = "Low memory. Please close other apps."
                return@launch
            }
            
            RunAnywhere.generateStream(prompt).collect { token ->
                // Handle token
            }
            
        } catch (e: OutOfMemoryError) {
            Log.e("Generate", "Out of memory", e)
            _error.value = "Out of memory. Try a smaller model."
            
            // Unload model to free memory
            RunAnywhere.unloadModel()
        }
    }
}
```

#### 6. Models Not Appearing After Registration

**Symptoms:**

- Empty model list
- Models registered but not shown
- Model registry seems empty

**Solutions:**

```kotlin
// Force model scan
suspend fun refreshModels() {
    try {
        // Scan for downloaded models
        RunAnywhere.scanForDownloadedModels()
        
        // Get updated list
        val models = listAvailableModels()
        _availableModels.value = models
        
        Log.d("Models", "Found ${models.size} models")
    } catch (e: Exception) {
        Log.e("Models", "Refresh failed", e)
    }
}
```

#### 7. JitPack Build Timeout

**Symptoms:**

- First Gradle sync takes forever
- "JitPack timeout" error
- Build never completes

**Solutions:**

- Wait 3-5 minutes (first build compiles SDK)
- Switch to local AAR files (faster)
- Check internet connection
- Use VPN if JitPack is blocked

```kotlin
// Use local AARs instead:
dependencies {
    implementation(files("libs/RunAnywhereKotlinSDK-release.aar"))
    implementation(files("libs/runanywhere-llm-llamacpp-release.aar"))
}
```

---

## API Reference

### RunAnywhere (Core API)

Main singleton class for SDK operations.

```kotlin
object RunAnywhere {
    
    // Initialization
    suspend fun initialize(
        context: Context,
        apiKey: String,
        environment: SDKEnvironment
    )
    
    // Model Management
    suspend fun downloadModel(modelId: String): Flow<Float>
    suspend fun loadModel(modelId: String): Boolean
    suspend fun unloadModel()
    suspend fun scanForDownloadedModels()
    
    // Text Generation
    suspend fun generate(prompt: String): String
    fun generateStream(prompt: String): Flow<String>
    suspend fun chat(prompt: String): String  // Alias for generate()
}
```

### Extension Functions

Convenience functions for common operations.

```kotlin
// Model registration
suspend fun addModelFromURL(
    url: String,
    name: String,
    type: String
)

// Model listing
suspend fun listAvailableModels(): List<ModelInfo>
```

### Service Providers

```kotlin
object LlamaCppServiceProvider {
    // Register LLM service provider
    fun register()
}
```

### Data Classes

```kotlin
// Model information
data class ModelInfo(
    val id: String,
    val name: String,
    val type: String,
    val url: String,
    val size: Long?,
    val isDownloaded: Boolean,
    val downloadPath: String?,
    val version: String?
)

// SDK environment
enum class SDKEnvironment {
    DEVELOPMENT,
    PRODUCTION
}
```

### Complete API Summary

| API | Type | Description |
|-----|------|-------------|
| `RunAnywhere.initialize()` | suspend fun | Initialize SDK with context, API key, environment |
| `RunAnywhere.downloadModel()` | suspend fun | Download model with progress tracking |
| `RunAnywhere.loadModel()` | suspend fun | Load model into memory for inference |
| `RunAnywhere.unloadModel()` | suspend fun | Unload current model from memory |
| `RunAnywhere.generate()` | suspend fun | Generate text from prompt (blocking) |
| `RunAnywhere.generateStream()` | fun | Generate text with streaming (Flow) |
| `RunAnywhere.chat()` | suspend fun | Alias for generate() |
| `RunAnywhere.scanForDownloadedModels()` | suspend fun | Scan storage for cached models |
| `addModelFromURL()` | suspend fun | Register model in SDK registry |
| `listAvailableModels()` | suspend fun | Get list of registered models |
| `LlamaCppServiceProvider.register()` | fun | Register LLM service provider |

---

## Conclusion

RunAnywhere SDK empowers Android developers to build powerful, privacy-focused AI applications that
run entirely on-device. With its simple API, optimized performance, and comprehensive model
management, you can create intelligent apps without complex infrastructure.

### Quick Checklist

✅ SDK installed (AAR or JitPack)  
✅ Dependencies added to `build.gradle.kts`  
✅ Manifest configured (`largeHeap`, permissions)  
✅ Application class created with initialization  
✅ Service provider registered (`LlamaCppServiceProvider`)  
✅ Models registered with `addModelFromURL()`  
✅ Model downloaded and loaded  
✅ Text generation working

### Next Steps

1. **Experiment** with different models
2. **Customize** system prompts for your use case
3. **Optimize** for your target devices
4. **Build** amazing on-device AI experiences
5. **Share** your feedback with the RunAnywhere team

### Support & Resources

- **GitHub**: [RunanywhereAI/runanywhere-sdks](https://github.com/RunanywhereAI/runanywhere-sdks)
- **Issues**: [GitHub Issues](https://github.com/RunanywhereAI/runanywhere-sdks/issues)
- **Releases**: [GitHub Releases](https://github.com/RunanywhereAI/runanywhere-sdks/releases)
- **Models**: [HuggingFace](https://huggingface.co/models?library=gguf)

### Version History

- **v0.1.2-alpha** (Current)
    - Latest stable release
    - Local AAR deployment
    - Improved stability

- **v0.1.1-alpha**
    - Prompt-based tool calling
    - Analytics improvements
    - Device registration
    - JVM platform fixes

- **v0.1.0-alpha**
    - Initial release
    - Core SDK architecture
    - LlamaCpp integration
    - Basic model management

---

**Made with ❤️ by the RunAnywhere team**

*Empowering developers to build intelligent, privacy-first Android applications*
