package dev.weiland.mods.cameracraft.util

fun Int.modulus(divisor: Int): Int {
    return ((this % divisor) + divisor) % divisor
}