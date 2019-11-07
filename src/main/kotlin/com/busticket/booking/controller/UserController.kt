package com.busticket.booking.controller

import com.busticket.booking.API_PREFIX
import com.busticket.booking.entity.User
import com.busticket.booking.enum.role.ROLE_MANAGER_USER
import com.busticket.booking.lib.auth.ReqUser
import com.busticket.booking.lib.exception.ExecuteException
import com.busticket.booking.lib.rest.RestResponseService
import com.busticket.booking.request.UserRequest
import com.busticket.booking.service.interfaces.DtoBuilderService
import com.busticket.booking.service.interfaces.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping(value = ["$API_PREFIX/users"])
@CrossOrigin
@Secured(ROLE_MANAGER_USER)
class UserController @Autowired constructor(
        private val dtoBuilder: DtoBuilderService,
        private val responseService: RestResponseService,
        private val userService: UserService
) {
    @PostMapping(value = ["/create"])
    fun createUser(@RequestBody @Valid dto: UserRequest): ResponseEntity<Any> {
        return responseService.restSuccess(dtoBuilder.buildUserDto(userService.create(dto)))
    }

    @GetMapping(value = ["/single"])
    fun singleUser(@RequestParam("id") id: Int): ResponseEntity<Any> {
        return responseService.restSuccess(dtoBuilder.buildUserDto(userService.singleById(id)))
    }

    @PostMapping(value = ["/edit"])
    fun editUser(@RequestBody @Valid dto: UserRequest): ResponseEntity<Any> {
        return dto.id?.let { it -> responseService.restSuccess(dtoBuilder.buildUserDto(userService.edit(it, dto))) }
                ?: throw ExecuteException("must_send_an_id")
    }

    @GetMapping(value = ["delete"])
    fun deleteUser(@RequestParam("id") id: Int): ResponseEntity<Any> {
        return responseService.restSuccess(dtoBuilder.buildUserDto(userService.delete(id)))
    }
}
