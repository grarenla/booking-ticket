package com.busticket.booking.controller

import com.busticket.booking.API_PREFIX
import com.busticket.booking.enum.role.ROLE_MANAGER_POLICY
import com.busticket.booking.lib.rest.RestResponseService
import com.busticket.booking.repository.user.UserRepository
import com.busticket.booking.request.OutJoinPolicyRequest
import com.busticket.booking.request.PolicyRequest
import com.busticket.booking.service.interfaces.DtoBuilderService
import com.busticket.booking.service.interfaces.PolicyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@CrossOrigin
@RequestMapping(value = ["$API_PREFIX/policies"])
@Secured(ROLE_MANAGER_POLICY)
class PolicyController @Autowired constructor(
        private val policyService: PolicyService,
        private val dtoBuilder: DtoBuilderService,
        private val restResponse: RestResponseService
) {
    @PostMapping
    fun createPolicy(@RequestBody @Valid dto: PolicyRequest): ResponseEntity<Any> {
        return restResponse.restSuccess(dtoBuilder.buildPolicyDto(policyService.create(dto)))
    }

    @GetMapping
    fun listPolicies(): ResponseEntity<Any> {
        return restResponse.restSuccess(policyService.findAllActiveItems().map { dtoBuilder.buildPolicyDto(it) })
    }

    @GetMapping(value = ["/roles"])
    fun listRoles(): ResponseEntity<Any> {
        return restResponse.restSuccess(policyService.listRoles().map { dtoBuilder.buildRoleDto(it) })
    }

    @PostMapping(value = ["/join/{id}"])
    fun joinPolicy(@RequestBody @Valid dto: OutJoinPolicyRequest, @PathVariable("id") id: Int): ResponseEntity<Any> {
        return restResponse.restSuccess(dtoBuilder.buildUserDto(policyService.userJoinPolicy(dto.userId!!, id)))
    }

    @PostMapping(value = ["/out"])
    fun outPolicy(@RequestBody @Valid dto: OutJoinPolicyRequest): ResponseEntity<Any> {
        return restResponse.restSuccess(dtoBuilder.buildUserDto(policyService.userOutPolicy(dto.userId!!)))
    }

    @PutMapping(value = ["/{id}"])
    fun editPolicy(@PathVariable("id") id: Int, @RequestBody @Valid dto: PolicyRequest): ResponseEntity<Any> {
        return restResponse.restSuccess(dtoBuilder.buildPolicyDto(policyService.edit(id, dto)))
    }

    @DeleteMapping(value = ["/{id}"])
    fun deletePolicy(@PathVariable("id") id: Int): ResponseEntity<Any> {
        return restResponse.restSuccess(dtoBuilder.buildPolicyDto(policyService.delete(id)))
    }

    @GetMapping(value = ["/{id}"])
    fun getPolicy(@PathVariable("id") id: Int): ResponseEntity<Any> {
        return restResponse.restSuccess(dtoBuilder.buildPolicyDto(policyService.singleById(id)))
    }

    @GetMapping(value = ["/{id}/users"])
    fun listUsersOfPolicy(@PathVariable("id") id: Int): ResponseEntity<Any> {
        return restResponse.restSuccess(policyService.usersOfPolicy(id).map { dtoBuilder.buildUserDto(it) });
    }
}
