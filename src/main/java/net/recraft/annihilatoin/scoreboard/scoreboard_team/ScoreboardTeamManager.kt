package net.recraft.annihilatoin.scoreboard.scoreboard_team

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class ScoreboardTeamManager {
    private val teamsScoreboard: EnumMap<GameTeam, ScoreboardTeamPacket> = EnumMap<GameTeam,ScoreboardTeamPacket>(GameTeam::class.java).apply {
        GameTeam.values().forEach {
            put(it, ScoreboardTeamPacket(it.name))
        }
    }
    private val invTeamsScoreboard: EnumMap<GameTeam, ScoreboardTeamPacket> = EnumMap<GameTeam, ScoreboardTeamPacket>(GameTeam::class.java).apply {
        GameTeam.values().forEach {
            put(it, ScoreboardTeamPacket("inv${it.name}"))
        }
    }
    private fun getPacket(team: GameTeam): ScoreboardTeamPacket {
        return teamsScoreboard[team]!!
    }
    private fun getInvPacket(team: GameTeam): ScoreboardTeamPacket {
        return invTeamsScoreboard[team]!!
    }
    fun joinPacket(player: Player) {
        // playerに対して送られる｡
        val playerData = Game.getPlayerData(player.uniqueId)
        if (playerData.team == null) return
        GameTeam.values().forEach {
            if (playerData.team == it) {
                getPacket(it).joinPacket(player)
                return@forEach
            }
            else {
                // 敵チームの透明なプレイヤーを探し出してJoinPlayerにFakeを送る
                getPacket(it).joinPacket(player)
                getInvPacket(it).fakeJoinPacket(player)
            }
        }
        val nonTeamPlayers = Game.getNonPlayers(playerData.team!!)
        nonTeamPlayers.forEach { nonPlayer ->
            val pd = Game.getPlayerData(nonPlayer.uniqueId)
            if (!pd.invisible) return@forEach
            getPacket(pd.team!!).fakeRemovePlayer(nonPlayer, listOf(player))
            getInvPacket(pd.team!!).fakeAddPlayer(nonPlayer, listOf(player))
        }
    }

    fun removeTeam(player: Player) {
        val bound = Bukkit.getOnlinePlayers().toList()
        val playerData = Game.getPlayerData(player.uniqueId)
        getPacket(playerData.team!!).removePlayer(player, bound)
        playerData.team = null
    }
    fun addTeam(player: Player,team: GameTeam) {
        val playerData = Game.getPlayerData(player.uniqueId)
        if (playerData.team != null) {
            removeTeam(player)
        }
        val bound = Bukkit.getOnlinePlayers().toList()
        Game.getPlayerData(player.uniqueId).team = team
        joinPacket(player)
        getPacket(team).addPlayer(player, bound)
    }
    fun setPlayerInv(player: Player): Boolean {
        val team = Game.getPlayerData(player.uniqueId).team ?: return false
        Game.getPlayerData(player.uniqueId).invisible = true
        val scorePacket = getPacket(team)
        val invPacket = getInvPacket(team)
        val bound = Game.getNonPlayers(team)
        invPacket.addPlayer(player, bound)
        scorePacket.fakeRemovePlayer(player, bound)
        return true
    }
    fun releasePlayerInv(player: Player) {
        Game.getPlayerData(player.uniqueId).invisible = false
        val team = Game.getPlayerData(player.uniqueId).team!!
        val scorePacket = getPacket(team)
        val invPacket = getInvPacket(team)
        val bound = Game.getNonPlayers(team)
        invPacket.removePlayer(player, bound)
        scorePacket.fakeAddPlayer(player, bound)
    }

    fun enable() {
        val bound = Bukkit.getOnlinePlayers().toList()
        GameTeam.values().forEach {
            getPacket(it).createTeam(
                _prefix = "${it.chatColor}",
                _nameTagVisibility = ScoreboardTeamPacket.Companion.NameTagInvisibility.ALWAYS,
                _friendlyFire = ScoreboardTeamPacket.Companion.FriendlyFire.FRIENDLY_INVISIBLE,
                bound = bound
            )
            getInvPacket(it).createTeam(
                _prefix = "[inv]${it.chatColor}",
                _nameTagVisibility = ScoreboardTeamPacket.Companion.NameTagInvisibility.NEVER,
                _friendlyFire = ScoreboardTeamPacket.Companion.FriendlyFire.FALSE,
                bound = bound
            )
        }
    }
    fun disable() {
        val bound = Bukkit.getOnlinePlayers().toList()
        GameTeam.values().forEach {
            getPacket(it).removeTeam(bound)
            getInvPacket(it).removeTeam(bound)
        }
    }
}
