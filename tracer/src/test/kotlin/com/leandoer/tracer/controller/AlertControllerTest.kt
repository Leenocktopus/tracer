package com.leandoer.tracer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leandoer.tracer.exception.ErrorType.VALIDATION_EXCEPTION
import com.leandoer.tracer.model.dto.alert.AlertFilter
import com.leandoer.tracer.model.dto.alert.GetAlertDto
import com.leandoer.tracer.model.dto.alert.PostAlertDto
import com.leandoer.tracer.model.dto.alert.PutAlertDto
import com.leandoer.tracer.service.AlertService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import liquibase.pro.packaged.al
import liquibase.pro.packaged.u
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put


@WebMvcTest(AlertController::class)
@AutoConfigureMockMvc
class AlertControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var alertService: AlertService

    @Autowired
    private lateinit var mapper: ObjectMapper


    @Test
    fun `Test findAll`() {
        val pageSize = 10
        val pageable = PageRequest.of(0, pageSize)
        val alertFilter = AlertFilter(2)
        val alerts = listOf(
            GetAlertDto(
                1,
                "alert #1",
                listOf(GetAlertDto.AlertContactDto(1, "a@gmail.com"), GetAlertDto.AlertContactDto(2, "b@gmail.com")),
                listOf()
            ),
            GetAlertDto(
                2,
                "alert #2",
                listOf(GetAlertDto.AlertContactDto(3, "c@gmail.com"), GetAlertDto.AlertContactDto(1, "a@gmail.com")),
                listOf()
            )
        )
        every {
            alertService.findAll(alertFilter, pageable)
        } returns PageImpl(alerts, pageable, alerts.size.toLong())

        mockMvc.get("/alerts?labelId=${alertFilter.labelId}")
            .andExpect {
                status { OK }
                jsonPath("$.totalElements") { value(alerts.size) }
                jsonPath("$.totalPages") { value(alerts.size / pageSize + 1) }
                jsonPath("$.content[0].id") { value(alerts[0].id) }
                jsonPath("$.content[0].name") { value(alerts[0].name) }
                jsonPath("$.content[0].contacts[0].id") { value(alerts[0].contacts[0].id) }
                jsonPath("$.content[0].contacts[0].email") { value(alerts[0].contacts[0].email) }
                jsonPath("$.content[0].contacts[1].id") { value(alerts[0].contacts[1].id) }
                jsonPath("$.content[0].contacts[1].email") { value(alerts[0].contacts[1].email) }
                jsonPath("$.content[1].id") { value(alerts[1].id) }
                jsonPath("$.content[1].name") { value(alerts[1].name) }
                jsonPath("$.content[1].contacts[0].id") { value(alerts[1].contacts[0].id) }
                jsonPath("$.content[1].contacts[0].email") { value(alerts[1].contacts[0].email) }
                jsonPath("$.content[1].contacts[1].id") { value(alerts[1].contacts[1].id) }
                jsonPath("$.content[1].contacts[1].email") { value(alerts[1].contacts[1].email) }
            }
        verify { alertService.findAll(alertFilter, pageable) }
    }

    @Test
    fun `Test findAll with invalid filter parameter`() {
        val alertFilter = AlertFilter(-100)

        mockMvc.get("/alerts?labelId=${alertFilter.labelId}")
            .andExpect {
                status { VALIDATION_EXCEPTION }
                jsonPath("$.error") { value(VALIDATION_EXCEPTION.name) }
                jsonPath("$.code") { value(VALIDATION_EXCEPTION.code) }
                jsonPath("$.message") { value("must be greater than or equal to 1") }
                jsonPath("$.metadata.field") { value("labelId") }
                jsonPath("$.metadata.value") { value(alertFilter.labelId) }
            }
    }

    @Test
    fun `Test findById`() {
        val alertId = 3
        val alert = GetAlertDto(
            alertId,
            "alert #1",
            listOf(GetAlertDto.AlertContactDto(1, "a@gmail.com"), GetAlertDto.AlertContactDto(2, "b@gmail.com")),
            listOf(GetAlertDto.AlertLabelDto(1, "google"), GetAlertDto.AlertLabelDto(2, "decimal"))
        )
        every {
            alertService.findById(alertId)
        } returns alert


        mockMvc.get("/alerts/{id}", alertId)
            .andExpect {
                status { OK }
                jsonPath("$.id") { value(alert.id) }
                jsonPath("$.name") { value(alert.name) }
                jsonPath("$.contacts[0].id") { value(alert.contacts[0].id) }
                jsonPath("$.contacts[0].email") { value(alert.contacts[0].email) }
                jsonPath("$.contacts[1].id") { value(alert.contacts[1].id) }
                jsonPath("$.contacts[1].email") { value(alert.contacts[1].email) }
                jsonPath("$.labels[0].id") { value(alert.labels[0].id) }
                jsonPath("$.labels[0].label") { value(alert.labels[0].label) }
                jsonPath("$.labels[1].id") { value(alert.labels[1].id) }
                jsonPath("$.labels[1].label") { value(alert.labels[1].label) }
            }

        verify { alertService.findById(alertId) }
    }

    @Test
    fun `Test findById with negative ID`() {
        val alertId = -9
        mockMvc.get("/alerts/{id}", alertId)
            .andExpect {
                status { BAD_REQUEST }
                jsonPath("$.error") { value(VALIDATION_EXCEPTION.name) }
                jsonPath("$.code") { value(4) }
                jsonPath("$.message") { value("must be greater than or equal to 1") }
                jsonPath("$.metadata.field") { value("findById.id") }
                jsonPath("$.metadata.value") { value(alertId) }
            }

    }

    @Test
    fun `Test create`() {
        val alert = PostAlertDto(
            "alert",
            listOf(PostAlertDto.AlertContactDto("ldap@gmail.com"), PostAlertDto.AlertContactDto("hello@ukr.net"))
        )
        val createdAlert = GetAlertDto(
            1, alert.name,
            listOf(
                GetAlertDto.AlertContactDto(1, alert.contacts[0].email),
                GetAlertDto.AlertContactDto(2, alert.contacts[1].email)
            ),
            listOf()
        )
        every {
            alertService.create(alert)
        } returns createdAlert

        mockMvc.post("/alerts") {
            content = mapper.writeValueAsString(alert)
            contentType = APPLICATION_JSON
        }
            .andExpect {
                status { CREATED }
                jsonPath("$.id") { value(createdAlert.id) }
                jsonPath("$.name") { value(createdAlert.name) }
                jsonPath("$.contacts[0].id") { value(createdAlert.contacts[0].id) }
                jsonPath("$.contacts[0].email") { value(alert.contacts[0].email) }
                jsonPath("$.contacts[1].id") { value(createdAlert.contacts[1].id) }
                jsonPath("$.contacts[1].email") { value(alert.contacts[1].email) }
            }

        verify { alertService.create(alert) }
    }

    @Test
    fun `Test update`() {
        val alertId = 1
        val alert = PutAlertDto(
            listOf(PutAlertDto.AlertContactDto("ldap@gmail.com"), PutAlertDto.AlertContactDto("hello@ukr.net")),
            listOf(1, 2)
        )

        val updatedAlert = GetAlertDto(
            1, "google-alert",
            listOf(
                GetAlertDto.AlertContactDto(1, alert.contacts[0].email),
                GetAlertDto.AlertContactDto(2, alert.contacts[1].email)
            ),
            listOf(
                GetAlertDto.AlertLabelDto(1, "google"),
                GetAlertDto.AlertLabelDto(2, "binary")
            )
        )
        every {
            alertService.update(alertId, alert)
        } returns updatedAlert

        mockMvc.put("/alerts/${alertId}") {
            content = mapper.writeValueAsString(alert)
            contentType = APPLICATION_JSON
        }
            .andExpect {
                status { CREATED }
                jsonPath("$.id") { value(updatedAlert.id) }
                jsonPath("$.name") { value(updatedAlert.name) }
                jsonPath("$.contacts[0].id") { value(updatedAlert.contacts[0].id) }
                jsonPath("$.contacts[0].email") { value(updatedAlert.contacts[0].email) }
                jsonPath("$.contacts[1].id") { value(updatedAlert.contacts[1].id) }
                jsonPath("$.contacts[1].email") { value(updatedAlert.contacts[1].email) }
                jsonPath("$.labels[0].id") { value(updatedAlert.labels[0].id) }
                jsonPath("$.labels[0].label") { value(updatedAlert.labels[0].label) }
                jsonPath("$.labels[1].id") { value(updatedAlert.labels[1].id) }
                jsonPath("$.labels[1].label") { value(updatedAlert.labels[1].label) }
            }

        verify { alertService.update(alertId, alert) }
    }

    @Test
    fun `Test delete with negative ID`() {
        val alertId = -9
        mockMvc.delete("/alerts/{id}", alertId)
            .andExpect {
                status { BAD_REQUEST }
                jsonPath("$.error") { value(VALIDATION_EXCEPTION.name) }
                jsonPath("$.code") { value(4) }
                jsonPath("$.message") { value("must be greater than or equal to 1") }
                jsonPath("$.metadata.field") { value("delete.id") }
                jsonPath("$.metadata.value") { value(alertId) }
            }

    }

    @Test
    fun `Test delete`() {
        val id = 1

        every {
            alertService.delete(id)
        } returns Unit

        mockMvc.delete("/alerts/${id}")
            .andExpect {
                status { HttpStatus.NO_CONTENT }
            }
        verify { alertService.delete(id) }

    }


}