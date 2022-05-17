package net.recraft.annihilatoin.objects.menu

import net.recraft.annihilatoin.objects.kit.KitGenerator
import net.recraft.annihilatoin.objects.kit.KitType

class KitMenu: Menu("Annihilation Kit Menu") {
    init {
        KitType.values().forEach {
            val kit = KitGenerator.get(it)!!
            items.add(kit.icon)
        }
    }
}