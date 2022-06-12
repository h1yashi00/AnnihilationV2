package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.scoreboard.scoreboard_team.ScoreboardTeamManager
import org.bukkit.Bukkit
import org.bukkit.GameMode
import java.util.*

data class PlayerStatics(var kills: Int = 0, var deaths: Int = 0, var mined_ores: Int = 0, var gained_exp: Int = 0) {
}

class PlayerData(
    private val uuid: UUID,
    _team: GameTeam? = null,
    _kitType: KitType = KitType.CIVILIAN,
    _invisible: Boolean = false,
    _voidCancel: Boolean = false
)
{
    val statics = PlayerStatics()
    var team: GameTeam? = _team
        set(value) {
            require(value != null)
            val player = Bukkit.getPlayer(uuid)
            if (team != null) {
                ScoreboardTeamManager.removeTeam(player)
            }
            ScoreboardTeamManager.addTeam(player, value)
            field = value
        }
    var kitType: KitType = _kitType
        set(value) {
            val player = Bukkit.getPlayer(uuid)
            player.allowFlight = player.gameMode == GameMode.CREATIVE
            player.healthScale = 20.0
            field = value
        }
    var invisible: Boolean = _invisible
    var voidCancel: Boolean = _voidCancel
}