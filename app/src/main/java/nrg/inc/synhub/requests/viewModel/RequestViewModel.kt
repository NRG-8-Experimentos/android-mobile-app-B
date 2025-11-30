package com.example.synhub.requests.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synhub.requests.application.dto.CreateRequest
import com.example.synhub.requests.application.dto.RequestResponse
import com.example.synhub.shared.model.client.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RequestViewModel: ViewModel() {
    private val _request = MutableStateFlow<RequestResponse?>(null)
    val request: StateFlow<RequestResponse?> = _request

    private val _requests = MutableStateFlow<List<RequestResponse>>(emptyList())
    val requests: StateFlow<List<RequestResponse>> = _requests

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchRequestById(taskId: Long, requestId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.requestsWebService.getRequestById(taskId, requestId)
                if (response.isSuccessful && response.body() != null) {
                    _request.value = response.body()
                } else {
                    _request.value = null
                }
            } catch (e: Exception) {
                _request.value = null
            }
        }
    }

    fun updateRequestStatus(taskId: Long?, requestId: Long, status: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.requestsWebService.updateRequestStatus(taskId, requestId, status)
                if (response.isSuccessful && response.body() != null) {
                    _request.value = response.body()
                } else {
                    _request.value = null
                }
            } catch (e: Exception) {
                _request.value = null
            }
        }
    }

    // Not tested yet
    fun deleteRequest(taskId: Long, requestId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.requestsWebService.deleteRequest(taskId, requestId)
                if (response.isSuccessful) {
                    _request.value = null
                } else {
                    _request.value = null
                }
            } catch (e: Exception) {
                _request.value = null
            }
        }
    }

    fun fetchGroupRequests() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.requestsWebService.getGroupRequests()
                if (response.isSuccessful && response.body() != null) {
                    _requests.value = response.body()!!
                } else {
                    _requests.value = emptyList()
                }
            } catch (e: Exception) {
                _requests.value = emptyList()
            }
        }
    }

    fun fetchRequestsByTaskId(taskId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.requestsWebService.getRequestsByTaskId(taskId)
                if (response.isSuccessful && response.body() != null) {
                    _requests.value = response.body()!!
                    _error.value = null
                } else {
                    _requests.value = emptyList()
                    _error.value = "Error al cargar comentarios"
                }
            } catch (e: Exception) {
                _requests.value = emptyList()
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createRequest(taskId: Long, description: String, requestType: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val createRequest = CreateRequest(
                    description = description,
                    requestType = requestType,
                    taskId = taskId
                )
                val response = RetrofitClient.requestsWebService.createRequest(taskId, createRequest)
                if (response.isSuccessful && response.body() != null) {
                    _error.value = null
                    onSuccess()
                } else {
                    _error.value = "Error al crear comentario"
                    onError("Error al crear comentario")
                }
            } catch (e: Exception) {
                _error.value = e.message
                onError(e.message ?: "Error desconocido")
            } finally {
                _isLoading.value = false
            }
        }
    }
}