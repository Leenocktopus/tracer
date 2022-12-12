package com.leandoer.tracer.model.entity

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "test_runs")
data class TestRun(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trace_id", nullable = false)
    val trace: Trace,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    val test: Test,

    @Column(name = "test_result")
    val testResult: BigDecimal,

    val random: Boolean?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {
            is TestRun -> {
                (this.id == other.id
                        && this.trace.id == other.trace.id
                        && this.test.id == other.test.id
                        && this.testResult == other.testResult
                        && this.random == other.random)
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "TestRun(id=$id, trace.id=${trace.id}, test.id=${test.id}, testResult=$testResult, random=$random)"
    }


}