package com.leandoer.tracer.service

import com.leandoer.tracer.repository.TraceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestingService @Autowired constructor(var traceRepository: TraceRepository) {
    fun computeNistStatisticForAll(testParams: Map<String?, Map<String?, String>>): Map<String, Any> {
        val label = testParams["sequence_params"]!!["label"]!!.toInt()
        val min = testParams["sequence_params"]!!["min"]!!.toLong()
        val max = testParams["sequence_params"]!!["max"]!!.toLong()
        val binarySequence = traceRepository.getBinarySequence(min, max, label)
        val results: MutableMap<String, Any> = LinkedHashMap()
        testParams.entries.filterNot { it.key == "sequence_params" }
            .forEach { map ->
            val method: NistFactory = NistFactory.valueOf(map.key!!.uppercase())
            results[method.toString().lowercase()] = method.getResult(
                map.value.toMutableMap().let {
                    it["sequence"] = binarySequence
                    it
                }
            )

        }

        results.forEach{
            println("${it.key}, ${it.value}")
        }
        return results
    }
}