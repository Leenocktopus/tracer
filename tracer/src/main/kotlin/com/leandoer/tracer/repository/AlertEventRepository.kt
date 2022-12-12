package com.leandoer.tracer.repository

import com.leandoer.tracer.model.entity.AlertEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface AlertEventRepository : JpaRepository<AlertEvent, Long>, JpaSpecificationExecutor<AlertEvent>{
}