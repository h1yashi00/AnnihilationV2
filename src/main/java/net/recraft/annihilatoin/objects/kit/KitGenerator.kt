package net.recraft.annihilatoin.objects.kit


object KitGenerator {
    fun get(kitType: KitType): KitBase {
        return when(kitType) {
            KitType.CIVILIAN -> Civilian()
            KitType.LUMBERJACK-> Lumberjack()
            KitType.ARCHAR   -> Archar()
            KitType.MINER    -> Miner()
            KitType.SCOUT    -> Scout()
            KitType.ENCHANTER-> Enchanter()
            KitType.ACROBAT  -> Acrobat()
            KitType.SWAPPER  -> Swapper()
            KitType.SCORPIO  -> Scorpio()
        }
    }
}