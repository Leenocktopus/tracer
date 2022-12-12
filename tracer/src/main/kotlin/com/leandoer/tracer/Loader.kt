package com.leandoer.tracer

import com.leandoer.tracer.model.dto.trace.PayloadType
import com.leandoer.tracer.model.dto.trace.PostTraceDto
import com.leandoer.tracer.model.entity.AlertStatus
import com.leandoer.tracer.repository.AlertEventRepository
import com.leandoer.tracer.repository.TestRunRepository
import com.leandoer.tracer.service.TraceService
import liquibase.pro.packaged.it
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.ThreadLocalRandom

@Component
@Profile("loader")
class Loader(
    private val traceService: TraceService,
    private val alertEventRepository: AlertEventRepository
) {

    val amounts = listOf(
        650,
        830,
        900,
        820,
        750,
        700,
        640,
        580,
        580,
        570,
        560,
        510,
        440,
        300,
        280,
        290,
        260,
        210,
        290,
        400,
        450,
        680,
        900,
        920,
        930,
        890,
        670,
        390,
        450,
        700
    )
    val labels = listOf("java", "binary", "decimal", "hardware")
    val applications =
        listOf("sender-service", "sender-service", "sender-service", "email-service", "email-service", "lt-service")

    @EventListener(ApplicationReadyEvent::class)
    fun startupSequence() {
        firstStep()
    }

    fun firstStep(){
        amounts.forEachIndexed { index, amount ->
            val realAmount = if (ThreadLocalRandom.current().nextBoolean()) {
                amount - ThreadLocalRandom.current().nextInt(0, 20)
            } else {
                amount + ThreadLocalRandom.current().nextInt(0, 20)
            } * 2

            ThreadLocalRandom.current().longs(realAmount.toLong()).forEach {
                traceService.create(
                    PostTraceDto(
                        randomValue(),
                        PayloadType.NUMBER,
                        randomApplication(),
                        randomLabels(),
                        randomTimestamp(
                            LocalDateTime.now().minusDays(30 - index.toLong()),
                            LocalDateTime.now().minusDays(30 - index.toLong() - 1)
                        )
                    )
                )
            }

        }
        ThreadLocalRandom.current().longs(10000).forEach {
            traceService.create(
                PostTraceDto(
                    randomValue(),
                    PayloadType.NUMBER,
                    randomApplication(),
                    randomLabels(),
                    randomTimestamp(LocalDateTime.now().minusDays(30), LocalDateTime.now())
                )
            )
        }

        ThreadLocalRandom.current().longs(3000).forEach {
            traceService.create(
                PostTraceDto(
                    randomBinaryValue(),
                    PayloadType.BINARY_STRING,
                    randomApplication(),
                    if (ThreadLocalRandom.current().nextBoolean()) listOf("binary", "hardware") else listOf("hardware"),
                    randomTimestamp(LocalDateTime.now().minusDays(30), LocalDateTime.now())
                )
            )
        }

        fillLastHour()

        alertEventRepository.findAll().map { it.copy(status = AlertStatus.RESOLVED) }
            .let { alertEventRepository.saveAll(it) }
    }

    fun fillLastHour(){
        ThreadLocalRandom.current().longs(200).forEach {
            traceService.create(
                PostTraceDto(
                    it.toString(),
                    PayloadType.NUMBER,
                    randomApplication(),
                    randomLabels(),
                    randomTimestamp(LocalDateTime.now().minusHours(1), LocalDateTime.now())
                )
            )
        }
    }

    fun randomValue(): String {
        return ThreadLocalRandom.current().nextLong().toString();
    }

    fun randomBinaryValue(): String {
        return java.lang.Long.toBinaryString(ThreadLocalRandom.current().nextLong()) +
                java.lang.Long.toBinaryString(ThreadLocalRandom.current().nextLong())
    }

    fun randomLabels(): List<String> {
        val labelsList = labels.flatMap { List(ThreadLocalRandom.current().nextInt(1, 10)) { _ -> it } }
        val secondElement = ThreadLocalRandom.current().nextBoolean() && ThreadLocalRandom.current().nextBoolean()
        return listOfNotNull(
            labelsList[ThreadLocalRandom.current().nextInt(0, labelsList.size)],
            if (secondElement) labelsList[ThreadLocalRandom.current().nextInt(0, labelsList.size)] else null
        )
    }

    fun randomApplication(): String {
        val applicationList = applications.flatMap { List(ThreadLocalRandom.current().nextInt(1, 5)) { _ -> it } }
        return applicationList[ThreadLocalRandom.current().nextInt(0, applicationList.size)]
    }

    fun randomTimestamp(beginDate: LocalDateTime, endDate: LocalDateTime): LocalDateTime {
        val minDay = beginDate.toEpochSecond(ZoneOffset.UTC)
        val maxDay = endDate.toEpochSecond(ZoneOffset.UTC)
        val randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay)
        return LocalDateTime.ofEpochSecond(randomDay, 0, ZoneOffset.UTC)
    }

}