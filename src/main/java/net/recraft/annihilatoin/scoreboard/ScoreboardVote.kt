package net.recraft.annihilatoin.scoreboard

import net.recraft.annihilatoin.objects.VoteManager
import net.recraft.annihilatoin.util.ScoreboardUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

object ScoreboardVote: ScoreboardUtil() {
    private lateinit var candidates: ArrayList<VoteManager.Candidate>
    fun registerCandidates(_candidates: ArrayList<VoteManager.Candidate>) {
        candidates = _candidates
    }

    fun createObject(objective: Objective) {
        objective.apply {
            displayName = "${ChatColor.YELLOW}${ChatColor.BOLD}VoteMap"
            displaySlot = DisplaySlot.SIDEBAR
            candidates.forEach { getScore(candidateFormat(it)).score = it.section}
            getScore(remainingTime(VoteManager.waitTime)).score = VoteManager.timeSection
        }
    }
    fun voteUpdate(candidate: VoteManager.Candidate) {
        Bukkit.getOnlinePlayers().forEach {
            replaceScore(it, candidate.section, candidateFormat(candidate))
        }
    }
    fun timeUpdate(currentTime: Int) {
        Bukkit.getOnlinePlayers().forEach {
            replaceScore(it, VoteManager.timeSection, remainingTime(currentTime))
        }
    }
    private fun candidateFormat(candidate: VoteManager.Candidate): String {
        return "${ChatColor.BOLD}${ChatColor.DARK_AQUA}${candidate.worldName} ${ChatColor.AQUA}${candidate.amount}"
    }
    private fun remainingTime(time: Int): String {
        return "${ChatColor.GRAY}マップ選択残り時間: ${ChatColor.AQUA}${time}秒"
    }
}