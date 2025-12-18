package com.example.jambubble_client.ui.viewmodel.searchs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.spotifyremote.data.model.Track
import com.example.jambubble_client.spotifyremote.data.repository.SpotifySearchRepository
import com.example.jambubble_client.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 * 検索ViewModel
 */
class SearchViewModel(
    private val searchRepository: SpotifySearchRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Track>>(emptyList())
    val searchResults: StateFlow<List<Track>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var searchJob: Job? = null

    /**
     * 検索クエリ変更（デバウンス付き）
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        // 前の検索をキャンセル
        searchJob?.cancel()

        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        // 500msデバウンス
        searchJob = viewModelScope.launch {
            delay(500)
            search(query)
        }
    }

    /**
     * 検索実行
     */
    private fun search(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = searchRepository.searchTracks(query)) {
                is Resource.Success -> {
                    _searchResults.value = result.data
                }
                is Resource.Error -> {
                    _errorMessage.value = result.message
                }
                is Resource.Loading -> {}
            }

            _isLoading.value = false
        }
    }

    /**
     * エラーをクリア
     */
    fun clearError() {
        _errorMessage.value = null
    }
}