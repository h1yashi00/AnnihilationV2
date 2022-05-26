package net.recraft.annihilatoin.util

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.scoreboard.ScoreboardAnni
import net.recraft.annihilatoin.scoreboard.ScoreboardVote
import org.bukkit.entity.Player

abstract class ScoreboardUtil {
    // プレイヤーがJoin,VoteからAnnihilationの表示の切り替え､reload時にいるプレイヤーに使用
    fun display(player: Player) {
        val scoreboard = player.scoreboard
        val objective = if (scoreboard.getObjective(player.name) == null) scoreboard.registerNewObjective(player.name, "dummy")
                        else scoreboard.getObjective(player.name)
        removeAllScores(player)
        if (Game.isStart) {
            ScoreboardAnni.createObject(objective)
        } else {
            ScoreboardVote.createObject(objective)
        }
    }
    fun replaceScore(player: Player, section: Int, newDisplayStr: String) {
        val scoreboard = player.scoreboard
        val objective  = scoreboard.getObjective(player.name)
        scoreboard.entries.forEach {
            if (objective.getScore(it).score == section) {
                scoreboard.resetScores(it)
                objective.getScore(newDisplayStr).score = section
                return
            }
        }
//        Util.fatal("scoreboard: can't find Section: $section")
    }
    private fun removeAllScores(p: Player) {
        try {
            val sb = p.scoreboard
            for (entry in sb.entries) {
                sb.resetScores(entry)
            }
        } catch (e: Exception) {
        }
    }
}