package com.leandoer.tracer.service

import nist.NistTest
import javax.validation.ValidationException


enum class NistFactory(val title: String) {
    FREQUENCY("Frequency test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.frequencyTest(testParams["sequence"]) as T
        }
    },
    BLOCK_FREQUENCY("Block frequency test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.blockFrequencyTest(testParams["sequence"], testParams["blockSize"]!!.toInt()) as T
        }
    },
    BINARY_MATRIX("Binary matrix test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.binaryMatrixRankTest(testParams["sequence"], testParams["matrixSize"]!!.toInt()) as T
        }
    },
    NON_OVERLAPPING_TEMPLATE("Non overlapping templates test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.nonOverlappingTemplateTest(
                testParams["sequence"],
                testParams["blockSize"]!!.toInt(),
                testParams["template"]
            ) as T
        }
    },
    OVERLAPPING_TEMPLATE("Overlapping templates test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.overlappingTemplateTest(
                testParams["sequence"],
                testParams["blockSize"]!!.toInt(),
                testParams["template"]
            ) as T
        }
    },
    SPECTRAL("Spectral test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.spectralTest(testParams["sequence"]) as T
        }
    },
    LINEAR_COMPLEXITY("Linear complexity test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.linearComplexityTest(testParams["sequence"], testParams["blockSize"]!!.toInt()) as T
        }
    },
    LONGEST_RUN_OF_ONES("Longest run of ones test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.longestRunOfOnesTest(testParams["sequence"]) as T
        }
    },
    MAURERS("Maurer's test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.maurersTest(
                testParams["sequence"], testParams["blockSize"]!!
                    .toInt(), testParams["blocksInInitSegment"]!!.toInt()
            ) as T
        }
    },
    CUMULATIVE_SUMS("Cumulative sums test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.cumulativeSumsTest(testParams["sequence"]) as T
        }
    },
    RANDOM_EXCURSIONS("Random excursions test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.randomExcursionsTest(testParams["sequence"]) as T
        }
    },
    EXCURSION_VARIANT("Random excursion variant test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.randomExcursionsVariantTest(testParams["sequence"]) as T
        }
    },
    RUNS("Runs test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.runsTest(testParams["sequence"]) as T
        }
    },
    SERIAL("Serial test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.serialTest(testParams["sequence"], testParams["blockSize"]!!.toInt()) as T
        }
    },
    ENTROPY("Entropy test") {
        override fun <T> test(testParams: Map<String?, String>): T {
            return NistTest.entropyTest(testParams["sequence"], testParams["blockSize"]!!.toInt()) as T
        }
    };

    abstract fun <T> test(testParams: Map<String?, String>): T
    fun <T> getResult(testParams: Map<String?, String>): T {
        return try {
            test(testParams)
        } catch (ex: IllegalArgumentException) {
            throw ValidationException(ex.message, Throwable(name))
        }
    }
}