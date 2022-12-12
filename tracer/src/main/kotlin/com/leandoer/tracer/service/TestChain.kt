package com.leandoer.tracer.service

import com.leandoer.tracer.model.entity.Test
import com.leandoer.tracer.model.entity.TestRun
import com.leandoer.tracer.model.entity.TestType
import com.leandoer.tracer.model.entity.TestType.CUMULATIVE_SUMS
import com.leandoer.tracer.model.entity.TestType.EIGHT
import com.leandoer.tracer.model.entity.TestType.EXCURSION_VARIANT
import com.leandoer.tracer.model.entity.TestType.FIVE
import com.leandoer.tracer.model.entity.TestType.FOUR
import com.leandoer.tracer.model.entity.TestType.FREQUENCY
import com.leandoer.tracer.model.entity.TestType.LONGEST_RUN_OF_ONES
import com.leandoer.tracer.model.entity.TestType.ONE
import com.leandoer.tracer.model.entity.TestType.RANDOM_EXCURSIONS
import com.leandoer.tracer.model.entity.TestType.RUNS
import com.leandoer.tracer.model.entity.TestType.SEVEN
import com.leandoer.tracer.model.entity.TestType.SIX
import com.leandoer.tracer.model.entity.TestType.SPECTRAL
import com.leandoer.tracer.model.entity.TestType.THREE
import com.leandoer.tracer.model.entity.TestType.TWO
import com.leandoer.tracer.model.entity.Trace
import md.MdTest
import nist.NistTest
import java.math.BigDecimal

abstract class TestExecution(private var next: TestExecution?, private val testType: TestType) {
    companion object {
        fun link(first: TestExecution, vararg chain: TestExecution): TestExecution {
            var head = first
            chain.forEach {
                head.next = it
                head = it
            }
            return first
        }
    }

    fun executeNext(tests: List<Test>, trace: Trace): List<TestRun> {
        return (tests.find { it.testType == testType }?.let { executeTest(trace, it) } ?: listOf()) +
                (next?.executeNext(tests, trace) ?: listOf())

    }


    abstract fun executeTest(trace: Trace, test: Test): List<TestRun>
}

class FrequencyExecution(next: TestExecution? = null) : TestExecution(next, FREQUENCY) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        val result = NistTest.frequencyTest(trace.value.toBinary())
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(result.getpValue()), result.isSequenceRandom))
    }

}

class SpectralExecution(next: TestExecution? = null) : TestExecution(next, SPECTRAL) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        val result = NistTest.spectralTest(trace.value.toBinary())
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(result.getpValue()), result.isSequenceRandom))
    }

}

class LongestRunOfOnesExecution(next: TestExecution? = null) : TestExecution(next, LONGEST_RUN_OF_ONES) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        val result = NistTest.longestRunOfOnesTest(trace.value.toBinary())
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(result.getpValue()), result.isSequenceRandom))
    }

}

class CumulativeSumsExecution(next: TestExecution? = null) : TestExecution(next, CUMULATIVE_SUMS) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        return NistTest.cumulativeSumsTest(trace.value.toBinary()).map {
            TestRun(null, trace, test, BigDecimal.valueOf(it.getpValue()), it.isSequenceRandom)
        }
    }

}


class RunsExecution(next: TestExecution? = null) : TestExecution(next, RUNS) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        val result = NistTest.runsTest(trace.value.toBinary())
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(result.getpValue()), result.isSequenceRandom))
    }

}

class RandomExcursionsExecution(next: TestExecution? = null) : TestExecution(next, RANDOM_EXCURSIONS) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        return NistTest.randomExcursionsTest(trace.value.toBinary()).map {
            TestRun(null, trace, test, BigDecimal.valueOf(it.getpValue()), it.isSequenceRandom)
        }
    }

}

class ExcursionVariantExecution(next: TestExecution? = null) : TestExecution(next, EXCURSION_VARIANT) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        return NistTest.randomExcursionsVariantTest(trace.value.toBinary()).map {
            TestRun(null, trace, test, BigDecimal.valueOf(it.getpValue()), it.isSequenceRandom)
        }
    }

}

class MultidimensionalOneExecution(next: TestExecution? = null) : TestExecution(next, ONE) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(MdTest.one(trace.value.toBinary())), null))
    }

}

class MultidimensionalTwoExecution(next: TestExecution? = null) : TestExecution(next, TWO) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(MdTest.two(trace.value.toBinary())), null))
    }

}

class MultidimensionalThreeExecution(next: TestExecution? = null) : TestExecution(next, THREE) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(MdTest.three(trace.value.toBinary())), null))
    }

}

class MultidimensionalFourExecution(next: TestExecution? = null) : TestExecution(next, FOUR) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(MdTest.four(trace.value.toBinary())), null))
    }

}

class MultidimensionalFiveExecution(next: TestExecution? = null) : TestExecution(next, FIVE) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(MdTest.five(trace.value.toBinary())), null))
    }

}

class MultidimensionalSixExecution(next: TestExecution? = null) : TestExecution(next, SIX) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(MdTest.six(trace.value.toBinary())), null))
    }

}

class MultidimensionalSevenExecution(next: TestExecution? = null) : TestExecution(next, SEVEN) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(MdTest.seven(trace.value.toBinary())), null))
    }

}

class MultidimensionalEightExecution(next: TestExecution? = null) : TestExecution(next, EIGHT) {

    override fun executeTest(trace: Trace, test: Test): List<TestRun> {
        return listOf(TestRun(null, trace, test, BigDecimal.valueOf(MdTest.eight(trace.value.toBinary())), null))
    }

}


object TestChain {
    val chain = TestExecution.link(
        FrequencyExecution(),
        SpectralExecution(),
        LongestRunOfOnesExecution(),
        CumulativeSumsExecution(),
        RunsExecution(),
        RandomExcursionsExecution(),
        ExcursionVariantExecution(),
        MultidimensionalOneExecution(),
        MultidimensionalTwoExecution(),
        MultidimensionalThreeExecution(),
        MultidimensionalFourExecution(),
        MultidimensionalFiveExecution(),
        MultidimensionalSixExecution(),
        MultidimensionalSevenExecution(),
        MultidimensionalEightExecution()
    )
}
