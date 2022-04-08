package space.iqbalsyafiq.storymedia.model

data class DataResponse(
    val error: Boolean?,
    val listStory: List<Story>?,
    val loginResult: LoginResult?,
    val message: String?
)