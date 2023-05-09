package com.example.storyapp.response

data class DetailStoryResponse(
    val error: Boolean,
    val message: String,
    val story: Story
)