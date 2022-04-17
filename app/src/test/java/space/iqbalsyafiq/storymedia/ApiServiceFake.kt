package space.iqbalsyafiq.storymedia

import okhttp3.MultipartBody
import okhttp3.RequestBody
import space.iqbalsyafiq.storymedia.model.DataResponse
import space.iqbalsyafiq.storymedia.model.request.LoginRequest
import space.iqbalsyafiq.storymedia.model.request.RegisterRequest
import space.iqbalsyafiq.storymedia.repository.api.ApiService

class ApiServiceFake : ApiService {
    override suspend fun registerUser(requestBody: RegisterRequest): DataResponse {
        println("Hai from fake")
        return DataDummy.generateSuccessRegisterResponse()
    }

    override suspend fun loginUser(requestBody: LoginRequest): DataResponse {
        return DataDummy.generateSuccessLoginResponse()
    }

    override suspend fun getAllStories(authKey: String, page: Int?, size: Int?): DataResponse {
        TODO("Not yet implemented")
    }

    override suspend fun uploadStory(
        authKey: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody,
        lng: RequestBody
    ): DataResponse {
        return DataDummy.generateCreateStoryResponse()
    }
}