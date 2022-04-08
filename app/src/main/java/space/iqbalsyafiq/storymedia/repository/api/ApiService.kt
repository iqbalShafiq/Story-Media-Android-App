package space.iqbalsyafiq.storymedia.repository.api

import retrofit2.Call
import retrofit2.http.*
import space.iqbalsyafiq.storymedia.model.DataResponse
import space.iqbalsyafiq.storymedia.model.request.LoginRequest
import space.iqbalsyafiq.storymedia.model.request.RegisterRequest

interface ApiService {
    @POST("register")
    fun registerUser(
        @Body() requestBody: RegisterRequest
    ): Call<DataResponse>

    @POST("login")
    fun loginUser(
        @Body() requestBody: LoginRequest
    ): Call<DataResponse>

    @GET("stories")
    @Headers("Authorization: Bearer ${ApiConstant.API_TOKEN}") // @TODO API TOKEN
    fun getAllStories(
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 10
    ): Call<DataResponse>
}