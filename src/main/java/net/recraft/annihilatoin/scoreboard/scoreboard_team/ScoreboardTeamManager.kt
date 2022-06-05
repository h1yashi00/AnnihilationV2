package net.recraft.annihilatoin.scoreboard.scoreboard_team

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.invisible
import net.recraft.annihilatoin.objects.Game.setInvisible
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object ScoreboardTeamManager {
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
    fun joinPacket(player: Player, team: GameTeam) {
        // playerに対して送られる｡
        GameTeam.values().forEach {
            if (team == it) {
                getPacket(it).joinPacket(player)
                return@forEach
            }
            else {
                // 敵チームの透明なプレイヤーを探し出してJoinPlayerにFakeを送る
                getPacket(it).joinPacket(player)
                getInvPacket(it).fakeJoinPacket(player)
            }
        }
        val nonTeamPlayers = Game.getNonPlayers(team)
        nonTeamPlayers.forEach { nonPlayer ->
            val nonTeam = nonPlayer.team() ?: return@forEach
            if (!nonPlayer.invisible()) return@forEach
            getPacket(nonTeam).fakeRemovePlayer(nonPlayer, listOf(player))
            getInvPacket(nonTeam).fakeAddPlayer(nonPlayer, listOf(player))
        }
    }

    fun removeTeam(player: Player) {
        val bound = Bukkit.getOnlinePlayers().toList()
        getPacket(player.team()!!).removePlayer(player, bound)
    }
    fun addTeam(player: Player,team: GameTeam) {
//      playerがすでに､他のチームに入っていて､ScoreboardTeamManagerのaddTeamのパケットを送信していた場合､removeTeam(player)をする必要がある｡
        val bound = Bukkit.getOnlinePlayers().toList()
        joinPacket(player,team)
        getPacket(team).addPlayer(player, bound)
    }
    fun setPlayerInv(player: Player): Boolean {
        val team = player.team() ?: return false
        player.setInvisible(true)
        val scorePacket = getPacket(team)
        val invPacket = getInvPacket(team)
        val bound = Game.getNonPlayers(team)
        invPacket.addPlayer(player, bound)
        scorePacket.fakeRemovePlayer(player, bound)
        return true
    }
    fun releasePlayerInv(player: Player) {
        player.setInvisible(false)
        val team = player.team()!!
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
