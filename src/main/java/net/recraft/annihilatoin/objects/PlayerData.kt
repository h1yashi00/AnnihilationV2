package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.objects.kit.KitType

data class PlayerData(
    var team: GameTeam? = null,
    var kitType: KitType = KitType.CIVILIAN,
    var invisible: Boolean = false,
) {
}