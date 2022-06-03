package net.recraft.annihilatoin.scoreboard

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.util.ScoreboardUtil
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import java.time.LocalDate

object ScoreboardAnni: ScoreboardUtil() {
    enum class AnniSection(val value: Int) {
        DATE  (15),
        RED   (13),
        BLUE  (12),
        YELLOW(11),
        GREEN (10),
        PHASE (7),
        NEXT_PHASE_TIME(6);
        companion object {
            fun get(name: String): AnniSection? {
                values().forEach { if(name.lowercase() == it.name.lowercase()) return it}
                return null
            }
        }
    }
    // createScoreboard
    fun createObject(objective: Objective) {
        objective.apply {
            displayName = "${ChatColor.YELLOW}${ChatColor.BOLD}Annihilatoin"
            displaySlot = DisplaySlot.SIDEBAR
            getScore(getDateFormat()).score = AnniSection.DATE.value
            getScore("  ").score = 14
            getScore(getNexusHpFormat(GameTeam.RED)).score    = AnniSection.RED.value
            getScore(getNexusHpFormat(GameTeam.BLUE)).score   = AnniSection.BLUE.value
            getScore(getNexusHpFormat(GameTeam.YELLOW)).score = AnniSection.YELLOW.value
            getScore(getNexusHpFormat(GameTeam.GREEN)).score  = AnniSection.GREEN.value
            getScore(" ").score = 9
            getScore("${ChatColor.DARK_PURPLE}${ChatColor.BOLD}Map: ${Game.map.name}").score = 8
            getScore(getCurrentPhaseFormat()).score = AnniSection.PHASE.value
            getScore(nextPhaseTimeFormat()).score = AnniSection.NEXT_PHASE_TIME.value
            getScore("${ChatColor.GRAY}recraft.net").score = 5
        }
    }
    fun currentPhaseUpdate() {
        Bukkit.getOnlinePlayers().forEach {
            replaceScore(it?: return@forEach, AnniSection.PHASE.value, getCurrentPhaseFormat())
        }
    }
    fun nextPhaseTimeUpdate() {
        Bukkit.getOnlinePlayers().forEach {
            replaceScore(it?: return@forEach, AnniSection.NEXT_PHASE_TIME.value, nextPhaseTimeFormat())
        }
    }

    fun breakNexusUpdate(team: GameTeam) {
        Bukkit.getOnlinePlayers().forEach {
            val nexusHp = getNexusHpFormat(team)
            replaceScore(it?: return@forEach, AnniSection.get(team.name)!!.value, nexusHp)
        }
    }

    private fun getCurrentPhaseFormat(): String {
        return "${ChatColor.GRAY}Current Phase ${Game.phase.currentPhase}"
    }

    private fun nextPhaseTimeFormat(): String {
        return "${ChatColor.GRAY}Next time: ${Game.phase.nextPhaseTime}"
    }

    private fun getDateFormat(): String {
        return "${ChatColor.GRAY}${LocalDate.now()}"
    }

    private fun getNexusHpFormat(team: GameTeam): String {
        return "${ChatColor.BOLD}${Util.getColoredTeamName(team)} ${ChatColor.AQUA}${team.objects.nexus.hp}"
    }
}