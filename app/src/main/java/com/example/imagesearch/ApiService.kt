package com.example.imagesearch

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

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

)

interface ApiService {
    @Headers("Authorization: KakaoAK d313d114a3bc4acba390e0941d76b26a")
    @GET("v2/search/image")
    fun searchImages(@Query("query") query: String): Call<SearchResponse>
}
