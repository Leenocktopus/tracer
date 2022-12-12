package com.leandoer.tracer.repository

import com.leandoer.tracer.model.entity.Contact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact, Int>, JpaSpecificationExecutor<Contact> {

}