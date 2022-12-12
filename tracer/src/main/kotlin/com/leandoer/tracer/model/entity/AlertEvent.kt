package com.leandoer.tracer.model.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "alert_events")
data class AlertEvent(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_id", nullable = false)
    val alert: Alert,

    @Column(name = "reason")
    val reason: String,

    @Enumerated(STRING)
    @Column(name = "status")
    val status: AlertStatus,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trace_id", nullable = false)
    val trace: Trace
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {
            is AlertEvent -> {
                (this.id == other.id
                        && this.alert.id == other.alert.id
                        && this.reason == other.reason
                        && this.trace.id == other.trace.id)
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "AlertEvent(id=$id, alert.id=${alert.id}, reason=${reason}, trace.id=${trace.id})"
    }


}