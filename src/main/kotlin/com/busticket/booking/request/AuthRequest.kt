package com.busticket.booking.request

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class AuthRequest(
        @field:NotEmpty(message = "can_not_be_empty")
        var email: String,
        @field:NotEmpty(message = "can_not_be_empty")
        var password: String
)