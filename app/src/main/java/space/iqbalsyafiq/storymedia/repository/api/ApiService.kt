package space.iqbalsyafiq.storymedia.repository.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import space.iqbalsyafiq.storymedia.model.DataResponse
import space.iqbalsyafiq.storymedia.model.request.LoginRequest
import space.iqbalsyafiq.storymedia.model.request.RegisterRequest

interface ApiService {
    @POST("register")
    fun registerUser(
        @Body requestBody: RegisterRequest
    ): Call<DataResponse>

    @POST("login")
    fun loginUser(
        @Body requestBody: LoginRequest
    ): Call<DataResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") authKey: String,
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 10
    ): DataResponse

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") authKey: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lng: RequestBody
    ): Call<DataResponse>
}