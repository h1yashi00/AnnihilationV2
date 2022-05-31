package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.objects.kit.KitType
import org.bukkit.Bukkit
import java.util.*

class PlayerData(
    private val uuid: UUID,
    _team: GameTeam? = null,
    _kitType: KitType = KitType.CIVILIAN,
    _invisible: Boolean = false,
    _voidCancel: Boolean = false,
)
{
    var team: GameTeam? = _team
    var kitType: KitType = _kitType
        set(value) {
            val player = Bukkit.getPlayer(uuid)
            player.allowFlight = false
            player.healthScale = 20.0
            field = value
        }
    var invisible: Boolean = _invisible
    var voidCancel: Boolean = _voidCancel
}