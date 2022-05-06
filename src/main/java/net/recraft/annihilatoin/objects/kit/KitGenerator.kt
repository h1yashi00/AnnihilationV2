package net.recraft.annihilatoin.objects.kit

import net.recraft.annihilatoin.util.Util
import net.recraft.annihilatoin.objects.kit.KitType.*

object KitGenerator {
//    private val kits = HashMap<KitType, KitBase>() // TODO KitBaseではなくジェネリクス型にしたい
    private val kits = mapOf(
            CIVILIAN to Civilian(),
            ARCHAR to Archar(),
            MINER to Miner(),
            ENCHANTER to Enchanter(),
            LUMBERJACK to Lumberjack(),
            SCOUT to Scout())
//    init {
//        kits[KitType.CIVILIAN]      = Civilian()
//        kits[KitType.ARCHAR]        = Archar()
//        kits[KitType.MINER]         = Miner()
//        kits[KitType.ENCHANTER]     = Enchanter()
//        kits[KitType.LUMBERJACK]    = Lumberjack()
//        kits[KitType.SCOUT]         = Scout()
//        if (KitType.values().size != kits.size) Util.fatal("KitType.values.size is not match kits.size!!!!!!!!!!! 訳initにkitを追加し忘れている")
//    }
    fun get(kitType: KitType): KitBase? {
        return kits[kitType]
}
//    private inline fun <reified T> get(backupLoot: () -> T):T? {
//        return kits[kitType]
//    }
}