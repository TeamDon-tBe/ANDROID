package com.teamdontbe.feature.util

import com.teamdontbe.feature.util.TransparentRange.Companion.HEX_COLOR_PATTERN

class Transparent() {
    fun calculateColorWithOpacity(value: Int): String {
        val transparentPercentage = -TransparentRange.getTransparentRange(value).clampedValue
        val alpha = (255 * (transparentPercentage / 100.0)).toInt()
        return String.format(HEX_COLOR_PATTERN, alpha, 252, 252, 253)
    }
}

enum class TransparentRange(val range: IntRange, val clampedValue: Int) {
    RANGE_1(-Int.MAX_VALUE..-81, -85), RANGE_2(-80..-71, -80), RANGE_3(
        -70..-61,
        -70
    ),
    RANGE_4(-60..-51, -60), RANGE_5(-50..-41, -50), RANGE_6(-40..-31, -40), RANGE_7(
        -30..-21,
        -30
    ),
    RANGE_8(-20..-11, -20), RANGE_9(-10..-1, -10), RANGE_10(0..Int.MAX_VALUE, 0);

    companion object {
        fun getTransparentRange(value: Int) = values().firstOrNull { value in it.range } ?: RANGE_10
        const val HEX_COLOR_PATTERN = "#%02X%02X%02X%02X"
    }
}
