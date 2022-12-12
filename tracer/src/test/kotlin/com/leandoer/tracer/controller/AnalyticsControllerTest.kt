package com.leandoer.tracer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leandoer.tracer.configuration.DATE_TIME_FORMAT
import com.leandoer.tracer.model.dto.analytics.GetCountStatisticsDto
import com.leandoer.tracer.model.dto.analytics.GetTestStatisticsDto
import com.leandoer.tracer.model.dto.analytics.DistributionDto
import com.leandoer.tracer.model.dto.analytics.RateDto
import com.leandoer.tracer.model.dto.analytics.DistributionFilter
import com.leandoer.tracer.model.dto.analytics.RateFilter
import com.leandoer.tracer.model.dto.analytics.TraceRateType.LABEL
import com.leandoer.tracer.model.entity.Package.MD
import com.leandoer.tracer.model.entity.Package.NIST
import com.leandoer.tracer.model.entity.TestType.FIVE
import com.leandoer.tracer.model.entity.TestType.CUMULATIVE_SUMS
import com.leandoer.tracer.model.entity.TestType.FREQUENCY
import com.leandoer.tracer.model.entity.Intervals.DAY
import com.leandoer.tracer.model.entity.Intervals.MONTH
import com.leandoer.tracer.service.AnalyticsService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus.OK
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@WebMvcTest(AnalyticsController::class)
@AutoConfigureMockMvc
class AnalyticsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var analyticsService: AnalyticsService

    @Autowired
    private lateinit var mapper: ObjectMapper


    @Test
    fun `Test getTracesRate`() {
        val traceRateFilter = RateFilter(LABEL, DAY)
        val rates = listOf(
            RateDto("google", LocalDateTime.of(2022, 10, 10, 12, 0), 1),
            RateDto("google", LocalDateTime.of(2022, 10, 10, 16, 0), 2),
            RateDto("google", LocalDateTime.of(2022, 10, 10, 20, 0), 0)
        )
        every {
            analyticsService.getTracesRate(traceRateFilter)
        } returns rates

        mockMvc.get("/analytics/rate/traces?interval" +
                "=${traceRateFilter.interval}&type=${traceRateFilter.type}")
            .andExpect {
                status { OK }
                jsonPath("$[0].label") { value(rates[0].label) }
                jsonPath("$[0].timestamp") {
                    value(
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(rates[0].timestamp)
                    )
                }
                jsonPath("$[0].count") { value(rates[0].count) }
                jsonPath("$[1].label") { value(rates[1].label) }
                jsonPath("$[1].timestamp") {
                    value(
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(rates[1].timestamp)
                    )
                }
                jsonPath("$[1].count") { value(rates[1].count) }
                jsonPath("$[2].label") { value(rates[2].label) }
                jsonPath("$[2].timestamp") {
                    value(
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(rates[2].timestamp)
                    )
                }
                jsonPath("$[2].count") { value(rates[2].count) }
            }
        verify { analyticsService.getTracesRate(traceRateFilter) }
    }

    @Test
    fun `Test getAlertEventRate`() {
        val alertEventRateFilter = RateFilter(LABEL, MONTH)
        val rates = listOf(
            RateDto("google", LocalDateTime.of(2022, 10, 10, 12, 0), 1),
            RateDto("google", LocalDateTime.of(2022, 10, 10, 16, 0), 2),
            RateDto("google", LocalDateTime.of(2022, 10, 10, 20, 0), 0)
        )
        every {
            analyticsService.getAlertEventsRate(alertEventRateFilter)
        } returns rates

        mockMvc.get("/analytics/rate/alert-events?interval" +
                "=${alertEventRateFilter.interval}&type=${alertEventRateFilter.type}")
            .andExpect {
                status { OK }
                jsonPath("$[0].label") { value(rates[0].label) }
                jsonPath("$[0].timestamp") {
                    value(
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(rates[0].timestamp)
                    )
                }
                jsonPath("$[0].count") { value(rates[0].count) }
                jsonPath("$[1].label") { value(rates[1].label) }
                jsonPath("$[1].timestamp") {
                    value(
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(rates[1].timestamp)
                    )
                }
                jsonPath("$[1].count") { value(rates[1].count) }
                jsonPath("$[2].label") { value(rates[2].label) }
                jsonPath("$[2].timestamp") {
                    value(
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(rates[2].timestamp)
                    )
                }
                jsonPath("$[2].count") { value(rates[2].count) }
            }
        verify { analyticsService.getAlertEventsRate(alertEventRateFilter) }
    }

    @Test
    fun `Test getTracesDistribution`() {
        val filter = DistributionFilter(DAY)
        val distribution = listOf(
            DistributionDto("sender", 10.3),
            DistributionDto("tracer", 24.7),
            DistributionDto("emailer", 65.0)
        )
        every {
            analyticsService.getTracesDistribution(filter)
        } returns distribution

        mockMvc.get("/analytics/distribution/traces?interval=${filter.interval}")
            .andExpect {
                status { OK }
                jsonPath("$[0].application") { value(distribution[0].application) }
                jsonPath("$[0].percentage") { value(distribution[0].percentage) }
                jsonPath("$[1].application") { value(distribution[1].application) }
                jsonPath("$[1].percentage") { value(distribution[1].percentage) }
                jsonPath("$[2].application") { value(distribution[2].application) }
                jsonPath("$[2].percentage") { value(distribution[2].percentage) }
            }
        verify { analyticsService.getTracesDistribution(filter) }
    }

    @Test
    fun `Test getAlertEventsDistribution`() {
        val filter = DistributionFilter(DAY)
        val distribution = listOf(
            DistributionDto("sender", 66.6),
            DistributionDto("tracer", 33.7),
        )
        every {
            analyticsService.getAlertEventsDistribution(filter)
        } returns distribution

        mockMvc.get("/analytics/distribution/alert-events?interval=${filter.interval}")
            .andExpect {
                status { OK }
                jsonPath("$[0].application") { value(distribution[0].application) }
                jsonPath("$[0].percentage") { value(distribution[0].percentage) }
                jsonPath("$[1].application") { value(distribution[1].application) }
                jsonPath("$[1].percentage") { value(distribution[1].percentage) }
            }
        verify { analyticsService.getAlertEventsDistribution(filter) }
    }

    @Test
    fun `Test getCountStatistics`() {
        val countStatistics = GetCountStatisticsDto(3, 2, 1000, 3, 100, 10, 90)

        every {
            analyticsService.getCountStatistics()
        } returns countStatistics

        mockMvc.get("/analytics/statistics/count")
            .andExpect {
                status { OK }
                jsonPath("$.applications") { value(countStatistics.applications) }
                jsonPath("$.labels") { value(countStatistics.labels) }
                jsonPath("$.traces") { value(countStatistics.traces) }
            }
        verify { analyticsService.getCountStatistics() }
    }

    @Test
    fun `Test getTestStatistics`() {
        val testStatistics = listOf(
            GetTestStatisticsDto(FIVE.title, MD, 1),
            GetTestStatisticsDto(CUMULATIVE_SUMS.title, NIST, 2),
            GetTestStatisticsDto(FREQUENCY.title, NIST, 10),
        )

        every {
            analyticsService.getTestStatistics()
        } returns testStatistics

        mockMvc.get("/analytics/statistics/tests")
            .andExpect {
                status { OK }
                jsonPath("$[0].testType") { value(testStatistics[0].testType) }
                jsonPath("$[0].type") { value(testStatistics[0].type.name) }
                jsonPath("$[0].count") { value(testStatistics[0].count) }
                jsonPath("$[1].testType") { value(testStatistics[1].testType) }
                jsonPath("$[1].type") { value(testStatistics[1].type.name) }
                jsonPath("$[1].count") { value(testStatistics[1].count) }
                jsonPath("$[2].testType") { value(testStatistics[2].testType) }
                jsonPath("$[2].type") { value(testStatistics[2].type.name) }
                jsonPath("$[2].count") { value(testStatistics[2].count) }
            }
        verify { analyticsService.getTestStatistics() }
    }


}