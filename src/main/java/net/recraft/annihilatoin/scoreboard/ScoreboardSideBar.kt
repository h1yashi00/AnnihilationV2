package net.recraft.annihilatoin.scoreboard

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.DisplaySlot
import java.time.LocalDate

class ScoreboardSideBar  {
    fun update() {
        val scoreboard = Bukkit.getScoreboardManager().newScoreboard
        val objective = scoreboard.registerNewObjective("dummy", "dummy")
        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.displayName = "${ChatColor.GOLD}${ChatColor.BOLD}Annihilation"
        objective.getScore("${ChatColor.GRAY}${LocalDate.now()}").score = 15
        objective.getScore("").score = 14
        objective.getScore(getNexusHpFormat(GameTeam.RED)).score = 13
        objective.getScore(getNexusHpFormat(GameTeam.BLUE)).score = 12
        objective.getScore(getNexusHpFormat(GameTeam.YELLOW)).score = 11
        objective.getScore(getNexusHpFormat(GameTeam.GREEN)).score = 10
        objective.getScore(" ").score = 9
        objective.getScore("${ChatColor.DARK_PURPLE}${ChatColor.BOLD}Map: ${Game.map.name}").score = 8
        objective.getScore("${ChatColor.GRAY}Current Phase" + Game.phase.currentPhase.toString()).score = 7
        objective.getScore("${ChatColor.GRAY}Next time: ${Game.phase.nextPhaseTime}").score = 6
        objective.getScore("${ChatColor.GRAY}recraft.net").score = 5
        Bukkit.getOnlinePlayers().forEach {
            it.scoreboard = scoreboard
        }
    }
    private fun getNexusHpFormat(team: GameTeam): String {
        return "${ChatColor.BOLD}${Util.getColoredTeamName(team)} ${ChatColor.AQUA}${team.objects.nexus.hp}"
    }
}