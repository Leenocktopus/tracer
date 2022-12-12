package com.leandoer.tracer.repository

import com.leandoer.tracer.model.entity.Label
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface LabelRepository : JpaRepository<Label, Int>, JpaSpecificationExecutor<Label> {

}