package com.infinum.shows_bruno_sacaric.network.models

import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "data")
    val data: LoginResponseData?,

    @Transient
    var responseCode: ResponseCode = ResponseCode.CODE_EMPTY
)

@JsonClass(generateAdapter = true)
data class LoginResponseData(
    @Json(name = "token")
    val token: String
)

