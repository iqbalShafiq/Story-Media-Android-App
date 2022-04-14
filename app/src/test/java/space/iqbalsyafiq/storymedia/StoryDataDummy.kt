package space.iqbalsyafiq.storymedia

import space.iqbalsyafiq.storymedia.model.Story

object StoryDataDummy {
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
}