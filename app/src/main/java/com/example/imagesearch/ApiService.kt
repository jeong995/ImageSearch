package com.example.imagesearch

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.io.Serializable

data class SearchResponse(
    val documents: List<ImageResult>
)

data class ImageResult(
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("display_sitename")
    val displaySiteName: String,
    @SerializedName("datetime")
    val datetime: String,
    var check: Boolean = false

): Serializable

interface ApiService {
    @Headers("")
    @GET("v2/search/image")
    fun searchImages(@Query("query") query: String): Call<SearchResponse>
}
