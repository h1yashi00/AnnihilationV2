package net.recraft.annihilatoin.objects.kit

enum class KitType(private val index: Int) {
    CIVILIAN(1),
    ARCHAR(2),
    MINER(3),
    ENCHANTER(4),
    LUMBERJACK(5),
    SCOUT(6),
    ACROBAT(7),
    SWAPPER(8),
    SCORPIO(9),
    DASHER(10),
    SPY(11),
    DEFENDER(12);
    val shortName: String = "${name[0]}${name[1]}${name[2]}"

    companion object {
        fun getKitByName(name: String): KitType? {
            values().forEach { if (it.name.equals(name, ignoreCase = true)) return it }
            return null
        }
    }
}