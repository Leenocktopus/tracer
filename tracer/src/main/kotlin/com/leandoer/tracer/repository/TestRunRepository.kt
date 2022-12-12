package com.leandoer.tracer.repository

import com.leandoer.tracer.model.entity.TestRun
import com.leandoer.tracer.model.entity.Trace
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface TestRunRepository : JpaRepository<TestRun, Long>, JpaSpecificationExecutor<TestRun> {

}