package br.com.hussan.enjoeitest.domain

import java.io.Serializable

data class Photo(
    val crop: String,
    val gravity: String,
    val publicId: String
) : Serializable
