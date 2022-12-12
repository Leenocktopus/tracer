package com.leandoer.tracer.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired
import com.leandoer.tracer.service.TestingService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * @author Alexey Raichev
 * @since 15.04.2020
 * Controller for NIST tests API
 */
@RestController
@RequestMapping("/realtime-test")
class TestingController @Autowired constructor(var testingService: TestingService) {
    @PostMapping
    fun testSequenceAll(@RequestBody params: Map<String?, Map<String?, String>>): Map<String, Any> {
        return testingService.computeNistStatisticForAll(params)
    }
}