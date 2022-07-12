package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.scoreboard.scoreboard_team.ScoreboardTeamManager
import org.bukkit.Bukkit
import org.bukkit.GameMode
import java.util.*


data class PlayerStats(
    val coins: Int,
    val timesPlayed: Int,
    val bossKills: Int,
    val meleeKills: Int,
    val kills: Int,

    val wins: Int,
    val loses: Int,
    val deaths: Int,

    val nexusDamaged: Int,
    val blocksBroken: Int,
    val oresMined: Int,
    val gapplesConsumed : Int,
    val goldUsed: Int,
    val expsGained: Int
    ) {
}

class PlayerData(
    private val uuid: UUID,
    _team: GameTeam? = null,
    _kitType: KitType = KitType.CIVILIAN,
    _invisible: Boolean = false,
    _voidCancel: Boolean = false
)
{
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