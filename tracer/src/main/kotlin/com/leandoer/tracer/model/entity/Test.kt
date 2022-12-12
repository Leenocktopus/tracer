package com.leandoer.tracer.model.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "tests")
data class Test(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Enumerated(STRING)
    @Column(name = "name")
    val testType: TestType,

    @Enumerated(STRING)
    val type: Package,

    val parametrized: Boolean,

    @OneToMany(mappedBy = "test")
    val tests: MutableSet<TestRun> = mutableSetOf(),

    @ManyToMany
    @JoinTable(
        name = "label_test",
        joinColumns = [JoinColumn(name = "test_id")],
        inverseJoinColumns = [JoinColumn(name = "label_id")]
    )
    val labels: MutableSet<Label> = mutableSetOf()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {
            is Test -> {
                (this.id == other.id && this.testType == other.testType && this.type == other.type)
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Test(id=$id, name='$testType', type=$type)"
    }


}