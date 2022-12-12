package com.leandoer.tracer.model.entity

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "contacts")
data class Contact(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    val email: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_id", nullable = false)
    val alert: Alert
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {
            is Contact -> {
                (this.id == other.id
                        && this.email == other.email && this.alert.id == this.alert.id)
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Contact(id=$id, email='$email', alert.id=${alert.id})"
    }

}