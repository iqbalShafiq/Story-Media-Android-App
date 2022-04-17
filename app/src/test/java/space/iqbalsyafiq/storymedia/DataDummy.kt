package space.iqbalsyafiq.storymedia

import space.iqbalsyafiq.storymedia.model.DataResponse
import space.iqbalsyafiq.storymedia.model.LoginResult
import space.iqbalsyafiq.storymedia.model.Story

object DataDummy {
    fun generateStoryDataDummy(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                "2022-04-14T15:06:34.194Z",
                "description",
                "story-iiRiP6kuER8wyHxy",
                -6.6166598,
                110.685986,
                "Kamisato Ayaka",
                "https://story-api.dicoding.dev/images/stories/photos-1649948794192_xcTM729C.jpg"
            )

            items.add(story)
        }
        return items
    }

    fun generateSuccessRegisterResponse(): DataResponse {
        return DataResponse(
            error = false,
            message = "User created",
            listStory = null,
            loginResult = null
        )
    }

    fun generateSuccessLoginResponse(): DataResponse {
        return DataResponse(
            error = false,
            message = "success",
            listStory = null,
            loginResult = LoginResult(
                name = "iqbal",
                token = "TOKEN",
                userId = "lANSKoS0Ak7X4hXR"
            )
        )
    }

    fun generateCreateStoryResponse(): DataResponse {
        return DataResponse(
            error = false,
            message = "Story created successfully",
            listStory = null,
            loginResult = null
        )
    }
}