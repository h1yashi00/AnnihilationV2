package net.recraft.annihilatoin.database

enum class RecraftTable {
    ANNIHILATION_STATS;

    override fun toString(): String {
        return name.toLowerCase()
    }
}