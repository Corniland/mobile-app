package com.corniland.mobile.data.model

data class Project(
    val id: String,
    val title: String,
    val short_description: String,
    val description: String,
    val status: String,
    val cover_picture_url: String,
    val published: Boolean,
    val owner: String,
    val members: List<String> = listOf(),
    val likes: Int
) {
    companion object {
        fun getMock() = Project(
            id = "5f6e96852f1bc609ad3c55de",
            title = "Subnautica 2.0",
            short_description = "Yet another unfinished project for our lord LSD",
            description =
            "This project is really not gonna be terminated but we do have some hope left. " +
                    "LSD is being stronger at art and dora is joining him. Maybe this project will " +
                    "see the light.",
            status = "Not even started",
            cover_picture_url = "https://www.thqnordic.com/sites/default/files/games/slider/backgrounds/biomutant_slider.jpg",
            published = false,
            owner = "507f191e810c19729de860ea",
            members = listOf("5f6e97cf4caa0215b409a84b", "5099803df3f4948bd2f98391"),
            likes = 42
        )
    }
}

data class CreateProjectRequest(
    var title: String
)

data class UpdateProjectRequest(
    var title: String,
    var short_description: String,
    var description: String,
    var status: String,
    var cover_picture_url: String,
    var published: Boolean
)