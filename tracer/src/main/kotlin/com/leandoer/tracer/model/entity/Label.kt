package com.leandoer.tracer.model.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "labels")
data class Label(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    val label: String,

    @ManyToMany(cascade = [CascadeType.MERGE])
    @JoinTable(
        name = "label_test",
        joinColumns = [JoinColumn(name = "label_id")],
        inverseJoinColumns = [JoinColumn(name = "test_id")]
    )
    val tests: MutableSet<Test> = mutableSetOf(),

    @ManyToMany()
    @JoinTable(
        name = "label_alert",
        joinColumns = [JoinColumn(name = "label_id")],
        inverseJoinColumns = [JoinColumn(name = "alert_id")]
    )
    val alerts: MutableSet<Alert> = mutableSetOf(),

    @ManyToMany(cascade = [CascadeType.REMOVE])
    @JoinTable(
        name = "label_trace",
        joinColumns = [JoinColumn(name = "label_id")],
        inverseJoinColumns = [JoinColumn(name = "trace_id")]
    )
    val traces: MutableSet<Trace> = mutableSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {
            is Label -> {
                (this.id == other.id && this.label == other.label)
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Label(id=$id, label='$label')"
    }

}