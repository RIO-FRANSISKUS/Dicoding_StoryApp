package com.example.storyapp.response

data class StoryResponse(
    val error: Boolean,
    val listStory: List<Story>,
    val message: String
)