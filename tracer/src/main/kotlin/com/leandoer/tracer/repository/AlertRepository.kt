package com.leandoer.tracer.repository

import com.leandoer.tracer.model.entity.Alert
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface AlertRepository : JpaRepository<Alert, Int>, JpaSpecificationExecutor<Alert> {

}