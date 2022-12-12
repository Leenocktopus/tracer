package com.leandoer.tracer.repository

import com.leandoer.tracer.model.entity.Test
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface TestRepository : JpaRepository<Test, Long>, JpaSpecificationExecutor<Test>{

}