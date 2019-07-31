package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(
    @Json(name = "data")
    val data: RegisterResponseData?,

    @Transient
    var isSuccessful: Boolean = true
)

@JsonClass(generateAdapter = true)
data class RegisterResponseData(
    @Json(name = "type")
    val type: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "_id")
    val id: String
)

