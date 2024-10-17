package io.hhplus.concert.presentation.user.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class PointRequest @JsonCreator constructor(
    @JsonProperty("point") val point: Long
)