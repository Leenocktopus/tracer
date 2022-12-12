const blockSizeTooltip = "Введіть число"
const templateTooltip = "Приклад: 001, 01"
export const data = {
    frequency: {
        ukrName: "Частотний тест",
        engName: "Frequency test",
        fields: {}
    },
    block_frequency: {
        ukrName: "Частотний тест у блоці",
        engName: "Block frequency test",
        fields: {
            blockSize: {
                ukrName: "Розмір блоку:",
                tooltip: blockSizeTooltip
            }
        }
    },
    runs: {
        ukrName: "Тест подібних послідовностей",
        engName: "Runs test",
        fields: {}
    },
    longest_run_of_ones: {
        ukrName: "Тест послідовності одиниць",
        engName: "Longest run of ones test",
        fields: {}
    },
    binary_matrix: {
        ukrName: "Тест рангів бінарних матриць",
        engName: "Binary matrix test",
        fields: {
            matrixSize: {
                ukrName: "Розмір матриці:",
                tooltip: blockSizeTooltip
            }
        }
    },
    spectral: {
        ukrName: "Спектральний тест",
        engName: "Spectral test",
        fields: {}
    },
    non_overlapping_template: {
        ukrName: "Тест шаблонів що не перетинаються",
        engName: "Non overlapping templates test",
        fields: {
            blockSize: {
                ukrName: "Розмір блоку:",
                tooltip: blockSizeTooltip
            },
            template: {
                ukrName: "Шаблон:",
                tooltip: templateTooltip
            }
        }
    },
    overlapping_template: {
        ukrName: "Тест шаблонів що перетинаються",
        engName: "Overlapping templates test",
        fields: {
            blockSize: {
                ukrName: "Розмір блоку:",
                tooltip: blockSizeTooltip
            },
            template: {
                ukrName: "Шаблон:",
                tooltip: templateTooltip
            }
        }
    },
    maurers: {
        engName: "Maurer's test",
        fields: {
            blockSize: {
                ukrName: "Розмір блоку:",
                tooltip: blockSizeTooltip
            },
            blocksInInitSegment: {
                ukrName: "Кількість блоків:",
                tooltip: blockSizeTooltip
            }
        }
    },
    linear_complexity: {
        ukrName: "Linear complexity test",
        engName: "Linear complexity test",
        fields: {
            blockSize: {
                ukrName: "Розмір блоку:",
                tooltip: blockSizeTooltip
            }
        }
    },
    serial: {
        ukrName: "Серійний тест",
        engName: "Serial test",
        fields: {
            blockSize: {
                ukrName: "Розмір блоку:",
                tooltip: blockSizeTooltip
            }
        },
        resultDisplay: ["Перший тест", "Другий тест"]
    },
    entropy: {
        ukrName: "Тест приблизної ентропії",
        engName: "Entropy test",
        fields: {
            blockSize: {
                ukrName: "Розмір блоку:",
                tooltip: blockSizeTooltip
            }
        }
    },
    cumulative_sums: {
        ukrName: "Тест кумулятивних сум",
        engName: "Cumulative sums test",
        fields: {},
        resultDisplay: ["Прямий тест", "Обернений тест"]
    },
    random_excursions: {
        ukrName: "Тест на довільні виключення",
        engName: "Random excursions test",
        fields: {},
        resultDisplay: [-4, -3, -2, -1, 1, 2, 3, 4]
    },
    excursion_variant: {
        ukrName: "Тест на варіант довільних виключень",
        engName: "Random excursion variant test",
        fields: {},
        resultDisplay: [-9, -8, -7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9]
    }
}