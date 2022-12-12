package com.leandoer.tracer.model.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "alerts")
data class Alert(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    val name: String,

    @OneToMany(mappedBy = "alert")
    val contacts: MutableSet<Contact> = mutableSetOf(),

    @OneToMany(mappedBy = "alert")
    val alertEvents: MutableSet<Contact> = mutableSetOf(),

    @ManyToMany
    @JoinTable(
        name = "label_alert",
        joinColumns = [JoinColumn(name = "alert_id")],
        inverseJoinColumns = [JoinColumn(name = "label_id")]
    )
    val labels: MutableSet<Label> = mutableSetOf()

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {
            is Alert -> {
                (this.id == other.id && this.name == other.name)
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Alert(id=$id, name='$name')"
    }

}