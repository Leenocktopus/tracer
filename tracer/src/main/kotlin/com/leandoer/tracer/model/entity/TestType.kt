package com.leandoer.tracer.model.entity

enum class TestType(val title: String) {
    FREQUENCY("Frequency test"),
    BLOCK_FREQUENCY("Block frequency test"),
    BINARY_MATRIX("Binary matrix test"),
    NON_OVERLAPPING_TEMPLATE("Non overlapping templates test"),
    OVERLAPPING_TEMPLATE("Overlapping templates test"),
    SPECTRAL("Spectral test"),
    LINEAR_COMPLEXITY("Linear complexity test"),
    LONGEST_RUN_OF_ONES("Longest run of ones test"),
    MAURERS("Maurer's test"),
    CUMULATIVE_SUMS("Cumulative sums test"),
    RANDOM_EXCURSIONS("Random excursions test"),
    EXCURSION_VARIANT("Random excursion variant test"),
    RUNS("Runs test"),
    SERIAL("Serial test"),
    ENTROPY("Entropy test"),
    ONE("Multidimensional statistics #1"),
    TWO("Multidimensional statistics #2"),
    THREE("Multidimensional statistics #3"),
    FOUR("Multidimensional statistics #4"),
    FIVE("Multidimensional statistics #5"),
    SIX("Multidimensional statistics #6"),
    SEVEN("Multidimensional statistics #7"),
    EIGHT("Multidimensional statistics #8");

}