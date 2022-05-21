package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.entity.Player

class Acrobat: KitBase(
    KitType.ACROBAT,
    Material.FEATHER,
    listOf(
        "落下ダメージを受けない",
        "二段ジャンプができる"
    )
) {
    override fun setInit(player: Player) {
        player.allowFlight = true
        player.isFlying = false
    }
}