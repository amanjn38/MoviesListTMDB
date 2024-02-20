package com.finance.networksdk

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

data class Response(
    val data: ByteArray,
    val status: Int,
    val message: String,
    val respHeaders: Map<String?, List<String>>
) {
    @Throws(JSONException::class)
    fun asJSONObject(): JSONObject {
        val str = asString()
        return if (str.isEmpty()) JSONObject() else JSONObject(str)
    }

    @Throws(JSONException::class)
    fun asJSONArray(): JSONArray {
        val str = asString()
        return if (str.isEmpty()) JSONArray() else JSONArray(str)
    }

    private fun asString(): String {
        return HttpUtils.asString(data)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Response

        if (!data.contentEquals(other.data)) return false
        if (status != other.status) return false
        if (message != other.message) return false
        return respHeaders == other.respHeaders
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + status
        result = 31 * result + message.hashCode()
        result = 31 * result + respHeaders.hashCode()
        return result
    }
}
