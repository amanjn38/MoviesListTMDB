package com.finance.networksdk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class RequestRepository {
    /**
     * Executes the HTTP request asynchronously.
     *
     * @param url The URL for the request.
     * @param method The HTTP method (e.g., GET, POST).
     * @param queryMap The map containing query parameters.
     * @param body The request body.
     */
    suspend fun executeRequest(
        url: String,
        method: String,
        queryMap: Map<String, String>? = null,
        headers: Map<String, String>,
        body: String? = null,
        page: Int = 1
    ): Resource<Response> {
        return try {
            val completeUrl = appendQueryParams(url, queryMap)

            val response = withContext(Dispatchers.IO) {
                val conn = makeRequest(completeUrl, method, headers, body)
                parseResponse(conn)
            }
            Resource.Success(response)
        } catch (e: IOException) {
            Resource.Error("Network Error: ${e.message}", null)
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.message}", null)
        }
    }

    private fun appendQueryParams(url: String, queryMap: Map<String, String>?): String {
        if (queryMap.isNullOrEmpty()) {
            return url
        }

        val queryString = queryMap.entries.joinToString("&") { (key, value) ->
            "${key.encodeURIComponent()}=${value.encodeURIComponent()}"
        }
        return if (url.contains("?")) {
            "$url&$queryString"
        } else {
            "$url?$queryString"
        }
    }

    private fun String.encodeURIComponent(): String {
        return if (containsPipe()) {
            this
        } else {
            URLEncoder.encode(this, "UTF-8")
        }
    }

    private fun String.containsPipe(): Boolean {
        return '|' in this
    }

    /**
     * Makes an HTTP request to the specified URL.
     *
     * @param url The URL for the request.
     * @param method The HTTP method (e.g., GET, POST).
     * @param queryMap The map containing query parameters.
     * @param body The request body.
     * @return The HttpURLConnection object representing the connection.
     */
    private fun makeRequest(
        url: String,
        method: String,
        headers: Map<String, String>,
        body: String?
    ): HttpURLConnection {
        // Create the URL object
        val urlConnection = URL(url).openConnection() as HttpURLConnection
        urlConnection.requestMethod = method

        // Add query parameters to the URL
        // Note: This is just a simple example. You may need to handle query parameters differently based on your requirements.
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        // Optionally, set other request headers here

        headers.forEach { (key, value) ->
            urlConnection.setRequestProperty(key, value)
        }

        // Optionally, set request body here if it's not null
        body?.let { requestBody ->
            urlConnection.doOutput = true
            val outputStream = urlConnection.outputStream
            outputStream.write(requestBody.toByteArray())
            outputStream.flush()
            outputStream.close()
        }

        // Return the HttpURLConnection object
        return urlConnection
    }

    /**
     * Parses the HTTP response.
     *
     * @param connection The HttpURLConnection object representing the connection.
     * @return The Response object representing the parsed response.
     */
    private fun parseResponse(connection: HttpURLConnection): Response {
        // Get the response data, status code, message, and headers from the connection
        val data = connection.inputStream.readBytes()
        val status = connection.responseCode
        val message = connection.responseMessage
        val headers = connection.headerFields
        // Close the connection
        connection.disconnect()

        // Create and return the Response object
        return Response(data, status, message, headers)
    }
}
