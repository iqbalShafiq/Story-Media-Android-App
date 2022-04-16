package space.iqbalsyafiq.storymedia

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import space.iqbalsyafiq.storymedia.model.DataResponse
import space.iqbalsyafiq.storymedia.model.request.LoginRequest
import space.iqbalsyafiq.storymedia.model.request.RegisterRequest
import space.iqbalsyafiq.storymedia.repository.api.ApiService

class ApiServiceFake : ApiService {
    override suspend fun registerUser(requestBody: RegisterRequest): DataResponse {
        println("Hai from fake")
        return StoryDataDummy.generateSuccessRegisterResponse()
    }

    override suspend fun loginUser(requestBody: LoginRequest): DataResponse {
        return StoryDataDummy.generateSuccessLoginResponse()
    }

    override suspend fun getAllStories(authKey: String, page: Int?, size: Int?): DataResponse {
        TODO("Not yet implemented")
    }

    override fun uploadStory(
        authKey: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody,
        lng: RequestBody
    ): Call<DataResponse> {
        TODO("Not yet implemented")
    }
}