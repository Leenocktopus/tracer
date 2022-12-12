package com.leandoer.tracer.model.entity

import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table


@Entity
@Table(name = "traces")
data class Trace(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val value: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    val application: Application,

    val generatedAt: LocalDateTime,

    @OneToMany(mappedBy = "trace", cascade = [CascadeType.PERSIST])
    val testRuns: MutableSet<TestRun> = mutableSetOf(),

    @ManyToMany
    @JoinTable(
        name = "label_trace",
        joinColumns = [JoinColumn(name = "trace_id")],
        inverseJoinColumns = [JoinColumn(name = "label_id")]
    )
    val labels: MutableSet<Label> = mutableSetOf()
) {

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        return when (other) {
            is Trace -> {
                (this.id == other.id
                        && this.value == other.value
                        && this.application.id == other.application.id
                        && this.generatedAt == other.generatedAt)
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Trace(id=$id, value='$value', application.id='${application.id}', generatedAt=$generatedAt)"
    }

}