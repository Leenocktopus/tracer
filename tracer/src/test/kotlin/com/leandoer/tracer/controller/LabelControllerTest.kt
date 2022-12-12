package com.leandoer.tracer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leandoer.tracer.exception.ErrorType.MEDIA_TYPE_NOT_ACCEPTABLE
import com.leandoer.tracer.exception.ErrorType.MEDIA_TYPE_NOT_SUPPORTED
import com.leandoer.tracer.exception.ErrorType.MESSAGE_NOT_READABLE_EXCEPTION
import com.leandoer.tracer.exception.ErrorType.VALIDATION_EXCEPTION
import com.leandoer.tracer.model.dto.label.GetLabelDto
import com.leandoer.tracer.model.dto.label.PostLabelDto
import com.leandoer.tracer.model.dto.label.PutLabelDto
import com.leandoer.tracer.model.entity.Label_
import com.leandoer.tracer.model.entity.Package.NIST
import com.leandoer.tracer.model.entity.TestType.FREQUENCY
import com.leandoer.tracer.service.LabelService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_ACCEPTABLE
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_XML
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put


@WebMvcTest(LabelController::class)
@AutoConfigureMockMvc
class LabelControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var labelService: LabelService

    @Autowired
    private lateinit var mapper: ObjectMapper


    @Test
    fun `Test findAll`() {
        val pageSize = 10
        val pageable = PageRequest.of(0, pageSize, Sort.by(Label_.ID))
        val labels = listOf(
            GetLabelDto(1, "google"),
            GetLabelDto(2, "frontend"),
            GetLabelDto(3, "backend"),
            GetLabelDto(4, "binary")
        )
        every {
            labelService.findAll(pageable)
        } returns PageImpl(labels, pageable, labels.size.toLong())

        mockMvc.get("/labels")
            .andExpect {
                status { OK }
                jsonPath("$.totalElements") { value(labels.size) }
                jsonPath("$.totalPages") { value(labels.size / pageSize + 1) }
                jsonPath("$.content[0].id") { value(labels[0].id) }
                jsonPath("$.content[0].label") { value(labels[0].label) }
                jsonPath("$.content[1].id") { value(labels[1].id) }
                jsonPath("$.content[1].label") { value(labels[1].label) }
                jsonPath("$.content[2].id") { value(labels[2].id) }
                jsonPath("$.content[2].label") { value(labels[2].label) }
                jsonPath("$.content[3].id") { value(labels[3].id) }
                jsonPath("$.content[3].label") { value(labels[3].label) }
            }
        verify { labelService.findAll(pageable) }
    }

    @Test
    fun `Test findAll with wrong accept type`() {
        val pageable = PageRequest.of(0, 10, Sort.by(Label_.ID))
        every {
            labelService.findAll(pageable)
        } returns PageImpl(
            listOf(
                GetLabelDto(1, "google"),
                GetLabelDto(2, "frontend"),
                GetLabelDto(3, "backend"),
                GetLabelDto(4, "binary")
            ),
            pageable,
            10
        )

        mockMvc.get("/labels") {
            accept = TEXT_XML
        }
            .andExpect {
                status { NOT_ACCEPTABLE }
                jsonPath("$.error") { value(MEDIA_TYPE_NOT_ACCEPTABLE.name) }
                jsonPath("$.code") { value(MEDIA_TYPE_NOT_ACCEPTABLE.code) }
                jsonPath("$.message") { value("media type not acceptable") }
                jsonPath("$.metadata") { isEmpty() }
            }
        verify { labelService.findAll(pageable) }
    }

    @Test
    fun `Test findById`() {
        val id = 1
        val label = GetLabelDto(id, "google", listOf(GetLabelDto.LabelTestDto(1, FREQUENCY.title, FREQUENCY, NIST)))

        every {
            labelService.findById(id)
        } returns label

        mockMvc.get("/labels/${id}")
            .andExpect {
                status { OK }
                jsonPath("$.id") { value(label.id) }
                jsonPath("$.label") { value(label.label) }
                jsonPath("$.tests[0].id") { value(label.tests[0].id) }
                jsonPath("$.tests[0].title") { value(label.tests[0].title) }
                jsonPath("$.tests[0].name") { value(label.tests[0].name.name) }
                jsonPath("$.tests[0].type") { value(label.tests[0].type.name) }
            }
        verify { labelService.findById(id) }
    }

    @Test
    fun `Test findById with negative ID`() {
        val labelId = -10
        mockMvc.get("/labels/{id}", labelId)
            .andExpect {
                status { BAD_REQUEST }
                jsonPath("$.error") { value(VALIDATION_EXCEPTION.name) }
                jsonPath("$.code") { value(VALIDATION_EXCEPTION.code) }
                jsonPath("$.message") { value("must be greater than or equal to 1") }
                jsonPath("$.metadata.field") { value("findById.id") }
                jsonPath("$.metadata.value") { value(labelId) }
            }
    }

    @Test
    fun `Test findById with ID of different type`() {
        mockMvc.get("/labels/true")
            .andExpect {
                status { BAD_REQUEST }
                jsonPath("$.error") { value(VALIDATION_EXCEPTION.name) }
                jsonPath("$.code") { value(VALIDATION_EXCEPTION.code) }
                jsonPath("$.message") { value("exception during number parse or conversion") }
                jsonPath("$.metadata.message") { value("For input string: \"true\"") }
            }
    }

    @Test
    fun `Test findById with overflowing ID`() {
        mockMvc.get("/labels/{id}", 99999999999999999)
            .andExpect {
                status { BAD_REQUEST }
                jsonPath("$.error") { value(VALIDATION_EXCEPTION.name) }
                jsonPath("$.code") { value(VALIDATION_EXCEPTION.code) }
                jsonPath("$.message") { value("exception during number parse or conversion") }
                jsonPath("$.metadata.message") { value("For input string: \"99999999999999999\"") }
            }
    }

    @Test
    fun `Test create`() {
        val label = PostLabelDto("google")
        val createdLabel = GetLabelDto(1, "google")

        every {
            labelService.create(label)
        } returns createdLabel

        mockMvc.post("/labels") {
            content = mapper.writeValueAsString(label)
            contentType = APPLICATION_JSON
        }
            .andExpect {
                status { CREATED }
                jsonPath("$.id") { value(createdLabel.id) }
                jsonPath("$.label") { value(label.label) }
            }
        verify { labelService.create(label) }
    }

    @Test
    fun `Test create with empty request body`() {
        mockMvc.post("/labels")
            .andExpect {
                status { BAD_REQUEST }
                jsonPath("$.error") { value(MESSAGE_NOT_READABLE_EXCEPTION.name) }
                jsonPath("$.code") { value(MESSAGE_NOT_READABLE_EXCEPTION.code) }
                jsonPath("$.message") { value("message not readable") }
                jsonPath("$.metadata") { isEmpty() }
            }
    }

    @Test
    fun `Test create with xml request body`() {
        mockMvc.post("/labels") {
            contentType = TEXT_XML
            content = "<text> <para>hello world</para> </text>"
        }
            .andExpect {
                status { UNSUPPORTED_MEDIA_TYPE }
                jsonPath("$.error") { value(MEDIA_TYPE_NOT_SUPPORTED.name) }
                jsonPath("$.code") { value(MEDIA_TYPE_NOT_SUPPORTED.code) }
                jsonPath("$.message") { value("media type not supported") }
                jsonPath("$.metadata") { isEmpty() }
            }
    }


    @Test
    fun `Test create with invalid json format`() {
        mockMvc.post("/labels") {
            contentType = APPLICATION_JSON
            content = """[{ "label": "google" }]"""

        }.andExpect {
            status { BAD_REQUEST }
            jsonPath("$.error") { value(MESSAGE_NOT_READABLE_EXCEPTION.name) }
            jsonPath("$.code") { value(MESSAGE_NOT_READABLE_EXCEPTION.code) }
            jsonPath("$.message") { value("mismatched input exception") }
            jsonPath("$.metadata") { isEmpty() }
        }
    }

    @Test
    fun `Test create with invalid symbols in label`() {
        val label = PostLabelDto("*")
        mockMvc.post("/labels") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(label)
        }.andExpect {
            status { BAD_REQUEST }
            jsonPath("$.error") { value(VALIDATION_EXCEPTION.name) }
            jsonPath("$.code") { value(VALIDATION_EXCEPTION.code) }
            jsonPath("$.message") { value("must match \"^[a-zA-Z0-9]+$\"") }
            jsonPath("$.metadata.field") { value("label") }
            jsonPath("$.metadata.value") { value(label.label) }

        }
    }

    @Test
    fun `Test create with long label`() {
        val label = PostLabelDto("verylongtextthatshouldbebiggerthanallowedlengthhopefully")
        mockMvc.post("/labels") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(label)
        }.andExpect {
            status { BAD_REQUEST }
            jsonPath("$.error") { value(VALIDATION_EXCEPTION.name) }
            jsonPath("$.code") { value(VALIDATION_EXCEPTION.code) }
            jsonPath("$.message") { value("size must be between 1 and 50") }
            jsonPath("$.metadata.field") { value("label") }
            jsonPath("$.metadata.value") { value(label.label) }
        }
    }

    @Test
    fun `Test create with null label`() {
        mockMvc.post("/labels") {
            contentType = APPLICATION_JSON
            content = """{ "label": null }"""
        }.andExpect {
            status { BAD_REQUEST }
            jsonPath("$.error") { value(VALIDATION_EXCEPTION.name) }
            jsonPath("$.code") { value(VALIDATION_EXCEPTION.code) }
            jsonPath("$.message") { value("mapping exception for field") }
            jsonPath("$.metadata.field") { value("label") }
            jsonPath("$.metadata.message") { value("must not be null") }
        }
    }

    @Test
    fun `Test update`() {
        val id = 1
        val label = PutLabelDto(listOf(1))
        val updatedLabel =
            GetLabelDto(1, "google", listOf(GetLabelDto.LabelTestDto(1, FREQUENCY.title, FREQUENCY, NIST)))

        every {
            labelService.update(id, label)
        } returns updatedLabel

        mockMvc.put("/labels/${id}") {
            content = mapper.writeValueAsString(label)
            contentType = APPLICATION_JSON
        }
            .andExpect {
                status { OK }
                jsonPath("$.id") { value(updatedLabel.id) }
                jsonPath("$.label") { value(updatedLabel.label) }
                jsonPath("$.tests[0].id") { value(updatedLabel.tests[0].id) }
                jsonPath("$.tests[0].title") { value(updatedLabel.tests[0].title) }
                jsonPath("$.tests[0].name") { value(updatedLabel.tests[0].name.name) }
                jsonPath("$.tests[0].type") { value(updatedLabel.tests[0].type.name) }
            }
        verify { labelService.update(id, label) }
    }

    @Test
    fun `Test delete`() {
        val id = 1

        every {
            labelService.delete(id)
        } returns Unit

        mockMvc.delete("/labels/${id}")
            .andExpect {
                status { NO_CONTENT }
            }
        verify { labelService.delete(id) }
    }


}