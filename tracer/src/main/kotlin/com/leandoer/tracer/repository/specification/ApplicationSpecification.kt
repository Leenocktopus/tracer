package com.leandoer.tracer.repository.specification

import com.leandoer.tracer.model.entity.Application
import com.leandoer.tracer.model.entity.Application_
import com.leandoer.tracer.model.entity.Label
import com.leandoer.tracer.model.entity.Label_
import md.MdTest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component


@Component
class ApplicationSpecification {
    fun nameEquals(name: String?): Specification<Application> {
        return Specification<Application> { root, _, builder ->
            if (name == null) {
                null
            } else {
                builder.equal(root.get<String>(Application_.NAME), name)
            }
        }
    }
}