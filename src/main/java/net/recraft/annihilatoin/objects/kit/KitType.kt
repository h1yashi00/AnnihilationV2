package net.recraft.annihilatoin.objects.kit

enum class KitType {
    CIVILIAN,
    ARCHAR,
    MINER,
    ENCHANTER,
    LUMBERJACK,
    SCOUT,
    ACROBAT,
    SWAPPER,
    SCORPIO;
    companion object {
        fun getKitByName(name: String): KitType? {
            values().forEach { if (it.name.toLowerCase() == name.toLowerCase()) return it }
            return null
        }
    }
}