package com.runanywhere.startup_hackathon20

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.sdk.public.extensions.listAvailableModels
import com.runanywhere.sdk.models.ModelInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Quote Data Class
data class Quote(
    val text: String,
    val category: QuoteCategory,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

// Quote Categories
enum class QuoteCategory(val displayName: String, val emoji: String) {
    MOTIVATION("Motivation", "💪"),
    SUCCESS("Success", "🎯"),
    LIFE("Life", "🌟"),
    LOVE("Love", "❤️"),
    WISDOM("Wisdom", "🧠"),
    HAPPINESS("Happiness", "😊"),
    INSPIRATION("Inspiration", "✨"),
    RANDOM("Random", "🎲")
}

// Quote ViewModel
class QuoteViewModel : ViewModel() {

    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes

    private val _currentQuote = MutableStateFlow<Quote?>(null)
    val currentQuote: StateFlow<Quote?> = _currentQuote

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating

    private val _availableModels = MutableStateFlow<List<ModelInfo>>(emptyList())
    val availableModels: StateFlow<List<ModelInfo>> = _availableModels

    private val _downloadProgress = MutableStateFlow<Float?>(null)
    val downloadProgress: StateFlow<Float?> = _downloadProgress

    private val _currentModelId = MutableStateFlow<String?>(null)
    val currentModelId: StateFlow<String?> = _currentModelId

    private val _statusMessage = MutableStateFlow<String>("Initializing...")
    val statusMessage: StateFlow<String> = _statusMessage

    private val _selectedCategory = MutableStateFlow(QuoteCategory.RANDOM)
    val selectedCategory: StateFlow<QuoteCategory> = _selectedCategory

    init {
        loadAvailableModels()
    }

    private fun loadAvailableModels() {
        viewModelScope.launch {
            try {
                val models = listAvailableModels()
                _availableModels.value = models
                _statusMessage.value = "Ready - Please download and load a model"
            } catch (e: Exception) {
                _statusMessage.value = "Error loading models: ${e.message}"
            }
        }
    }

    fun downloadModel(modelId: String) {
        viewModelScope.launch {
            try {
                _statusMessage.value = "Downloading model..."
                RunAnywhere.downloadModel(modelId).collect { progress ->
                    _downloadProgress.value = progress
                    _statusMessage.value = "Downloading: ${(progress * 100).toInt()}%"
                }
                _downloadProgress.value = null
                _statusMessage.value = "Download complete! Please load the model."
            } catch (e: Exception) {
                _statusMessage.value = "Download failed: ${e.message}"
                _downloadProgress.value = null
            }
        }
    }

    fun loadModel(modelId: String) {
        viewModelScope.launch {
            try {
                _statusMessage.value = "Loading model..."
                val success = RunAnywhere.loadModel(modelId)
                if (success) {
                    _currentModelId.value = modelId
                    _statusMessage.value = "Ready to generate quotes!"
                } else {
                    _statusMessage.value = "Failed to load model"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Error loading model: ${e.message}"
            }
        }
    }

    fun selectCategory(category: QuoteCategory) {
        _selectedCategory.value = category
    }

    fun generateQuote(category: QuoteCategory = _selectedCategory.value) {
        if (_currentModelId.value == null) {
            _statusMessage.value = "Please load a model first"
            return
        }

        viewModelScope.launch {
            _isGenerating.value = true
            _statusMessage.value = "Generating quote..."

            try {
                val prompt = buildPrompt(category)
                var generatedText = ""

                RunAnywhere.generateStream(prompt).collect { token ->
                    generatedText += token
                }

                // Clean up the generated quote
                val cleanQuote = cleanupQuote(generatedText)

                if (cleanQuote.isNotBlank()) {
                    val quote = Quote(
                        text = cleanQuote,
                        category = category
                    )
                    _currentQuote.value = quote
                    _quotes.value = listOf(quote) + _quotes.value
                    _statusMessage.value = "Quote generated!"
                } else {
                    _statusMessage.value = "Failed to generate quote. Try again."
                }
            } catch (e: Exception) {
                _statusMessage.value = "Error: ${e.message}"
            }

            _isGenerating.value = false
        }
    }

    private fun buildPrompt(category: QuoteCategory): String {
        return when (category) {
            QuoteCategory.MOTIVATION -> "Generate a short, powerful motivational quote (one sentence only, no attribution, no quotes around it):"
            QuoteCategory.SUCCESS -> "Generate a short, inspiring quote about success and achievement (one sentence only, no attribution, no quotes around it):"
            QuoteCategory.LIFE -> "Generate a short, meaningful quote about life (one sentence only, no attribution, no quotes around it):"
            QuoteCategory.LOVE -> "Generate a short, beautiful quote about love (one sentence only, no attribution, no quotes around it):"
            QuoteCategory.WISDOM -> "Generate a short, wise quote about life wisdom (one sentence only, no attribution, no quotes around it):"
            QuoteCategory.HAPPINESS -> "Generate a short, uplifting quote about happiness (one sentence only, no attribution, no quotes around it):"
            QuoteCategory.INSPIRATION -> "Generate a short, inspiring quote (one sentence only, no attribution, no quotes around it):"
            QuoteCategory.RANDOM -> "Generate a short, inspiring quote (one sentence only, no attribution, no quotes around it):"
        }
    }

    private fun cleanupQuote(text: String): String {
        return text
            .trim()
            .removePrefix("\"")
            .removeSuffix("\"")
            .split("\n")
            .firstOrNull { it.isNotBlank() }
            ?.trim()
            ?: text.trim()
    }

    fun toggleFavorite(quote: Quote) {
        val updatedQuote = quote.copy(isFavorite = !quote.isFavorite)
        _quotes.value = _quotes.value.map {
            if (it.timestamp == quote.timestamp) updatedQuote else it
        }
        if (_currentQuote.value?.timestamp == quote.timestamp) {
            _currentQuote.value = updatedQuote
        }
    }

    fun deleteQuote(quote: Quote) {
        _quotes.value = _quotes.value.filter { it.timestamp != quote.timestamp }
        if (_currentQuote.value?.timestamp == quote.timestamp) {
            _currentQuote.value = _quotes.value.firstOrNull()
        }
    }

    fun getFavoriteQuotes(): List<Quote> {
        return _quotes.value.filter { it.isFavorite }
    }

    fun refreshModels() {
        loadAvailableModels()
    }
}
