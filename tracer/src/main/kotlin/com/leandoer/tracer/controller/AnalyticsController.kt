package com.leandoer.tracer.controller

import com.leandoer.tracer.model.dto.analytics.GetCountStatisticsDto
import com.leandoer.tracer.model.dto.analytics.GetTestStatisticsDto
import com.leandoer.tracer.model.dto.analytics.DistributionDto
import com.leandoer.tracer.model.dto.analytics.RateDto
import com.leandoer.tracer.model.dto.analytics.DistributionFilter
import com.leandoer.tracer.model.dto.analytics.GetLabelDistributionDto
import com.leandoer.tracer.model.dto.analytics.RateFilter
import com.leandoer.tracer.service.AnalyticsService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/analytics")
class AnalyticsController(
    private val analyticsService: AnalyticsService
) {
    @GetMapping("/rate/traces")
    fun getTracesRate(
        @Valid filter: RateFilter
    ): List<RateDto> {
        return analyticsService.getTracesRate(filter)
    }

    @GetMapping("/rate/alert-events")
    fun getAlertEventRate(
        @Valid filter: RateFilter
    ): List<RateDto> {
        return analyticsService.getAlertEventsRate(filter)
    }


    @GetMapping("/distribution/traces")
    fun getTracesDistribution(
        @Valid filter: DistributionFilter
    ): List<DistributionDto> {
        return analyticsService.getTracesDistribution(filter)
    }

    @GetMapping("/distribution/alert-events")
    fun getAlertEventsDistribution(
        @Valid filter: DistributionFilter
    ): List<DistributionDto> {
        return analyticsService.getAlertEventsDistribution(filter)
    }

    @GetMapping("/statistics/count")
    fun getCountStatistics(): GetCountStatisticsDto {
        return analyticsService.getCountStatistics()
    }

    @GetMapping("/statistics/tests")
    fun getTestStatistics(): List<GetTestStatisticsDto> {
        return analyticsService.getTestStatistics()
    }

    @GetMapping("/distribution/labels")
    fun getLabelDistribution(@RequestParam @Min(1) labelId: Int): List<GetLabelDistributionDto> {
        return analyticsService.getLabelDistribution(labelId)
    }

}