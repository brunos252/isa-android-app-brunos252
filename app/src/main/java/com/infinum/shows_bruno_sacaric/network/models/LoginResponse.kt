package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "data")
    val data: LoginResponseData?,

    @Transient
    var isSuccessful: Boolean = true
)

@JsonClass(generateAdapter = true)
data class LoginResponseData(
    @Json(name = "token")
    val token: String
)

