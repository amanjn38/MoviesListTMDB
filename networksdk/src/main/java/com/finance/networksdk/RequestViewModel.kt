package com.finance.networksdk

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel responsible for handling network requests and exposing response data to the UI.
 *
 * @property requestRepository The repository responsible for executing HTTP requests.
 */
@HiltViewModel
class RequestViewModel @Inject constructor(private val requestRepository: RequestRepository) :
    ViewModel() {
    /**
     * Executes an HTTP request asynchronously.
     *
     * @param url The URL for the request.
     * @param method The HTTP method (e.g., GET, POST).
     * @param queryMap The map containing query parameters.
     * @param body The request body.
     */
    suspend fun executeRequest(
        baseURL: String,
        endpoint: String,
        method: String,
        queryMap: Map<String, String>?,
        headers: Map<String, String>,
        body: String?,
        page: Int = 1
    ): Resource<Response> {
        return withContext(Dispatchers.IO) {
            val url = "$baseURL/$endpoint"
            try {
                val result = requestRepository.executeRequest(url, method, queryMap,headers, body, page)
                result
            } catch (e: Exception) {
                Resource.Error("An error occurred: ${e.message}", null)
            }
        }
    }
}
