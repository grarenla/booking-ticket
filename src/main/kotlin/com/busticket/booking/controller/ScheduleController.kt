package com.busticket.booking.controller

import com.busticket.booking.API_PREFIX
import com.busticket.booking.dto.PaginationDto
import com.busticket.booking.entity.User
import com.busticket.booking.enum.role.ROLE_MANAGER_SCHEDULE
import com.busticket.booking.lib.auth.ReqUser
import com.busticket.booking.lib.rest.RestResponseService
import com.busticket.booking.request.ScheduleRequest
import com.busticket.booking.request.ScheduleTemplateRequest
import com.busticket.booking.service.interfaces.DtoBuilderService
import com.busticket.booking.service.interfaces.ScheduleService
import org.hibernate.annotations.Parameter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.streams.toList

@RestController
@CrossOrigin
@RequestMapping(value = ["$API_PREFIX/schedules"])
class ScheduleController @Autowired constructor(
        private val scheduleService: ScheduleService,
        private val dtoBuilder: DtoBuilderService,
        private val restResponse: RestResponseService
) {
    @PostMapping
    @Secured(ROLE_MANAGER_SCHEDULE)
    fun createSchedule(@RequestBody @Valid dto: ScheduleRequest, @ReqUser user: User): ResponseEntity<Any> {
        val schedule = scheduleService.create(user, dto)
        val result = dtoBuilder.buildScheduleDto(schedule)
        return restResponse.restSuccess(result)
    }

    @GetMapping(value = ["/{id}"])
    @Secured(ROLE_MANAGER_SCHEDULE)
    fun getScheduleById(@PathVariable("id") id: Int): ResponseEntity<Any> {
        val schedule = scheduleService.singleById(id)
        val result = dtoBuilder.buildScheduleDto(schedule)
        return restResponse.restSuccess(result)
    }

    @DeleteMapping(value = ["/{id}"])
    @Secured(ROLE_MANAGER_SCHEDULE)
    fun deleteSchedule(@PathVariable("id") id: Int): ResponseEntity<Any> {
        val schedule = scheduleService.delete(id)
        val result = dtoBuilder.buildScheduleDto(schedule)
        return restResponse.restSuccess(result)
    }

    @PutMapping(value = ["/{id}"])
    @Secured(ROLE_MANAGER_SCHEDULE)
    fun editSchedule(@PathVariable("id") id: Int, @RequestBody @Valid dto: ScheduleRequest, @ReqUser user: User): ResponseEntity<Any> {
        val schedule = scheduleService.edit(user, id, dto)
        val result = dtoBuilder.buildScheduleDto(schedule)
        return restResponse.restSuccess(result)
    }

    @GetMapping(value = ["/search"])
    fun search(@RequestParam("voyage_id") voyageId: Int? = null,
               @RequestParam("date") date: Long? = null,
               @RequestParam("driver_id") driverId: Int? = null,
               @RequestParam("schedule_template_id") scheduleTemplateId: Int? = null,
               @RequestParam("vehicle_category_id") vehicleCategoryId: Int? = null,
               @RequestParam("status") status: Int? = null,
               @RequestParam("page") page: Int? = null,
               @RequestParam("limit") limit: Int? = null
    ): ResponseEntity<Any> {
        val realPage = page ?: 0
        val realLimit = limit ?: 20
        val schedules = scheduleService.search(voyageId, date, driverId, scheduleTemplateId, vehicleCategoryId, status, realPage, realLimit)
        val result = schedules.content.map { dtoBuilder.buildScheduleDto(it) }
        val pagination = PaginationDto(schedules, realPage, realLimit)
        return restResponse.restSuccess(result, pagination)
    }
}
