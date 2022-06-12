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
    SCORPIO,
    DASHER,
    SPY,
    DEFENDER;
    val shortName: String = "${name[0]}${name[1]}${name[2]}"

    companion object {
        fun getKitByName(name: String): KitType? {
            values().forEach { if (it.name.equals(name, ignoreCase = true)) return it }
            return null
        }
    }
}