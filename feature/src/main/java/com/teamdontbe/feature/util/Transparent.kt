package com.teamdontbe.feature.util

class Transparent() {
    fun calculateColorWithOpacity(value: Int): String {
        val clampedValue = value.coerceIn(-100, 0)

        // Calculate the opacity percentage
        val opacityPercentage = ((-clampedValue) / 100.0) * 100

        // Calculate the alpha value based on the opacity percentage
        val alpha = (255 * (opacityPercentage / 100)).toInt()

        // Generate the hex color code with the calculated alpha
        val hexColor = String.format("#%02X%02X%02X%02X", alpha, 252, 252, 253)

        return hexColor
    }
}
