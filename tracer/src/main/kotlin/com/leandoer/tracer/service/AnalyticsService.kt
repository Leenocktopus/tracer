package com.leandoer.tracer.service

import com.leandoer.tracer.mapper.map
import com.leandoer.tracer.model.dto.analytics.DistributionFilter
import com.leandoer.tracer.model.dto.analytics.GetCountStatisticsDto
import com.leandoer.tracer.model.dto.analytics.GetTestStatisticsDto
import com.leandoer.tracer.model.dto.analytics.DistributionDto
import com.leandoer.tracer.model.dto.analytics.GetLabelDistributionDto
import com.leandoer.tracer.model.dto.analytics.RateDto
import com.leandoer.tracer.model.dto.analytics.RateFilter
import com.leandoer.tracer.model.dto.analytics.TraceRateType
import com.leandoer.tracer.repository.TraceRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AnalyticsService(
    private val traceRepository: TraceRepository
) {

    fun getTracesRate(filter: RateFilter): List<RateDto> {
        return when (filter.type) {
            TraceRateType.APPLICATION -> traceRepository.getTracesRateByApplication(
                LocalDateTime.now().minus(filter.interval.interval),
                LocalDateTime.now(),
                filter.interval.scale
            ).map { map(it) }
            TraceRateType.LABEL -> traceRepository.getTracesRateByLabel(
                LocalDateTime.now().minus(filter.interval.interval),
                LocalDateTime.now(),
                filter.interval.scale
            ).map { map(it) }
        }
    }

    fun getAlertEventsRate(filter: RateFilter): List<RateDto> {
        return when (filter.type) {
            TraceRateType.APPLICATION -> traceRepository.getAlertEventsRateByApplication(
                LocalDateTime.now().minus(filter.interval.interval),
                LocalDateTime.now(),
                filter.interval.scale
            ).map { map(it) }
            TraceRateType.LABEL -> traceRepository.getAlertEventsRateByLabel(
                LocalDateTime.now().minus(filter.interval.interval),
                LocalDateTime.now(),
                filter.interval.scale
            ).map { map(it) }
        }
    }



    fun getTracesDistribution(filter: DistributionFilter): List<DistributionDto> {
        return traceRepository.getTracesDistribution(
            LocalDateTime.now().minus(filter.interval.interval),
            LocalDateTime.now()
        ).map { map(it) }
    }

    fun getAlertEventsDistribution(filter: DistributionFilter): List<DistributionDto> {
        return traceRepository.getAlertEventsDistribution(
            LocalDateTime.now().minus(filter.interval.interval),
            LocalDateTime.now()
        ).map { map(it) }
    }

    fun getCountStatistics(): GetCountStatisticsDto {
        return map(traceRepository.getCountStatistics());
    }

    fun getTestStatistics(): List<GetTestStatisticsDto> {
        return traceRepository.getTestStatistics().map { map(it) }
    }

    fun getLabelDistribution(labelId: Int): List<GetLabelDistributionDto> {
        return traceRepository.getLabelDistribution(labelId).map { map(it) }
    }

}
