package com.busticket.booking.dto

import com.busticket.booking.entity.VehicleCategory

class VehicleDto(
        val id: Int,
        val name: String,
        val plate: String,
        val color: String,
        val createdAt: Long,
        val updatedAt: Long,
        val category_id: Int,
        val createdAtStr: String,
        val updatedAtStr: String,
        val status: Int
) {
}