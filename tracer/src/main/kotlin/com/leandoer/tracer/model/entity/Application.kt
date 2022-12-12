package com.leandoer.tracer.model.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "applications")
data class Application(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    val name: String,

    @OneToMany(mappedBy = "application")
    val traces: MutableSet<Trace> = mutableSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {
            is Application -> {
                (this.id == other.id && this.name == other.name)
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Application(id=$id, name='$name')"
    }

}