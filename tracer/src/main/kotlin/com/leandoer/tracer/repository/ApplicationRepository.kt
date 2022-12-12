package com.leandoer.tracer.repository

import com.leandoer.tracer.model.entity.Application
import com.leandoer.tracer.model.entity.Trace
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ApplicationRepository : JpaRepository<Application, Int>, JpaSpecificationExecutor<Application>{

}