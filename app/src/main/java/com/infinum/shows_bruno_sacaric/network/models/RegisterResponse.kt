package com.infinum.shows_bruno_sacaric.network.models

import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(
    @Json(name = "data")
    val data: RegisterResponseData?,

    @Transient
    var responseCode: ResponseCode = ResponseCode.CODE_EMPTY
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

